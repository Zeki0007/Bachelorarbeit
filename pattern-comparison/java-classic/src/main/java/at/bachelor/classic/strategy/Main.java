package at.bachelor.classic.strategy;

public class Main {
    public static void main(String[] args) {
        double cartValue = 100.0;
        System.out.println("Item Value: " + cartValue + " euro\n");

        // Test 1 No strategy
        ShoppingCart cart = new ShoppingCart(cartValue, new NoDiscount());
        System.out.println("Without discount:          " + cart.calculateFinalPrice() + " Euro");

        // Test 2 We dynamically change the strategy (20% discount)
        cart.setDiscountStrategy(new PercentageDiscount(0.20));
        System.out.println("20% Discount:              " + cart.calculateFinalPrice() + " Euro");

        // Test 3 Fixed voucher (15 euro)
        cart.setDiscountStrategy(new FlatDiscount(15.0));
        System.out.println("15 Euro voucher:           " + cart.calculateFinalPrice() + " Euro");

        // Test 4 Edge-Case Test: Fixed voucher (150 euro) - must not fall below 0
        cart.setDiscountStrategy(new FlatDiscount(150.0));
        System.out.println("150 Euro voucher:          " + cart.calculateFinalPrice() + " Euro");
    }

}
