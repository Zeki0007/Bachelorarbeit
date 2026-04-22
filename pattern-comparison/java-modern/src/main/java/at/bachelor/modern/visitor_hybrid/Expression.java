package at.bachelor.modern.visitor_hybrid;

public sealed interface Expression permits Literal,Variable, Operation {
}
