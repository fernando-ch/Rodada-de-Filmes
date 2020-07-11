module Movie exposing
    ( Movie
    , NewMovie
    , movieDecoder
    , newMovieEncoder
    , MovieWithVisualization
    , moviesWithVisualizationsDecoder
    , MovieToChoose
    , createMovieToChoose, moviesToChooseEncoder, Visualization)

import Json.Decode as Decode exposing (Decoder, int, string, list, bool)
import Json.Decode.Pipeline exposing (required)
import Json.Encode as Encode


type alias Movie =
    { id : Int
    , title : String
    , tooManyPeopleAlreadySaw : Bool
    }


movieDecoder : Decoder Movie
movieDecoder =
    Decode.succeed Movie
        |> required "id" int
        |> required "title" string
        |> required "tooManyPeopleAlreadySaw" bool


type alias NewMovie =
    { personId : Int
    , title : String
    }


newMovieEncoder : NewMovie -> Encode.Value
newMovieEncoder newMovie =
    Encode.object
        [ ( "personId",  Encode.int newMovie.personId )
        , ( "title", Encode.string newMovie.title )
        ]


type alias MovieWithVisualization =
    { id : Int
    , title : String
    , visualizations: List Visualization
    }


type alias Visualization =
    { alreadySawBeforeRound : Bool
    , personId : Int
    }


visualizationDecoder : Decoder Visualization
visualizationDecoder =
    Decode.succeed Visualization
        |> required "alreadySawBeforeRound" bool
        |> required "personId" int


movieWithVisualizationDecoder : Decoder MovieWithVisualization
movieWithVisualizationDecoder =
    Decode.succeed MovieWithVisualization
        |> required "id" int
        |> required "title" string
        |> required "visualizations" (list visualizationDecoder)


moviesWithVisualizationsDecoder : Decoder (List MovieWithVisualization)
moviesWithVisualizationsDecoder =
    list movieWithVisualizationDecoder


type alias MovieToChoose =
    { id : Int
    , title : String
    , visualizations: List Visualization
    , currentPersonSawItBeforeRound : Maybe Bool
    }


createMovieToChoose : Int -> MovieWithVisualization -> MovieToChoose
createMovieToChoose personId movieWithVisualization =
    { id = movieWithVisualization.id
    , title = movieWithVisualization.title
    , visualizations = movieWithVisualization.visualizations
    , currentPersonSawItBeforeRound = personSawBeforeRound personId movieWithVisualization
    }


personSawBeforeRound : Int -> MovieWithVisualization -> Maybe Bool
personSawBeforeRound personId movieToChoose =
    case List.filter (\visualization -> visualization.personId == personId) movieToChoose.visualizations of
        head::_ ->
            Just head.alreadySawBeforeRound

        [] ->
            Nothing


movieToChooseEncoder : MovieToChoose -> Encode.Value
movieToChooseEncoder movieToChoose =
    Encode.object
        [ ( "id",  Encode.int movieToChoose.id )
        , ( "title", Encode.string movieToChoose.title )
        , ( "currentPersonSawItBeforeRound", maybe Encode.bool movieToChoose.currentPersonSawItBeforeRound )
        ]


maybe : (a -> Encode.Value) -> Maybe a -> Encode.Value
maybe encoder =
    Maybe.map encoder >> Maybe.withDefault Encode.null


moviesToChooseEncoder : List MovieToChoose -> Encode.Value
moviesToChooseEncoder moviesToChooses =
    Encode.list movieToChooseEncoder moviesToChooses