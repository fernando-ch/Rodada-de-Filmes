module Main exposing (main)

import Browser
import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)
import Http
import RemoteData exposing (WebData)
import Person exposing (Person, personDecoder)
import Round exposing (Round, Step(..), roundDecoder)
import Recommendation exposing (Recommendation, NewRecommendation, recommendationDecoder, newRecommendationEncoder)


baseUrl : String
baseUrl =
    "http://localhost:8080/"


type alias Model =
    { inputLogin : String
    , currentRound : WebData Round
    , person : WebData Person
    , inputRecommendation : String
    , recommendation : WebData Recommendation
    }


type Message
    = SaveLogin String
    | Login
    | PersonReceived (WebData Person)
    | RoundReceived (WebData Round)
    | SaveRecommendation String
    | SendRecommendation
    | RecommendationReceive (WebData Recommendation)


view : Model -> Html Message
view model =
    div []
        [ case model.person of
            RemoteData.Success _ ->
                viewRoundPage model
            _ ->
                viewLoginForm model
        ]


viewLoginForm : Model -> Html Message
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


viewRoundPage : Model -> Html Message
viewRoundPage model =
    case model.currentRound of
        RemoteData.Failure error ->
            case error of
                Http.Timeout ->
                  "O servidor está demorando muito. Tente mais tarde."

                Http.NetworkError ->
                    "Verifique sua internet. Se persistir os sintomas o administrador deve ser consultado."

                _ ->
                    "Ocorreu um erro inesperado"

        RemoteData.Success round ->
            case round.step of
                Recommendation ->
                    viewRecommendationForm model

                WhoSawWhat ->
                    viewRecommendations model

        _ ->
            div [] [ text "Carregando..." ]


viewRecommendationForm : Model -> Html Message
viewRecommendationForm model =
    Html.form [ id "form-recommendation", onSubmit SendRecommendation ]
        [ input [ id "input-recommendation"
                , onInput SaveRecommendation
                , type_ "text"
                , placeholder "Digite o do filme"
                ] []
        , viewRecommendationErrorOrNothing model.recommendation
        , button [] [ text "Enviar Recomendação" ]
        ]


viewRecommendationErrorOrNothing : WebData Recommendation -> Html Message
viewRecommendationErrorOrNothing recommendation =
    div [] []


viewRecommendations : Model -> Html Message
viewRecommendations model =
    div [] [ text "Á fazer" ]


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


sendRecommendation : ( WebData Person, String ) -> Cmd Message
sendRecommendation (personData, title) =
    case personData of
        RemoteData.Success person ->
            Http.post
                { url = baseUrl ++ "recommendations"
                , body = Http.jsonBody (newRecommendationEncoder { personId = person.id, title = title })
                , expect =
                    recommendationDecoder
                        |> Http.expectJson (RemoteData.fromResult >> RecommendationReceive)
                }

        _ ->
           Cmd.none


update : Message -> Model -> ( Model, Cmd Message )
update message model =
    case message of
        SaveLogin login ->
            ( { model | inputLogin = login }, Cmd.none )

        Login ->
            ( model, fetchPerson model.inputLogin )

        PersonReceived response ->
            ( { model | person = response }, Cmd.none )

        RoundReceived response ->
            ( { model | currentRound = response }, Cmd.none )

        SaveRecommendation recommendation ->
            ( { model | inputRecommendation = recommendation }, Cmd.none )

        SendRecommendation ->
            ( model, sendRecommendation ( model.person, model.inputRecommendation ) )

        RecommendationReceive webData ->
            ( model, Cmd.none )



init : () -> ( Model, Cmd Message )
init _ =
    ( { inputLogin = ""
      , person = RemoteData.NotAsked
      , currentRound = RemoteData.Loading
      , inputRecommendation = ""
      , recommendation = RemoteData.NotAsked
      }
    , fetchCurrentRound )


main : Program () Model Message
main =
    Browser.element
        { init = init
        , view = view
        , update = update
        , subscriptions = \_ -> Sub.none
        }