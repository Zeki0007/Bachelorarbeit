package at.bachelor.modern.strategy;

public class DiscountService {
    public record CheckoutResult(double finalPrice, ShoppingCart updatedCart) {
        @Override
        public String toString() {
            return String.format("CheckoutResult[finalPrice=%.2f Euro, updatedCart=%s]", finalPrice, updatedCart);
        }
    }

    public static CheckoutResult calculateFinalPrice(ShoppingCart cart){
        return switch (cart.strategy()){
            case NoDiscount() ->
                new CheckoutResult(cart.totalAmount(), cart);
            case PercentageDiscount(double discountPercentage) ->{
                double newPrice = cart.totalAmount() - (cart.totalAmount() * discountPercentage);
                yield new CheckoutResult(newPrice, new ShoppingCart(newPrice, new NoDiscount()));
            }
            case FlatDiscount(double discountAmount) -> {
                double newPrice = cart.totalAmount() - discountAmount;

                if (newPrice < 0) {
                    double remainingDiscount = -newPrice;
                    ShoppingCart newCart = new ShoppingCart(0.0, new FlatDiscount(remainingDiscount));
                    yield new CheckoutResult(0.0, newCart);
                }
                ShoppingCart newCart = new ShoppingCart(newPrice, new NoDiscount());
                yield new CheckoutResult(newPrice, newCart);

            }
        };
    }
}
