module Strategy where

-- 1. The Strategies (The Sum Type / ADT)
data DiscountStrategy
    = NoDiscount
    | PercentageDiscount Double
    | FlatDiscount Double
    deriving (Show, Eq)

-- 2. The Shopping Cart (The Product Type / Record)
data ShoppingCart = ShoppingCart
    { totalAmount :: Double
    , strategy    :: DiscountStrategy
    } deriving (Show, Eq)

-- 3. The Result Type for Checkout
data CheckoutResult = CheckoutResult
    { finalPrice  :: Double
    , updatedCart :: ShoppingCart
    } deriving (Show, Eq)

-- 4. Checkout-Logic (Pattern Matching)
processCheckout :: ShoppingCart -> CheckoutResult
processCheckout cart@(ShoppingCart amount NoDiscount) = CheckoutResult amount cart -- Case 1: No Discount
processCheckout (ShoppingCart amount (PercentageDiscount percent)) =   -- Case 2: Percentage Discount
    let newPrice = amount * (1 - percent)
    in CheckoutResult newPrice (ShoppingCart newPrice NoDiscount)
processCheckout (ShoppingCart amount (FlatDiscount discount)) = -- Case 3: Flat Discount (with remaining discount logic)
    let newPrice = amount - discount
    in if newPrice < 0
       then 
           let remainingDiscount = -newPrice
               newCart = ShoppingCart 0.0 (FlatDiscount remainingDiscount)
           in CheckoutResult 0.0 newCart
       else 
           let newCart = ShoppingCart newPrice NoDiscount
           in CheckoutResult newPrice newCart