package at.bachelor.classic.strategy;

public class FlatDiscount implements DiscountStrategy{
    private double discountAmount;

    public FlatDiscount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public double applyDiscount(double originalPrice) {
        double discountedPrice = originalPrice - discountAmount;
        if (discountedPrice < 0) {
            discountAmount = -discountedPrice;
            return 0;
        }
        discountAmount = 0;
        return discountedPrice;
    }
}
