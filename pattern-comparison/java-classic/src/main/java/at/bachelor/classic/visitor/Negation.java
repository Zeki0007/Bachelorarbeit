package at.bachelor.classic.visitor;

public class Negation implements Expression{
    private final Expression expression;

    public Negation(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visitNegation(this);
    }
}
