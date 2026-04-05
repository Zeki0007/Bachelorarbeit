module Main where

import Criterion.Main
import Visitor
import qualified Data.Map as Map

-- Generator 1: Simple
createSimpleTree :: Int -> Expression
createSimpleTree 0 = Literal 2
createSimpleTree n = Addition (createSimpleTree (n - 1)) (createSimpleTree (n - 1))

-- Generator 2: complex
createComplexTree :: Int -> Int -> Expression
createComplexTree 0 nodeId
    | nodeId `mod` 3 == 0 = Variable ("x" ++ show (nodeId `mod` 10))
    | otherwise           = Literal (nodeId `mod` 100)
createComplexTree depth nodeId =
    let left  = createComplexTree (depth - 1) (nodeId * 2)
        right = createComplexTree (depth - 1) (nodeId * 2 + 1)
    in if depth `mod` 4 == 0
       then Addition (Negation left) right
       else Addition left right

main :: IO ()
main = do
    -- 1. Setup the environment (x0 = 0, x1 = 1, ..., x9 = 9)
    let evalEnv = Map.fromList [("x" ++ show i, i) | i <- [0..9]]

    -- 2. Setup the trees before the measurement
    let depths = [13, 16, 19, 23]
    let simpleTrees  = [(d, createSimpleTree d) | d <- depths]
    let complexTrees = [(d, createComplexTree d 1) | d <- depths]

    -- 3. the benchmark harness
    defaultMain [
        bgroup "SimpleTree" [ 
            bench ("Depth-" ++ show d) $ whnf (evaluate evalEnv) tree
            | (d, tree) <- simpleTrees
        ],
        
        bgroup "ComplexTree" [
            bench ("Depth-" ++ show d) $ whnf (evaluate evalEnv) tree
            | (d, tree) <- complexTrees
        ],

        bgroup "The Lazy Trap" [
            -- Here we don't use whnf, but instead wrap the evaluation in a 'let'
            -- Haskell constructs the thunk (result), but since we only return the string "Done!" 
            -- as the final value, 'result' is never actually calculated!

            bench ("Fake-Evaluation Depth-" ++ show d) $ 
                whnf (\tree -> let result = evaluate evalEnv tree in "Done!") tree
            | (d, tree) <- complexTrees
        ]
      ]
