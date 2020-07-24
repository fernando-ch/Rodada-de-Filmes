module Movie exposing
    ( Movie
    , movieDecoder
    , moviesDecoder, movieEncoder, findPersonMovieTitle, findPersonMovie, personSawMovieBeforeRound, personDidntSawMovieBeforeRound)

import Json.Decode as Decode exposing (Decoder, int, string, list)
import Json.Decode.Pipeline exposing (required)
import Json.Encode as Encode
import MovieVisualization exposing (MovieVisualization, moviesVisualizationsDecoder)
import Person exposing (Person, personDecoder)


type alias Movie =
    { id : Int
    , title : String
    , tooManyPeopleAlreadySaw : Bool
    , isReadyToBeSeeing : Bool
    , person : Person
    , movieVisualizations : List MovieVisualization
    }


findPersonMovieTitle : Person -> List Movie -> Maybe String
findPersonMovieTitle person movies =
    case List.filter (\movie -> movie.person.id == person.id) movies of
        personMovie::_ ->
            Just personMovie.title

        [] ->
            Nothing


findPersonMovie : Person -> List Movie -> Maybe Movie
findPersonMovie person movies =
    List.filter (\movie -> movie.person.id == person.id) movies |> List.head


personSawMovieBeforeRound : Person -> Movie -> Bool
personSawMovieBeforeRound person movie =
    List.any (\visualization -> visualization.person.id == person.id && visualization.alreadySawBeforeRound) movie.movieVisualizations


personDidntSawMovieBeforeRound : Person -> Movie -> Bool
personDidntSawMovieBeforeRound person movie =
    List.any (\visualization -> visualization.person.id == person.id && not visualization.alreadySawBeforeRound) movie.movieVisualizations


movieDecoder : Decoder Movie
movieDecoder =
    Decode.succeed Movie
        |> required "id" int
        |> required "title" string
        |> required "person" personDecoder
        |> required "movieVisualizations" moviesVisualizationsDecoder


moviesDecoder : Decoder (List Movie)
moviesDecoder =
    list movieDecoder


movieEncoder : { title : String, person : Person } -> Encode.Value
movieEncoder movie =
    Encode.object <|
        [ ( "title", Encode.string movie.title )
        , ( "person", Person.personEncoder movie.person )
        ]