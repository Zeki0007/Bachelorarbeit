package at.bachelor.modern.visitor;

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
        int result = Evaluation.evaluate(negation, env);
        System.out.println("The result is: " + result);
        // Result: (-(5 + (-3))) = -2

        //Test 2
        String formattedExpression = Format.getFormat(negation);
        System.out.println("The result: " + formattedExpression);

        //Test 3
        Metrics.MetricResult metrics = Metrics.getMetrics(negation);
        System.out.println("The result: " + metrics);
    }
}
