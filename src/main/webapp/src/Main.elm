module Main exposing (main)

import Browser
import Html exposing (..)
import Html.Attributes exposing (..)
import Html.Events exposing (..)
import Http
import RemoteData exposing (WebData)
import Person exposing (Person, personDecoder)
import Round exposing (Round, Step(..), roundDecoder)
import Movie exposing (Movie, MovieToChoose, MovieWithVisualization, NewMovie, createMovieToChoose, movieDecoder, moviesToChooseEncoder, moviesToSeeDecoder, moviesWithVisualizationsDecoder, newMovieEncoder)


baseUrl : String
baseUrl =
    ""
    --"http://localhost:8080/"


movieUrl =
    baseUrl ++ "movies/"


type alias LoginModel =
    { inputLogin : String
    , person : WebData Person
    }


type alias RoundLoadingModel =
    { person : Person
    , round : WebData Round
    }


type alias MovieFormModel =
    { inputTitle : String
    , person : Person
    , round : Round
    , movie : WebData Movie
    }


type alias MoviesChooseModel =
    { movies : WebData (List MovieToChoose)
    , person : Person
    , round : Round
    , success : Bool
    }


type alias MoviesToSeeModel =
    { movies : WebData (List Movie)
    , person : Person
    , round : Round
    }


type Model
    = LoginFormPage LoginModel
    | RoundLoading RoundLoadingModel
    | MovieFormPage MovieFormModel
    | MoviesChoosePage MoviesChooseModel
    | MovieWatchingPage MoviesToSeeModel


type Message
    = SaveLogin String
    | Login
    | PersonReceived (WebData Person)
    | RoundReceived (WebData Round)
    | SaveTitle String
    | SendMovie
    | MovieSaved (WebData Movie)
    | MoviesReceived (WebData (List MovieWithVisualization))
    | SaveChooseMovie MovieToChoose String
    | SendChooseMovies
    | ChooseMoviesSaved (WebData (List MovieWithVisualization))
    | MoviesToSeeReceived (WebData (List Movie))

