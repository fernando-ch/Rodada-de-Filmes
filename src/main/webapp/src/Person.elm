module Person exposing (Person, personDecoder)

import Json.Decode as Decode exposing (Decoder, int, string)
import Json.Decode.Pipeline exposing (required)

type alias Person =
    { id : Int
    , name : String
    }


personDecoder : Decoder Person
personDecoder =
    Decode.succeed Person
        |> required "id" int
        |> required "name" string