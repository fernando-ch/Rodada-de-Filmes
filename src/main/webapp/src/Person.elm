module Person exposing (Person, personDecoder, personEncoder)

import Json.Decode as Decode exposing (Decoder, int, string)
import Json.Decode.Pipeline exposing (required)
import Json.Encode as Encoder

type alias Person =
    { id : Int
    , name : String
    }


personDecoder : Decoder Person
personDecoder =
    Decode.succeed Person
        |> required "id" int
        |> required "name" string


personEncoder : Person -> Encoder.Value
personEncoder person =
    Encoder.object <|
        [ ( "id", Encoder.int person.id )
        , ( "name", Encoder.string person.name )
        ]