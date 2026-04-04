module Main where

import Criterion.Main
import Strategy

carts :: [ShoppingCart]
carts = [ ShoppingCart (fromIntegral ((i * 137) `mod` 2000)) (PercentageDiscount 0.20) | i <- [0..9999999] ]

cleanPipeline :: [ShoppingCart] -> Double
cleanPipeline cs =
    let
        results    = map processCheckout cs
        filtered   = filter (\res -> finalPrice res > 1000.0) results
        top5       = take 5 filtered
    in sum (map finalPrice top5)

main :: IO ()
main = do
    -- The Benchmark-Harness
    defaultMain [
        bgroup "Strategy-Pattern (10 Million Shopping Carts)" [
            -- We measure our clean pipeline
            bench "Clean Pipeline (Lazy Evaluation)" $ whnf cleanPipeline carts
        ]
      ]
