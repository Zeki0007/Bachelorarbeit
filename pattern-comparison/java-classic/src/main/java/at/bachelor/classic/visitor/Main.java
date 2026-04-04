package at.bachelor.classic.visitor;

import java.util.Arrays;
import java.util.Map;

public class Main {
    public static void main (String[] args){
        // 5 + (-x)
        Expression expression = new Addition(
            new Literal(5),
            new Negation(new Variable("x"))
        );

        Expression negation = new Negation(expression);

        Map<String, Integer> env = Map.of("x", 3);

        // Test 1
        EvaluationVisitor evaluator = new EvaluationVisitor(env);
        int result = negation.accept(evaluator);
        System.out.println("The result is: " + result);
        // Result: (-(5 + (-3))) = -2

        //Test 2
        FormatVisitor formatter = new FormatVisitor();
        String formattedExpression = negation.accept(formatter);
        System.out.println("The result: " + formattedExpression);

        //Test 3
        MetricsVisitor metricsVisitor = new MetricsVisitor();
        Integer[] metrics = negation.accept(metricsVisitor);
        System.out.println("The result: " + Arrays.toString(metrics));
    }
}
