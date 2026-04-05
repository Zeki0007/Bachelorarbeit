module Visitor where

import qualified Data.Map as Map

-- 1. Data structure (combining sum and product types)
data Expression
    = Literal Int
    | Variable String
    | Negation Expression
    | Addition Expression Expression
    deriving (Show, Eq)

-- 2. Evaluation (computation)
evaluate :: Map.Map String Int -> Expression -> Int
evaluate _   (Literal val)          = val
evaluate env (Variable name)        = 
    case Map.lookup name env of
        Just val -> val
        Nothing  -> error ("Unknown variable: " ++ name)
evaluate env (Negation expr)        = - (evaluate env expr)
evaluate env (Addition left right)  = evaluate env left + evaluate env right

-- 3. Formatting (string generation)
format :: Expression -> String
format (Literal val)         = show val
format (Variable name)       = name
format (Negation expr)       = "(-" ++ format expr ++ ")"
format (Addition left right) = "(" ++ format left ++ " + " ++ format right ++ ")"

-- 4. Metrics (number of nodes and maximum depth)
getMetrics :: Expression -> (Int, Int)
getMetrics (Literal _)         = (1, 1)
getMetrics (Variable _)        = (1, 1)
getMetrics (Negation expr)     = 
    let (nodes, depth) = getMetrics expr
    in (nodes + 1, depth + 1)
getMetrics (Addition left right) = 
    let (lNodes, lDepth) = getMetrics left
        (rNodes, rDepth) = getMetrics right
    in (lNodes + rNodes + 1, max lDepth rDepth + 1)
