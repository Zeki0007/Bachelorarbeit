package at.bachelor.classic.visitor;

public interface Visitor<T> {
    T visitLiteral(Literal literal);
    T visitVariable(Variable variable);
    T visitNegation(Negation negation);
    T visitAddition(Addition addition);
}
