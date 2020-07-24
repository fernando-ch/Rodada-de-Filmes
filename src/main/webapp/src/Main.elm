module Main exposing (main)

import Browser
import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)
import Http
import RemoteData exposing (WebData)
import Person exposing (Person, personDecoder)
import Round exposing (Round, Step(..), roundDecoder)
import Movie exposing (Movie, findPersonMovie, findPersonMovieTitle, movieDecoder, movieEncoder, personDidntSawMovieBeforeRound, personSawMovieBeforeRound)
import MovieVisualization exposing (MovieVisualization)


baseUrl : String
baseUrl =
    ""
    --"http://localhost:8080/"


movieUrl =
    baseUrl ++ "movies/"


type Model
    = LoginModel LoginModel
    | MovieFormModel MovieFormModel
    | RoundModel RoundModel


type alias LoginModel =
    { inputLogin : String
    , person : WebData Person
    }


type alias MovieFormModel =
    { person : Person
    , round : Round
    , inputTitle : String
    , formMovie : WebData Movie
    }


type alias RoundModel =
    { person : Person
    , round : WebData Round
    }


type Message
    = LoginMessage LoginMessage
    | RoundMessage RoundMessage
    | MovieFormMessage MovieFormMessage


type LoginMessage
    = SaveLogin String
    | Login
    | PersonReceived (Result Http.Error Person)


type MovieFormMessage
    = SaveTitle String
    | SendMovie
    | MovieSaved (Result Http.Error Movie)


type RoundMessage
    = RoundReceived (Result Http.Error Round)
    | SaveChooseMovie MovieToChoose String


view : Model -> Html Message
view model =
    case model of
        LoginModel loginModel ->
            viewLoginForm loginModel

        MovieFormModel movieFormModel ->
            viewMovieForm movieFormModel

        RoundModel roundModel ->
            viewRound roundModel


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

                          Http.BadStatus status ->
                              case status of
                                  404 ->
                                    "Login inválido. Digite seu nome com a primeira letra maiúscula."

                                  _ ->
                                      "Erro status " ++ String.fromInt status

                          Http.BadUrl string ->
                              "Url incorreta " ++ string

                          Http.BadBody errorMessage ->
                              "Erro: " ++ errorMessage

                    )

                _  ->
                    ( "", "" )
    in
    span [ class className ] [ text message ]


viewMovieForm : MovieFormModel -> Html Message
viewMovieForm model =
    div []
        [ Html.form [ onSubmit SendMovie ]
              [ input [ onInput SaveTitle
                      , type_ "text"
                      , placeholder "Digite o título do filme"
                      , value model.inputTitle
                      ] []
              , viewMovieMessage model
              , button [] [ text "Enviar Recomendação" ]
              ]
        ]


viewMovieMessage : MovieFormModel -> Html Message
viewMovieMessage model =
    let
        ( className, message ) =
            case model.formMovie of
                RemoteData.Failure error ->
                    ( "error"
                    , case error of
                          Http.Timeout ->
                              "O servidor está demorando muito. Tente mais tarde."

                          Http.NetworkError ->
                              "Verifique sua internet. Se persistir os sintomas o administrador deve ser consultado."


                          Http.BadUrl string ->
                              "Url incorreta " ++ string

                          Http.BadStatus status ->
                              "Erro status " ++ String.fromInt status

                          Http.BadBody string ->
                              "Body incorreto " ++ string

                    )

                RemoteData.NotAsked ->
                     case findPersonMovie model.person model.round.movies of
                         Just _ ->
                             ( "error", "Muitas pessoas já viram seu filme. Escolha outro." )

                         Nothing ->
                             ( "", "" )

                RemoteData.Success _ ->
                     ( "success", "Sua recomendação foi enviada com sucesso" )

                _  ->
                    ( "", "" )
    in
    span [ class className ] [ text message ]


viewRound : RoundModel -> Html Message
viewRound model =
    case model.round of
        RemoteData.Success round ->
            case round.step of
                Recommendation ->
                    viewRecommendationStep round model.person

                WhoSawWhat ->
                    viewWhoSawWhatStep round model.person

                Watching ->
                    viewWatchingStep round model.person

        RemoteData.NotAsked ->


        RemoteData.Loading ->


        RemoteData.Failure e ->



