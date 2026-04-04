package at.bachelor.classic.visitor;

public interface Expression {
    <T> T accept(Visitor<T> visitor);
}
