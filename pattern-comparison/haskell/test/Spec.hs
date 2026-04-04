module Main where

import Test.Hspec
import Test.QuickCheck
import AST
import qualified Data.Map as Map

instance Arbitrary Expression where
    arbitrary = sized genExpr
      where
        genExpr 0 = oneof [ Literal <$> arbitrary -- Generates a random number
                          , Variable <$> elements ["x", "y", "z"] -- Randomly selects x, y or z
                          ]
        genExpr n = oneof [ Literal <$> arbitrary
                          , Variable <$> elements ["x", "y", "z"]
                          -- Recursion: Builds random additions and negations
                          , Addition <$> genExpr (n `div` 2) <*> genExpr (n `div` 2)
                          , Negation <$> genExpr (n - 1)
                          ]

-- A standard environment for our tests (x=10, y=20, z=30)
testEnv :: Map.Map String Int
testEnv = Map.fromList [("x", 10), ("y", 20), ("z", 30)]

main :: IO ()
main = hspec $ do
    describe "AST Evaluation (Property-based Testing)" $ do
        
        it "satisfies identity: (Expression + 0) equals Expression" $
            property $ \expr -> 
                evaluate testEnv (Addition expr (Literal 0)) == evaluate testEnv expr
                
        it "satisfies commutativity: (A + B) equals (B + A)" $
            property $ \exprA exprB -> 
                evaluate testEnv (Addition exprA exprB) == evaluate testEnv (Addition exprB exprA)
                
        it "satisfies double negation: -(-Expression) equals Expression" $
            property $ \expr -> 
                evaluate testEnv (Negation (Negation expr)) == evaluate testEnv expr
