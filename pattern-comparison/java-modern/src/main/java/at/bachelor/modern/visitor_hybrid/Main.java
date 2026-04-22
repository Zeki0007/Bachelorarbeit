package at.bachelor.modern.visitor_hybrid;

import java.util.Map;

// 1. We are simulating a "foreign" developer.
//Since 'Operation' is non-sealed, we can simply do this!
record Multiplication(Expression left, Expression right) implements Operation {}

public class Main {
    public static void main (String[] args){
        System.out.println("--- Test 1: Closed / Sealed Tree ---");
        Expression safeTree = new Addition(new Literal(5), new Literal(10));
        System.out.println("Result: " + Evaluation.evaluate(safeTree, Map.of()));


        System.out.println("\n--- Test 2: Non Sealed Tree ---");
        Expression unsafeTree = new Addition(
            new Literal(5),
            new Multiplication(new Literal(2), new Literal(3))
        );

        try {
            // This will crash and end up in the 'default' branch of our switch!
            Evaluation.evaluate(unsafeTree, Map.of());
        } catch (IllegalArgumentException e) {
            System.out.println("Crash successfully proven:\n " + e.getMessage());
            System.out.println("-> Conclusion: Functional exhaustiveness was destroyed by 'non-sealed'\n.");
        }
    }
}