viewRecommendationStep : Round -> Person -> Html Message
viewRecommendationStep round person =
    table []
          [ thead []
                  [ th [] [ text "Título" ]
                  , th [] []
                  ]
          , tbody []
                  (List.map (viewMovieToChoose person) round.movies)
          ]


viewWhoSawWhatStep : Round -> Person -> Html Message
viewWhoSawWhatStep round person =



viewMovieToChoose : Person -> Movie -> Html Message
viewMovieToChoose person movie =
    tr []
       [ td [] [ text movie.title ]
       , td []
            [ label [ for ("inputYes" ++ String.fromInt movie.id) ] [ text "Já vi" ]
            , input [ id ("inputYes" ++ String.fromInt movie.id)
                    , type_ "radio"
                    , name ("answer" ++ String.fromInt movie.id)
                    , value "Sim"
                    , disabled (movie.person.id == person.id)
                    , onInput (SaveChooseMovie movie)
                    , checked (personSawMovieBeforeRound person movie)
                    ]
                    []

            , label [ for ("inputNo" ++ String.fromInt movie.id) ] [ text "Não vi" ]
            , input [ id ("inputNo" ++ String.fromInt movie.id)
                    , type_ "radio"
                    , name ("answer" ++ String.fromInt movie.id)
                    , value "Não"
                    , disabled (movie.person.id == person.id)
                    , onInput (SaveChooseMovie movie)
                    , checked (personDidntSawMovieBeforeRound person movie)
                    ]
                    []
            ]
       ]


viewRecommendationsError : Http.Error -> Html Message
viewRecommendationsError error =
    let
        errorMessage =
            case error of
                Http.Timeout ->
                  "O servidor está demorando muito. Tente mais tarde."

                Http.NetworkError ->
                    "Verifique sua internet. Se persistir os sintomas o administrador deve ser consultado."

                Http.BadUrl string ->
                    "Url incorreta " ++ string

                Http.BadStatus int ->
                    "Erro status " ++ String.fromInt int

                Http.BadBody string ->
                    "Body incorreto " ++ string


    in
    span [ class "error" ] [ text errorMessage ]


viewWatchingStep : Round -> Person -> Html Message
viewWatchingStep round person =
    table []
          [ thead []
                  [ th [] [ text "Título" ]
                  ]
          , tbody []
                  (List.map (\movie -> tr [] [ td [] [ text movie.title ] ]) round.movies)
          ]


fetchPerson : String -> Cmd Message
fetchPerson personName =
    Http.get
        { url = baseUrl ++ "people/" ++ personName
        , expect = Http.expectJson (PersonReceived >> LoginMessage) personDecoder
        }


fetchCurrentRound : Cmd Message
fetchCurrentRound =
    Http.get
        { url = baseUrl ++ "rounds/current"
        , expect = Http.expectJson (RoundReceived >> RoundMessage) roundDecoder
        }


postMovie : Person -> String -> Cmd Message
postMovie person title =
    Http.post
        { url = movieUrl
        , body = Http.jsonBody (movieEncoder { person = person, title = title })
        , expect = Http.expectJson (MovieSaved >> MovieFormMessage) movieDecoder
        }


putMovie : { id : Int, title : String } -> Person -> Cmd Message
putMovie movie person =
    Http.request
        { method = "PUT"
        , headers = []
        , url = movieUrl ++ "/" ++ String.fromInt movie.id
        , body = Http.jsonBody (movieEncoder { title = movie.title, person = person })
        , expect = Http.expectJson (MovieSaved >> MovieFormMessage) movieDecoder
        , timeout = Nothing
        , tracker = Nothing
        }


