package at.bachelor.classic.visitor;

public class MetricsVisitor implements Visitor<Integer[]>{
    @Override
    public Integer[] visitLiteral(Literal literal) {
        return new Integer[]{1,1};
    }

    @Override
    public Integer[] visitVariable(Variable variable) {
        return new Integer[]{1,1};
    }

    @Override
    public Integer[] visitNegation(Negation negation) {
        Integer[] innerValue = negation.getExpression().accept(this);
        innerValue[0] += 1;
        innerValue[1] += 1;
        return innerValue;
    }

    @Override
    public Integer[] visitAddition(Addition addition) {
        Integer[] leftValue = addition.getLeft().accept(this);
        Integer[] rightValue = addition.getRight().accept(this);
        leftValue[0] += rightValue[0]+1;
        int depth = Math.max(leftValue[1], rightValue[1])+1;
        return new Integer[]{leftValue[0], depth};
    }
}
