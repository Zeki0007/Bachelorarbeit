package at.bachelor.modern.strategy;

public sealed interface DiscountStrategy permits NoDiscount, PercentageDiscount, FlatDiscount {
}
