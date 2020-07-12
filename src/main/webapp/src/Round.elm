module Round exposing (Round, Step(..), roundDecoder)

import Json.Decode as Decode exposing (Decoder, int, bool)
import Json.Decode.Pipeline exposing (required)


type alias Round =
    { id : Int
    , current : Bool
    , step : Step
    }


roundDecoder : Decoder Round
roundDecoder =
    Decode.succeed Round
        |> required "id" int
        |> required "current" bool
        |> required "step" stepDecoder


type Step
    = Recommendation
    | WhoSawWhat
    | Watching


stepDecoder : Decoder Step
stepDecoder =
    Decode.string
        |> Decode.andThen (\str ->
           case str of
                "Recommendation" ->
                    Decode.succeed Recommendation

                "WhoSawWhat" ->
                    Decode.succeed WhoSawWhat

                "Watching" ->
                    Decode.succeed Watching

                somethingElse ->
                    Decode.fail <| "Etapa da rodada desconhecida: " ++ somethingElse
        )