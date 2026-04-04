package at.bachelor.classic.visitor;

import java.util.Map;

public class EvaluationVisitor implements Visitor<Integer>{
    private final Map<String, Integer> environment;

    public EvaluationVisitor(Map<String, Integer> environment) {
        this.environment = environment;
    }

    @Override
    public Integer visitLiteral(Literal literal) {
        return literal.getValue();
    }

    @Override
    public Integer visitVariable(Variable variable) {
        String name = variable.getName();
        if (!environment.containsKey(name)) {
            throw new IllegalArgumentException("Unknown variable: " + name);
        }
        return environment.get(name);
    }

    @Override
    public Integer visitNegation(Negation negation) {
        Integer innerValue = negation.getExpression().accept(this);
        return -innerValue;
    }

    @Override
    public Integer visitAddition(Addition addition) {
        Integer leftValue = addition.getLeft().accept(this);
        Integer rightValue = addition.getRight().accept(this);
        return leftValue + rightValue;
    }
}
