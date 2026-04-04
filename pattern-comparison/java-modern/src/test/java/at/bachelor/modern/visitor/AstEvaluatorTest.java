package at.bachelor.modern.visitor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AstEvaluatorTest {

    private Map<String, Integer> environment;

    @BeforeEach
    public void setup() {
        environment = Map.of("x", 10, "y", 20, "z", 30);
    }

    @Test
    public void testIdentity() {
        Expression expr = new Variable("x");
        Expression additionMitNull = new Addition(expr, new Literal(0));

        assertEquals(Evaluation.evaluate(expr, environment),
            Evaluation.evaluate(additionMitNull,environment),
            "Identity violated!");
    }

    @Test
    public void testCommutativity() {
        Expression exprA = new Literal(15);
        Expression exprB = new Variable("y");

        assertEquals(Evaluation.evaluate(new Addition(exprA, exprB), environment),
            Evaluation.evaluate(new Addition(exprB, exprA),environment),
            "Commutativity violated!\n");
    }

    @Test
    public void testDoubleNegation() {
        Expression expr = new Variable("z");
        Expression doppelteNegation = new Negation(new Negation(expr));

        assertEquals(Evaluation.evaluate(expr, environment),
            Evaluation.evaluate(doppelteNegation, environment),
            "Double negation violates!");
    }
}