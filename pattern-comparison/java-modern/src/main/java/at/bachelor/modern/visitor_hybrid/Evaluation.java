package at.bachelor.modern.visitor_hybrid;

import java.util.Map;

public class Evaluation {
    public static int evaluate(Expression expression, Map<String, Integer> env) {
        return switch (expression) {
            case Literal(int value) -> value;
            case Variable(String name) -> getVariableValue(name, env);
            case Negation(Expression e) -> -evaluate(e, env);
            case Addition(Expression left, Expression right) -> evaluate(left, env) + evaluate(right, env);
            case Operation unknown -> throw new IllegalArgumentException("Unknown operation!");
        };
    }

    private static int getVariableValue(String name,Map<String, Integer> env){
        if (!env.containsKey(name)) {
            throw new IllegalArgumentException("Unknown variable: " + name);
        }
        return env.get(name);
    }

}
