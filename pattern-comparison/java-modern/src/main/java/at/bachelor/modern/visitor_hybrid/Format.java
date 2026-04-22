package at.bachelor.modern.visitor_hybrid;

public class Format {
    public static String getFormat(Expression expression) {
        return switch (expression) {
            case Literal(int value) -> String.valueOf(value);
            case Variable(String name) -> name;
            case Negation(Expression e) -> "(-" + getFormat(e)+")";
            case Addition(Expression left, Expression right) -> "(" + getFormat(left) + " + " + getFormat(right) + ")";
            case Operation unknown -> throw new IllegalArgumentException("Unknown operation!");
        };
    }
}
