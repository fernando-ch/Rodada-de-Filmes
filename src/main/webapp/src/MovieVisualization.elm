module MovieVisualization exposing (MovieVisualization, movieVisualizationDecoder, moviesVisualizationsDecoder)

import Json.Decode as Decode exposing (Decoder, int, string, list)
import Json.Decode.Pipeline exposing (required)
import Json.Encode as Encode
import Person exposing (Person, personDecoder)


type alias MovieVisualization =
    { id : Int
    , alreadySawBeforeRound : Bool
    , alreadySawDuringRound : Bool
    , person : Person
    }


movieVisualizationDecoder : Decoder MovieVisualization
movieVisualizationDecoder =
    Decode.succeed MovieVisualization
        |> required "id" int
        |> required "alreadySawBeforeRound" Decode.bool
        |> required "alreadySawDuringRound" Decode.bool
        |> required "person" personDecoder


moviesVisualizationsDecoder : Decoder (List MovieVisualization)
moviesVisualizationsDecoder =
    (list movieVisualizationDecoder)