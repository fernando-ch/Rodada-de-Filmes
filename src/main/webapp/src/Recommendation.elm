module Recommendation exposing
    ( Recommendation
    , NewRecommendation
    , recommendationsDecoder
    , recommendationDecoder
    , newRecommendationEncoder
    , RecommendationWithVisualization
    , recommendationsWithVisualizationsDecoder
    , RecommendationToChoose
    , createRecommendationToChoose
    )

import Json.Decode as Decode exposing (Decoder, int, string, list)
import Json.Decode.Pipeline exposing (required)
import Json.Encode as Encode
import Person exposing (Person, personsDecoder)


type alias Recommendation =
    { id : Int
    , title: String
    }


recommendationDecoder : Decoder Recommendation
recommendationDecoder =
    Decode.succeed Recommendation
        |> required "id" int
        |> required "title" string


recommendationsDecoder : Decoder (List Recommendation)
recommendationsDecoder =
    list recommendationDecoder


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


type alias RecommendationWithVisualization =
    { id : Int
    , title : String
    , personsWhoSawBeforeRound : List Person
    , personWhoSawDuringRound : List Person
    }


recommendationWithVisualizationDecoder : Decoder RecommendationWithVisualization
recommendationWithVisualizationDecoder =
    Decode.succeed RecommendationWithVisualization
        |> required "id" int
        |> required "title" string
        |> required "personsWhoSawBeforeRound" personsDecoder
        |> required "personWhoSawDuringRound" personsDecoder


recommendationsWithVisualizationsDecoder : Decoder (List RecommendationWithVisualization)
recommendationsWithVisualizationsDecoder =
    list recommendationWithVisualizationDecoder


type alias RecommendationToChoose =
    { id : Int
    , title : String
    , personsWhoSawBeforeRound : List Person
    , personWhoSawDuringRound : List Person
    , currentPersonSawItBeforeRound : Bool
    }

createRecommendationToChoose : Person -> RecommendationWithVisualization -> RecommendationToChoose
createRecommendationToChoose person recommendationWithVisualization =
    { id = recommendationWithVisualization.id
    , title = recommendationWithVisualization.title
    , personsWhoSawBeforeRound = recommendationWithVisualization.personsWhoSawBeforeRound
    , personWhoSawDuringRound = recommendationWithVisualization.personWhoSawDuringRound
    , currentPersonSawItBeforeRound = List.any (\personWhoSaw -> personWhoSaw.id == person.id) recommendationWithVisualization.personsWhoSawBeforeRound
    }
