package at.bachelor.modern.strategy;

public class Main {
    public static void main(String[] args){
        double cartValue = 100.0;
        System.out.println("Item Value: " + cartValue + " Euro\n");

        // Test 1 No strategy
        ShoppingCart cart1 = new ShoppingCart(cartValue, new NoDiscount());
        System.out.println("Without discount:          " + DiscountService.calculateFinalPrice(cart1));

        // Test 2 20% discount
        ShoppingCart cart2 = new ShoppingCart(cartValue, new PercentageDiscount(0.20));
        System.out.println("20% Discount:              " + DiscountService.calculateFinalPrice(cart2));

        // Test 3 Fixed voucher (15 Euro)
        ShoppingCart cart3 = new ShoppingCart(cartValue, new FlatDiscount(15.0));
        System.out.println("15 Euro voucher:           " + DiscountService.calculateFinalPrice(cart3));

        // Test 4 Edge-Case (150 Euro)
        ShoppingCart cart4 = new ShoppingCart(cartValue, new FlatDiscount(150.0));
        System.out.println("150 Euro voucher:          " + DiscountService.calculateFinalPrice(cart4));
    }
}
