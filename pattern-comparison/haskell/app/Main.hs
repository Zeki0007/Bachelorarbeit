module Main where
{-
import Visitor
import qualified Data.Map as Map

main :: IO ()
main = do
    -- 5 + (-x)
    let expr = Addition (Literal 5) (Negation (Variable "x"))

    let env = Map.fromList [("x", 3)]
    
    let result = evaluate env expr
    let formattedStr = format expr
    let (totalNodes, maxDepth) = getMetrics expr
    
    -- Output
    putStrLn $ "Expression:    " ++ formattedStr
    putStrLn $ "Result:    " ++ show result
    putStrLn $ "Node Count:  " ++ show totalNodes
    putStrLn $ "Max Depth:   " ++ show maxDepth
-}
{- 
import Strategy
main :: IO ()
main = do
    let cartValue = 100.0
    putStrLn $ "Item Value: " ++ show cartValue ++ " Euro\n"

    -- 1. No Strategy
    let cart1 = ShoppingCart cartValue NoDiscount
    let res1  = processCheckout cart1
    putStrLn $ "Without Discount:  " ++ show (finalPrice res1) ++ " Euro"
    putStrLn $ " -> New State of the Shopping Cart: " ++ show (updatedCart res1)
    
    ---2. 20% Discount
    let cart2 = ShoppingCart cartValue (PercentageDiscount 0.20)
    let res2  = processCheckout cart2
    putStrLn $ "20% Discount:      " ++ show (finalPrice res2) ++ " Euro"
    putStrLn $ " -> New State of the Shopping Cart: " ++ show (updatedCart res2)

    -- 3. Flat Discount (15 Euro)
    let cart3 = ShoppingCart cartValue (FlatDiscount 15.0)
    let res3  = processCheckout cart3
    putStrLn $ "15 Euro Coupon:    " ++ show (finalPrice res3) ++ " Euro"
    putStrLn $ " -> New State of the Shopping Cart: " ++ show (updatedCart res3)

    -- 4. Edge Case Test (150 Euro)
    let cart4 = ShoppingCart cartValue (FlatDiscount 150.0)
    let res4  = processCheckout cart4
    putStrLn $ "150 Euro Coupon:   " ++ show (finalPrice res4) ++ " Euro"
    putStrLn $ " -> New State of the Shopping Cart: " ++ show (updatedCart res4)
    -}
import State

main :: IO ()
main = do
    putStrLn "--- State Pattern in Haskell ---"
    
    let doc1 = Draft "My first bachelor's thesis"
    putStrLn $ "Start: " ++ show doc1

    case edit doc1 "My final bachelor's thesis" of
        Left err -> putStrLn $ "Error caught: " ++ err
        Right doc2 -> do
            putStrLn $ "After Edit: " ++ show doc2

            case submitForReview doc2 of
                Left err -> putStrLn $ "Error submitting for review: " ++ err
                Right doc3 -> do
                    putStrLn $ "Submitted: " ++ show doc3

                    putStrLn "\nTrying unauthorized edit..."
                    case edit doc3 "I want to change something!" of
                        Right _  -> putStrLn "Success? This should not happen!"
                        Left err -> putStrLn $ "Error caught: " ++ err

                    case approve doc3 "Prof. Dr. Schmidt" "2026-03-25" of
                        Left err -> putStrLn $ "Error approving: " ++ err
                        Right doc4 -> do
                            putStrLn $ "\nApproved: " ++ show doc4

                            case doc4 of
                                Published content reviewer date -> do
                                    putStrLn "\nSuccessfully read!"
                                    putStrLn $ " -> Content:   " ++ content
                                    putStrLn $ " -> Reviewer: " ++ reviewer
                                    putStrLn $ " -> Date:  " ++ date
                                _ -> putStrLn "Document is not yet published!"