update : Message -> Model -> ( Model, Cmd Message )
update message model =
    case message of
        LoginMessage loginMessage ->
            case model of
                LoginModel loginModel ->
                    updateLogin loginMessage loginModel

                _ ->
                    ( model, Cmd.none )

        RoundMessage roundMessage ->
            case model of
                 RoundModel roundModel ->
                     updateRound roundMessage roundModel

                 _ ->
                     ( model, Cmd.none )

        MovieFormMessage movieFormMessage ->
            case model of
                MovieFormModel movieFormModel ->
                    updateMovieForm movieFormMessage movieFormModel

                _ ->
                    ( model, Cmd.none )


updateLogin : LoginMessage -> LoginModel -> ( Model, Cmd Message )
updateLogin message model =
    case message of
        SaveLogin login ->
            ( LoginModel { model | inputLogin = login }, Cmd.none )

        Login ->
            ( LoginModel model, fetchPerson model.inputLogin )

        PersonReceived response ->
            case response of
                Ok person ->
                    ( RoundModel { person = person, round = RemoteData.Loading }, fetchCurrentRound )

                Err error ->
                    ( LoginModel { model | person = RemoteData.Failure error }, Cmd.none )


updateMovieForm : MovieFormMessage -> MovieFormModel -> ( Model, Cmd Message )
updateMovieForm message model =
    case message of
        SaveTitle title ->
            ( MovieFormModel { model | inputTitle = title }, Cmd.none )

        SendMovie ->
             case findPersonMovie model.person model.round.movies of
                 Just personMovie ->
                     ( MovieFormModel model, putMovie { id = personMovie.id, title = model.inputTitle } model.person )

                 Nothing ->
                     ( MovieFormModel model, postMovie model.person model.inputTitle )

        MovieSaved response ->
            case response of
                Ok movie ->
                    case model.round.step of
                        Recommendation ->
                            ( MovieFormModel
                                { model | inputTitle = movie.title, formMovie = RemoteData.Success movie }
                            , Cmd.none )

                        _ ->
                            ( RoundModel
                               { person = model.person
                               , round = RemoteData.Loading
                               }
                            , fetchCurrentRound
                            )

                Err error ->
                    ( MovieFormModel { model | formMovie = RemoteData.Failure error }, Cmd.none )


updateRound : RoundMessage -> RoundModel -> ( Model, Cmd Message )
updateRound message model =
    case message of
        RoundReceived response ->
            case response of
                Ok round ->
                    case round.step of
                        Recommendation ->
                            ( MovieFormModel (updateRecommendationRound round model), Cmd.none )

                        WhoSawWhat ->
                            updateWhoSawWhatRound round model

                        Watching ->
                         ( RoundModel { person = model.person, round = RemoteData.Success round }, Cmd.none )

                Err error ->
                    ( RoundModel { person = model.person, round = RemoteData.Failure error }, Cmd.none )

        SaveChooseMovie unknown string ->


updateRecommendationRound : Round -> RoundModel -> MovieFormModel
updateRecommendationRound round model =
    { person = model.person
    , round = round
    , inputTitle = Maybe.withDefault "" (findPersonMovieTitle model.person round.movies)
    , formMovie = RemoteData.NotAsked
    }


updateWhoSawWhatRound : Round -> RoundModel -> ( Model, Cmd Message )
updateWhoSawWhatRound round model =
    let
        personMovie =
            List.head (List.filter (\movie -> movie.person.id == model.person.id) round.movies)

        getCurrentPage movie =
            if movie.tooManyPeopleAlreadySaw then
                ( MovieFormModel
                    { person = model.person
                    , round = round
                    , inputTitle = movie.title
                    , formMovie = RemoteData.NotAsked
                    }
                , Cmd.none )
            else
                ( RoundModel
                    { person = model.person
                    , round = RemoteData.Success round
                    }
                , Cmd.none
                )
    in
    case personMovie of
     Just movie ->
         getCurrentPage movie

     Nothing ->
         ( RoundModel { model | round = RemoteData.Success round }, Cmd.none )


init : () -> ( Model, Cmd Message )
init _ =
    ( LoginModel { inputLogin = "", person = RemoteData.NotAsked }
    , Cmd.none )


main : Program () Model Message
main =
    Browser.element
        { init = init
        , view = view
        , update = update
        , subscriptions = \_ -> Sub.none
        }