view : Model -> Html Message
view model =
    div []
        [ case model of
            LoginFormPage loginModel ->
                viewLoginForm loginModel

            RoundLoading subModel ->
                case subModel.round of
                    RemoteData.Failure error ->
                        case error of
                            Http.BadUrl string ->
                                div [] [ text string]

                            Http.Timeout ->
                                div [] [ text "O servidor está demorando muito para responder" ]

                            Http.NetworkError ->
                                div [] [ text "Verifique sua internet. Se persistir os sintomas o administrador deve ser consultado."]

                            Http.BadStatus status ->
                                div [] [ text ("Erro status" ++ String.fromInt status) ]

                            Http.BadBody string ->
                                div [] [ text ("Erro " ++ string) ]

                    _ ->
                        div [] [ text "Carregando" ]

            MovieFormPage recommendationModel ->
                viewRecommendationForm recommendationModel

            MoviesChoosePage recommendationChooseModel ->
                viewRecommendationsChoose recommendationChooseModel

            MovieWatchingPage moviesToSeeModel ->
                viewMovieWatchingPage moviesToSeeModel
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


viewRecommendationForm : MovieFormModel -> Html Message
viewRecommendationForm model =
    div []
        [ Html.form [ id "form-recommendation", onSubmit SendMovie ]
              [ input [ id "input-recommendation"
                      , onInput SaveTitle
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
            case model.movie of
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
                     case model.person.movie of
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


viewRecommendationsChoose : MoviesChooseModel -> Html Message
viewRecommendationsChoose model =
    div []
        [ case model.movies of
            RemoteData.Success recommendations ->
                div []
                    [ viewRecommendationsChooseTable recommendations model.person
                    , button [ onClick SendChooseMovies ] [ text "Enviar" ]
                    , span [ class "success" ]
                           [ text (if model.success then
                                "Suas respostas foram enviadas com sucesso"

                             else
                                "")
                           ]
                    ]

            RemoteData.Failure error ->
                viewRecommendationsError error

            RemoteData.Loading ->
                span [] [ text "Carregando recommendações" ]

            RemoteData.NotAsked ->
                span [] []
        ]


viewRecommendationsChooseTable : List MovieToChoose -> Person -> Html Message
viewRecommendationsChooseTable recommendations person =
    table []
        [ thead []
                [ th [] [ text "Título" ]
                , th [] []
                ]
        , tbody []
                (List.map (viewRecommendationChoose person) recommendations)
        ]


viewRecommendationChoose : Person -> MovieToChoose -> Html Message
viewRecommendationChoose person movie =
    tr []
       [ td [] [ text movie.title ]
       , td []
            [ label [ for ("inputYes" ++ String.fromInt movie.id) ] [ text "Já vi" ]
            , input [ id ("inputYes" ++ String.fromInt movie.id)
                    , type_ "radio"
                    , name ("answer" ++ String.fromInt movie.id)
                    , value "Sim"
                    , disabled (recommendationFromOwnPerson movie person)
                    , onInput (SaveChooseMovie movie)
                    , checked (Maybe.withDefault False movie.currentPersonSawItBeforeRound)
                    ]
                    []

            , label [ for ("inputNo" ++ String.fromInt movie.id) ] [ text "Não vi" ]
            , input [ id ("inputNo" ++ String.fromInt movie.id)
                    , type_ "radio"
                    , name ("answer" ++ String.fromInt movie.id)
                    , value "Não"
                    , disabled (recommendationFromOwnPerson movie person)
                    , onInput (SaveChooseMovie movie)
                    , checked (Maybe.withDefault False (Maybe.map not movie.currentPersonSawItBeforeRound))
                    ]
                    []
            ]
       ]


recommendationFromOwnPerson : MovieToChoose -> Person -> Bool
recommendationFromOwnPerson recommendation person =
    case person.movie of
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

                Http.BadUrl string ->
                    "Url incorreta " ++ string

                Http.BadStatus int ->
                    "Erro status " ++ String.fromInt int

                Http.BadBody string ->
                    "Body incorreto " ++ string


    in
    span [ class "error" ] [ text errorMessage ]


viewMovieWatchingPage : MoviesToSeeModel -> Html Message
viewMovieWatchingPage model =
    div []
        [ case model.movies of
            RemoteData.Success movies ->
                div []
                    [ viewMoviesToSeeTable movies
                    ]

            RemoteData.Failure error ->
                viewRecommendationsError error

            RemoteData.Loading ->
                span [] [ text "Carregando recommendações" ]

            RemoteData.NotAsked ->
                span [] []
        ]


viewMoviesToSeeTable : List Movie -> Html Message
viewMoviesToSeeTable movies =
    table []
        [ thead []
                [ th [] [ text "Título" ]
                ]
        , tbody []
                (List.map (\movie -> tr [] [ td [] [ text movie.title ] ]) movies)
        ]


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

fetchMovie : Person -> Cmd Message
fetchMovie person =
    Http.get
        { url = movieUrl ++ "search?personId=" ++ String.fromInt person.id
        , expect =
            movieDecoder
                |> Http.expectJson (RemoteData.fromResult >> MovieSaved)
        }

sendMovie : ( Person, String ) -> Cmd Message
sendMovie (person, title) =
    Http.post
        { url = movieUrl
        , body = Http.jsonBody (newMovieEncoder { personId = person.id, title = title })
        , expect =
            movieDecoder
                |> Http.expectJson (RemoteData.fromResult >> MovieSaved)
        }


fetchMovies : Cmd Message
fetchMovies =
    Http.get
        { url = movieUrl
        , expect =
            moviesWithVisualizationsDecoder
                |> Http.expectJson (RemoteData.fromResult >> MoviesReceived)
        }


sendChooseMovies : List MovieToChoose -> Person -> Cmd Message
sendChooseMovies moviesToChoose person =
    Http.post
        { url = movieUrl ++ "mark-what-person-already-saw/" ++ String.fromInt person.id
        , body = Http.jsonBody (moviesToChooseEncoder moviesToChoose)
        , expect =
            moviesWithVisualizationsDecoder
                |> Http.expectJson (RemoteData.fromResult >> ChooseMoviesSaved)
        }


fetchMoviesToSee : Cmd Message
fetchMoviesToSee =
    Http.get
        { url = movieUrl ++ "to-see"
        , expect =
            moviesToSeeDecoder
                |> Http.expectJson (RemoteData.fromResult >> MoviesToSeeReceived)
        }


update : Message -> Model -> ( Model, Cmd Message )
update message model =
    case message of
        SaveLogin login ->
            case model of
                LoginFormPage subModel ->
                    ( LoginFormPage { subModel | inputLogin = login }, Cmd.none )

                _ ->
                   ( model, Cmd.none )

        Login ->
            case model of
                LoginFormPage subModel ->
                    ( model, fetchPerson subModel.inputLogin )

                _ ->
                   ( model, Cmd.none )

        PersonReceived response ->
            case model of
                 LoginFormPage subModel ->
                     case response of
                        RemoteData.Success person ->
                            ( RoundLoading { person = person, round = RemoteData.Loading }, fetchCurrentRound )

                        _ ->
                            ( LoginFormPage { subModel | person = response }, Cmd.none )

                 _ ->
                    ( model, Cmd.none )

        RoundReceived response ->
            case model of
                 RoundLoading subModel ->
                    case response of
                        RemoteData.Success round ->
                            case round.step of
                                Recommendation ->
                                    ( MovieFormPage
                                        { person = subModel.person
                                        , round = round
                                        , movie = RemoteData.Loading
                                        , inputTitle = Maybe.withDefault "" (Maybe.map (\movie -> movie.title) subModel.person.movie)
                                        }
                                    , Cmd.none )

                                WhoSawWhat ->
                                    case subModel.person.movie of
                                        Just recommendation ->
                                            if recommendation.tooManyPeopleAlreadySaw then
                                                ( MovieFormPage
                                                    { person = subModel.person
                                                    , round = round
                                                    , movie = RemoteData.NotAsked
                                                    , inputTitle = ""
                                                    }
                                                , Cmd.none )

                                            else
                                                ( MoviesChoosePage
                                                    { person = subModel.person
                                                    , round = round
                                                    , movies = RemoteData.Loading
                                                    , success = False
                                                    }
                                                 , fetchMovies)

                                        Nothing ->
                                             ( MovieFormPage
                                                 { person = subModel.person
                                                 , round = round
                                                 , movie = RemoteData.Loading
                                                 , inputTitle = ""
                                                 }
                                             , fetchMovie subModel.person )

                                Watching ->
                                    ( MovieWatchingPage
                                        { movies = RemoteData.Loading
                                        , person = subModel.person
                                        , round = round
                                        }
                                     , fetchMoviesToSee
                                     )

                        _ ->
                            ( RoundLoading { subModel | round = response }, Cmd.none )

                 _ ->
                    ( model, Cmd.none )

        SaveTitle title ->
            case model of
                 MovieFormPage subModel ->
                    ( MovieFormPage { subModel | inputTitle = title }, Cmd.none )

                 _ ->
                    ( model, Cmd.none )

        SendMovie ->
            case model of
                 MovieFormPage subModel ->
                    ( model, sendMovie ( subModel.person, subModel.inputTitle ) )

                 _ ->
                    ( model, Cmd.none )

        MovieSaved response ->
            case model of
                 MovieFormPage subModel ->
                    case response of
                        RemoteData.Success movie ->
                            case subModel.round.step of
                                Recommendation ->
                                    let
                                        updatePerson person =
                                            { person | movie = Just movie }
                                    in
                                    ( MovieFormPage
                                        { subModel | inputTitle = movie.title, person = updatePerson subModel.person, movie = response }
                                    , Cmd.none )

                                WhoSawWhat ->
                                    ( MoviesChoosePage
                                       { person = subModel.person
                                       , round = subModel.round
                                       , movies = RemoteData.Loading
                                       , success = False
                                       }
                                    , fetchMovies)

                                Watching ->
                                    ( model, Cmd.none )

                        _ ->
                            ( MovieFormPage { subModel | movie = response }, Cmd.none )

                 _ ->
                    ( model, Cmd.none )

        MoviesReceived response ->
            case model of
                MoviesChoosePage subModel ->
                    let
                        mappedResponse =
                            case response of
                                RemoteData.Success recommendationsWithVisualization ->
                                    let
                                        recommendationsToChoose =
                                            List.map (createMovieToChoose subModel.person.id) recommendationsWithVisualization
                                    in
                                    RemoteData.Success recommendationsToChoose

                                RemoteData.NotAsked ->
                                    RemoteData.NotAsked

                                RemoteData.Loading ->
                                    RemoteData.Loading

                                RemoteData.Failure e ->
                                    RemoteData.Failure e
                    in
                    ( MoviesChoosePage { subModel | movies = mappedResponse }, Cmd.none )

                _ ->
                   ( model, Cmd.none )

        SaveChooseMovie movieToUpdate answer ->
            case model of
                 MoviesChoosePage subModel ->
                     case subModel.movies of
                         RemoteData.Success recommendations ->
                             let
                                 updateRecommendation recommendation =
                                     if recommendation.id == movieToUpdate.id then
                                         { recommendation | currentPersonSawItBeforeRound = Just (answer == "Sim") }

                                     else
                                         recommendation
                             in
                             ( MoviesChoosePage
                                 { subModel | movies = RemoteData.Success (List.map updateRecommendation recommendations)
                                 }
                             , Cmd.none
                             )

                         _ ->
                             ( model, Cmd.none )

                 _ ->
                    ( model, Cmd.none )

        SendChooseMovies ->
            case model of
                 MoviesChoosePage subModel ->
                     case subModel.movies of
                         RemoteData.Success recommendations ->
                            ( MoviesChoosePage { subModel | success = False }, sendChooseMovies recommendations subModel.person )

                         _ ->
                            ( model, Cmd.none )

                 _ ->
                    ( model, Cmd.none )

        ChooseMoviesSaved response ->
            case model of
                MoviesChoosePage subModel ->
                    let
                        ( mappedResponse, success ) =
                            case response of
                                RemoteData.Success recommendationsWithVisualization ->
                                    let
                                        recommendationsToChoose =
                                            List.map (createMovieToChoose subModel.person.id) recommendationsWithVisualization
                                    in
                                    ( RemoteData.Success recommendationsToChoose, True )

                                RemoteData.NotAsked ->
                                    ( RemoteData.NotAsked, False )

                                RemoteData.Loading ->
                                    ( RemoteData.Loading, False )

                                RemoteData.Failure e ->
                                    ( RemoteData.Failure e, False )
                    in
                    ( MoviesChoosePage { subModel | movies = mappedResponse, success = success }, Cmd.none )

                _ ->
                    ( model, Cmd.none )

        MoviesToSeeReceived webData ->
            case model of
                MovieWatchingPage subModel ->
                    ( MovieWatchingPage { subModel | movies = webData }, Cmd.none )

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