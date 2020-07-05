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


type LoginMessage
    = SaveLogin String
    | Login
    | PersonReceived (WebData Person)


type RoundLoadingMessage
    = RoundReceived (WebData Round)


type RecommendationMessage
    = SaveRecommendation String
    | SendRecommendation
    | RecommendationReceived (WebData Recommendation)


type RecommendationsChooseMessage
    = RecommendationsReceived (WebData (List Recommendation))
    | SaveChooseRecommendation Recommendation String


type Message
    = LoginPageMessage LoginMessage
    | RoundLoadingPageMessage RoundLoadingMessage
    | RecommendationPageMessage RecommendationMessage
    | RecommendationsChoosePageMessage RecommendationsChooseMessage


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
                |> Http.expectJson (RemoteData.fromResult >> PersonReceived >> LoginPageMessage)
        }


fetchCurrentRound : Cmd Message
fetchCurrentRound =
    Http.get
        { url = baseUrl ++ "rounds/current"
        , expect =
            roundDecoder
                |> Http.expectJson (RemoteData.fromResult >> RoundReceived >> RoundLoadingPageMessage)
        }

fetchRecommendation : Person -> Cmd Message
fetchRecommendation person =
    Http.get
        { url = baseUrl ++ "recommendations/search/?personId=" ++ String.fromInt person.id
        , expect =
            recommendationDecoder
                |> Http.expectJson (RemoteData.fromResult >> RecommendationReceived >> RecommendationPageMessage)
        }

sendRecommendation : ( Person, String ) -> Cmd Message
sendRecommendation (person, title) =
    Http.post
        { url = baseUrl ++ "recommendations"
        , body = Http.jsonBody (newRecommendationEncoder { personId = person.id, title = title })
        , expect =
            recommendationDecoder
                |> Http.expectJson (RemoteData.fromResult >> RecommendationReceived >> RecommendationPageMessage)
        }


fetchRecommendations : Cmd Message
fetchRecommendations =
    Http.get
        { url = baseUrl ++ "recommendations"
        , expect =
            recommendationsDecoder
                |> Http.expectJson (RemoteData.fromResult >> RecommendationsReceived >> RecommendationsChoosePageMessage)
        }


update : Message -> Model -> ( Model, Cmd Message )
update message model =
    case ( message, model ) of
        ( LoginPageMessage subMessage, LoginFormPage subModel) ->
            case subMessage of
                SaveLogin login ->
                    ( LoginFormPage { subModel | inputLogin = login }, Cmd.none )

                Login ->
                    ( model, fetchPerson subModel.inputLogin )

                PersonReceived response ->
                    case response of
                        RemoteData.Success person ->
                            ( RoundLoading { person = person, round = RemoteData.Loading }, fetchCurrentRound )

                        _ ->
                            ( LoginFormPage { subModel | person = response }, Cmd.none )

        ( LoginPageMessage _, _ ) ->
            ( model, Cmd.none )

        ( RoundLoadingPageMessage subMessage, RoundLoading subModel ) ->
            case subMessage of
                RoundReceived response ->
                    case response of
                        RemoteData.NotAsked ->
                            ( RoundLoading subModel, fetchCurrentRound )

                        RemoteData.Success round ->
                            case round.step of
                                Recommendation ->
                                    ( RecommendationFormPage
                                        { person = subModel.person
                                        , round = round
                                        , recommendation = RemoteData.Loading
                                        , inputRecommendation = ""
                                        }
                                    , fetchRecommendation subModel.person )

                                WhoSawWhat ->
                                    ( RecommendationsChoosePage
                                        { person = subModel.person
                                        , round = round
                                        , recommendations = RemoteData.Loading
                                        }
                                     , fetchRecommendations)

                        _ ->
                            ( RoundLoading subModel, Cmd.none )

        ( RoundLoadingPageMessage _, _ ) ->
            ( model, Cmd.none )

        ( RecommendationPageMessage subMessage, RecommendationFormPage subModel ) ->
            case subMessage of
                SaveRecommendation recommendation ->
                    ( RecommendationFormPage { subModel | inputRecommendation = recommendation }, Cmd.none )

                SendRecommendation ->
                    ( model, sendRecommendation ( subModel.person, subModel.inputRecommendation ) )

                RecommendationReceived response ->
                    case response of
                        RemoteData.Success recommendation ->
                            let
                                updatePerson person =
                                    { person | recommendation = Just recommendation }
                            in
                            ( RecommendationFormPage
                                { subModel | inputRecommendation = recommendation.title, person = updatePerson subModel.person }
                            , Cmd.none )

                        _ ->
                            ( RecommendationFormPage subModel, Cmd.none )

        ( RecommendationPageMessage _, _ ) ->
            ( model, Cmd.none )

        ( RecommendationsChoosePageMessage subMessage, RecommendationsChoosePage subModel ) ->
            case subMessage of
                RecommendationsReceived response ->
                    ( RecommendationsChoosePage { subModel | recommendations = response }, Cmd.none )

                SaveChooseRecommendation recommendation answer ->
                    ( model, Cmd.none )

        ( RecommendationsChoosePageMessage _, _ ) ->
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