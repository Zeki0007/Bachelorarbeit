package at.bachelor.classic.strategy;

public class PercentageDiscount implements DiscountStrategy{
    private final double discountPercentage;

    public PercentageDiscount(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    @Override
    public double applyDiscount(double originalPrice) {
        return originalPrice - (originalPrice * discountPercentage);
    }
}
