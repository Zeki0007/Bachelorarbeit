module Generics where
    import Prelude hiding (Either, Left, Right) --we ignore the existing Either to use ower new created MyEither

    data MyEither l r = Left l | Right r

    safeDivide :: Int -> Int -> MyEither String Int
    safeDivide _ 0 = Left "Division by zero!"
    safeDivide x y = Right (x `div` y)