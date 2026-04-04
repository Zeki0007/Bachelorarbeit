package at.bachelor.modern.visitor;

public sealed interface Expression permits Literal,Variable,Negation,Addition{
}
