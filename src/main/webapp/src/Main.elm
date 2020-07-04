module Main exposing (main)

import Browser
import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)
import Http
import RemoteData exposing (WebData)
import Person exposing (Person, personDecoder)
import Round exposing (Round, Step(..), roundDecoder)
import Recommendation exposing (Recommendation, NewRecommendation, recommendationDecoder, recommendationsDecoder, newRecommendationEncoder)


baseUrl : String
baseUrl =
    "http://localhost:8080/"


type alias LoginModel =
    { inputLogin : String
    , person : WebData Person
    }


type alias RoundLoadingModel =
    { person : Person
    , round : WebData Round
    }


type alias RecommendationModel =
    { inputRecommendation : String
    , person : Person
    , round : Round
    , recommendation : WebData Recommendation
    }


type alias RecommendationsChooseModel =
    { recommendations : WebData (List Recommendation)
    , person : Person
    , round : Round
    }


type Model
    = LoginFormPage LoginModel
    | RoundLoading RoundLoadingModel
    | RecommendationFormPage RecommendationModel
    | RecommendationsChoosePage RecommendationsChooseModel


type Message
    = SaveLogin String
    | Login
    | PersonReceived (WebData Person)
    | RoundReceived (WebData Round)
    | SaveRecommendation String
    | SendRecommendation
    | RecommendationReceived (WebData Recommendation)
    | RecommendationsReceived (WebData (List Recommendation))
    | SaveChooseRecommendation Recommendation String


view : Model -> Html Message
view model =
    div []
        [ case model of
            LoginFormPage loginModel ->
                viewLoginForm loginModel

            RoundLoading _ ->
                div [] []

            RecommendationFormPage recommendationModel ->
                viewRecommendationForm recommendationModel

            RecommendationsChoosePage recommendationChooseModel ->
                viewRecommendationsChoose recommendationChooseModel
        ]


viewLoginForm : LoginModel -> Html Message
viewLoginForm model =
    Html.form [ id "form-login", onSubmit Login ]
        [ input [ id "input-login"
                , onInput SaveLogin
                , type_ "text"
                , placeholder "Digite o seu nome. Ex: Fernando"
                ] []
        , viewLoginErrorOrNothing model.person
        , button [] [ text "Entrar" ]
        ]


viewLoginErrorOrNothing : WebData Person -> Html Message
viewLoginErrorOrNothing person =
    let
        ( className, message ) =
            case person of
                RemoteData.Failure error ->
                    ( "error"
                    , case error of
                          Http.Timeout ->
                              "O servidor está demorando muito. Tente mais tarde."

                          Http.NetworkError ->
                              "Verifique sua internet. Se persistir os sintomas o administrador deve ser consultado."

                          Http.BadStatus 404 ->
                              "Login inválido. Digite seu nome com a primeira letra maiúscula."

                          _ ->
                              "Ocorreu um erro inesperado"
                    )

                _  ->
                    ( "", "" )
    in
    span [ class className ] [ text message ]


viewRecommendationForm : RecommendationModel -> Html Message
viewRecommendationForm model =
    div []
        [ Html.form [ id "form-recommendation", onSubmit SendRecommendation ]
              [ input [ id "input-recommendation"
                      , onInput SaveRecommendation
                      , type_ "text"
                      , placeholder "Digite o do filme"
                      ] []
              , viewRecommendationErrorOrNothing model.recommendation
              , button [] [ text "Enviar Recomendação" ]
              ]
          , case model.recommendation of
                RemoteData.Success _ ->
                    span [] [ text "Sua recomendação foi enviada com sucesso" ]

                _ ->
                    span [] []
        ]


viewRecommendationErrorOrNothing : WebData Recommendation -> Html Message
viewRecommendationErrorOrNothing recommendation =
    let
        ( className, message ) =
            case recommendation of
                RemoteData.Failure error ->
                    ( "error"
                    , case error of
                          Http.Timeout ->
                              "O servidor está demorando muito. Tente mais tarde."

                          Http.NetworkError ->
                              "Verifique sua internet. Se persistir os sintomas o administrador deve ser consultado."

                          _ ->
                              "Ocorreu um erro inesperado"
                    )

                _  ->
                    ( "", "" )
    in
    span [ class className ] [ text message ]


viewRecommendationsChoose : RecommendationsChooseModel -> Html Message
viewRecommendationsChoose model =
    div []
        [ case model.recommendations of
            RemoteData.Success recommendations ->
                viewRecommendationsChooseTable recommendations model.person

            RemoteData.Failure error ->
                viewRecommendationsError error

            RemoteData.Loading ->
                span [] [ text "Carregando recommendações" ]

            RemoteData.NotAsked ->
                span [] []
        ]


viewRecommendationsChooseTable : List Recommendation -> Person -> Html Message
viewRecommendationsChooseTable recommendations person =
    table []
        [ thead []
                [ th [] [ text "Título" ]
                , th [] []
                ]
        , tbody []
                [ List.map (viewRecommendationChoose person) recommendations
                ]
        ]


viewRecommendationChoose : Person -> Recommendation -> Html Message
viewRecommendationChoose person recommendation =
    tr []
       [ td [] [ text recommendation.title ]
       , td []
            [ input [ type_ "radio", name "answer", disabled (disableRadio recommendation person), onInput (SaveChooseRecommendation recommendation) ]
                    [ text "Sim" ]
            , input [ type_ "radio", name "answer", disabled (disableRadio recommendation person), onInput (SaveChooseRecommendation recommendation) ]
                    [ text "Não" ]
            ]
       ]


