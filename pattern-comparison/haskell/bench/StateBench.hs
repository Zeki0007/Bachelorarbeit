{- HLINT ignore "Redundant return" -}
module Main where

import Criterion.Main
import State

workflow :: Int -> Either String DocumentState
workflow i = do
    let draft = Draft ("Title " ++ show i)
    edited   <- edit draft "This is an important bachelor thesis text."
    inReview <- submitForReview edited
    published <- approve inReview "Prof. Dr. Mueller" "2026-03-31"
    return published

runWorkflow :: Int -> Int
runWorkflow i = case workflow i of
    Right (Published text _ _) -> length text
    Left _                     -> 0

-- 3. The numbers 1 to 1,000,000
workflows :: [Int]
workflows = [1..1000000]

main :: IO ()
main = defaultMain [
    bgroup "State-Pattern (1M Documents)" [
        bench "Either Workflow (Immutability)" $ whnf (\xs -> sum (map runWorkflow xs)) workflows
    ]
  ]
