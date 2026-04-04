package at.bachelor.classic.strategy;

public class ShoppingCart {
    private final double totalAmount;
    private DiscountStrategy discountStrategy;

    public ShoppingCart(double totalAmount, DiscountStrategy discountStrategy) {
        this.totalAmount = totalAmount;
        this.discountStrategy = discountStrategy;
    }

    public void setDiscountStrategy(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy;
    }

    public double calculateFinalPrice(){
        return discountStrategy.applyDiscount(totalAmount);
    }
}