disableRadio : Recommendation -> Person -> Bool
disableRadio recommendation person =
    case person.recommendation of
        Nothing ->
            False

        Just personRecommendation ->
            recommendation.id == personRecommendation.id


viewRecommendationsError : Http.Error -> Html Message
viewRecommendationsError error =
    let
        errorMessage =
            case error of
                Http.Timeout ->
                  "O servidor está demorando muito. Tente mais tarde."

                Http.NetworkError ->
                    "Verifique sua internet. Se persistir os sintomas o administrador deve ser consultado."

                _ ->
                    "Ocorreu um erro inesperado"
    in
    span [ class "error" ] [ text errorMessage ]



fetchPerson : String -> Cmd Message
fetchPerson personName =
    Http.get
        { url = baseUrl ++ "people/" ++ personName
        , expect =
            personDecoder
                |> Http.expectJson (RemoteData.fromResult >> PersonReceived)
        }


fetchCurrentRound : Cmd Message
fetchCurrentRound =
    Http.get
        { url = baseUrl ++ "rounds/current"
        , expect =
            roundDecoder
                |> Http.expectJson (RemoteData.fromResult >> RoundReceived)
        }

fetchRecommendation : Person -> Cmd Message
fetchRecommendation person =
    Http.get
        { url = baseUrl ++ "recommendations/search/?personId=" ++ String.fromInt person.id
        , expect =
            recommendationDecoder
                |> Http.expectJson (RemoteData.fromResult >> RecommendationReceived)
        }

sendRecommendation : ( Person, String ) -> Cmd Message
sendRecommendation (person, title) =
    Http.post
        { url = baseUrl ++ "recommendations"
        , body = Http.jsonBody (newRecommendationEncoder { personId = person.id, title = title })
        , expect =
            recommendationDecoder
                |> Http.expectJson (RemoteData.fromResult >> RecommendationReceived)
        }


fetchRecommendations : Cmd Message
fetchRecommendations =
    Http.get
        { url = baseUrl ++ "recommendations"
        , expect =
            recommendationsDecoder
                |> Http.expectJson (RemoteData.fromResult >> RecommendationsReceived)
        }


update : Message -> Model -> ( Model, Cmd Message )
update message model =
    case model of
        LoginFormPage loginModel ->
            case message of
                    SaveLogin login ->
                        ( LoginFormPage { loginModel | inputLogin = login }, Cmd.none )

                    Login ->
                        ( model, fetchPerson loginModel.inputLogin )

                    PersonReceived response ->
                        case response of
                            RemoteData.Success person ->
                                ( RoundLoading { person = person, round = RemoteData.Loading }, fetchCurrentRound )

                            _ ->
                                ( LoginFormPage { loginModel | person = response }, Cmd.none )

                    _ ->
                        ( model, Cmd.none )

        RoundLoading roundLoadingModel ->
            case message of
                RoundReceived response ->
                    case response of
                        RemoteData.NotAsked ->
                            ( RoundLoading roundLoadingModel, fetchCurrentRound )

                        RemoteData.Success round ->
                            case round.step of
                                Recommendation ->
                                    ( RecommendationFormPage
                                        { person = roundLoadingModel.person
                                        , round = round
                                        , recommendation = RemoteData.Loading
                                        , inputRecommendation = ""
                                        }
                                    , fetchRecommendation roundLoadingModel.person )

                                WhoSawWhat ->
                                    ( RecommendationsChoosePage
                                        { person = roundLoadingModel.person
                                        , round = round
                                        , recommendations = RemoteData.Loading
                                        }
                                     , fetchRecommendations)

                        _ ->
                            ( RoundLoading roundLoadingModel, Cmd.none )

                _ ->
                    ( RoundLoading roundLoadingModel, Cmd.none )

        RecommendationFormPage recommendationModel ->
            case message of
                SaveRecommendation recommendation ->
                    ( RecommendationFormPage { recommendationModel | inputRecommendation = recommendation }, Cmd.none )

                SendRecommendation ->
                    ( model, sendRecommendation ( recommendationModel.person, recommendationModel.inputRecommendation ) )

                RecommendationReceived response ->
                    case response of
                        RemoteData.Success recommendation ->
                            let
                                updatePerson person =
                                    { person | recommendation = Just recommendation }
                            in
                            ( RecommendationFormPage
                                { recommendationModel | inputRecommendation = recommendation.title, person = updatePerson recommendationModel.person }
                            , Cmd.none )

                        _ ->
                            ( RecommendationFormPage recommendationModel, Cmd.none )

                _ ->
                    ( model, Cmd.none )

        RecommendationsChoosePage recommendationsChooseModel ->
            case message of
                RecommendationsReceived response ->
                    ( RecommendationsChoosePage { recommendationsChooseModel | recommendations = response }, Cmd.none )

                SaveChooseRecommendation recommendation answer ->
                    ( model, Cmd.none )

                _ ->
                    ( model, Cmd.none )


init : () -> ( Model, Cmd Message )
init _ =
    ( LoginFormPage { inputLogin = "", person = RemoteData.NotAsked }
    , Cmd.none )


main : Program () Model Message
main =
    Browser.element
        { init = init
        , view = view
        , update = update
        , subscriptions = \_ -> Sub.none
        }