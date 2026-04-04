package at.bachelor.modern.strategy;

public record ShoppingCart(double totalAmount, DiscountStrategy strategy) {
}
