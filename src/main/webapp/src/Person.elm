module Person exposing (Person, personDecoder)

import Json.Decode as Decode exposing (Decoder, int, string, nullable)
import Json.Decode.Pipeline exposing (required)
import Movie exposing (Movie, movieDecoder)

type alias Person =
    { id : Int
    , name : String
    , movie : Maybe Movie
    }


personDecoder : Decoder Person
personDecoder =
    Decode.succeed Person
        |> required "id" int
        |> required "name" string
        |> required "movie" (nullable movieDecoder)
