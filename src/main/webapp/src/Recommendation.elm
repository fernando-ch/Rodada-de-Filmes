module Recommendation exposing (Recommendation, NewRecommendation, recommendationDecoder, newRecommendationEncoder)

import Json.Decode as Decode exposing (Decoder, int, string)
import Json.Decode.Pipeline exposing (required)
import Json.Encode as Encode


type alias Recommendation =
    { id : Int
    , title: String
    }


recommendationDecoder : Decoder Recommendation
recommendationDecoder =
    Decode.succeed Recommendation
        |> required "id" int
        |> required "title" string


type alias NewRecommendation =
    { personId : Int
    , title : String
    }


newRecommendationEncoder : NewRecommendation -> Encode.Value
newRecommendationEncoder newRecommendation =
    Encode.object
        [ ( "personId",  Encode.int newRecommendation.personId )
        , ( "title", Encode.string newRecommendation.title )
        ]