package at.bachelor.classic.visitor;

public class FormatVisitor implements Visitor<String>{
    @Override
    public String visitLiteral(Literal literal) {
        return Integer.toString(literal.getValue());
    }

    @Override
    public String visitVariable(Variable variable) {
        return variable.getName();
    }

    @Override
    public String visitNegation(Negation negation) {
        String innerValue = negation.getExpression().accept(this);
        return "(-" + innerValue+")";
    }

    @Override
    public String visitAddition(Addition addition) {
        String leftValue = addition.getLeft().accept(this);
        String rightValue = addition.getRight().accept(this);
        return "("+ leftValue + "+" + rightValue+ ")";
    }


}
