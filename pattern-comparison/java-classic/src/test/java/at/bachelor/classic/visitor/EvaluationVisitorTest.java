package at.bachelor.classic.visitor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EvaluationVisitorTest {

    private EvaluationVisitor evaluator;
    private Map<String, Integer> environment;

    @BeforeEach
    public void setup() {
        environment = new HashMap<>();
        environment.put("x", 10);
        environment.put("y", 20);
        environment.put("z", 30);
        evaluator = new EvaluationVisitor(environment);
    }

    @Test
    public void testIdentity() {
        Expression expr = new Variable("x");
        Expression additionMitNull = new Addition(expr, new Literal(0));

        int resultOriginal = expr.accept(evaluator);
        int resultMitNull = additionMitNull.accept(evaluator);

        assertEquals(resultOriginal, resultMitNull, "Identity violated!");
    }

    @Test
    public void testCommutativity() {
        Expression exprA = new Literal(15);
        Expression exprB = new Variable("y");

        Expression aPlusB = new Addition(exprA, exprB);
        Expression bPlusA = new Addition(exprB, exprA);

        assertEquals(aPlusB.accept(evaluator), bPlusA.accept(evaluator), "Commutativity violated!");
    }

    @Test
    public void testDoubleNegation() {
        Expression expr = new Variable("z");
        Expression doppelteNegation = new Negation(new Negation(expr));

        assertEquals(expr.accept(evaluator), doppelteNegation.accept(evaluator), "Double negation violates!");
    }
}