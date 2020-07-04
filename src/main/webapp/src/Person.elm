module Person exposing (Person, personDecoder)

import Json.Decode as Decode exposing (Decoder, int, string, nullable)
import Json.Decode.Pipeline exposing (required)
import Recommendation exposing (Recommendation, recommendationDecoder)

type alias Person =
    { id : Int
    , name : String
    , recommendation : Maybe Recommendation
    }


personDecoder : Decoder Person
personDecoder =
    Decode.succeed Person
        |> required "id" int
        |> required "name" string
        |> required "recommendation" (nullable recommendationDecoder)