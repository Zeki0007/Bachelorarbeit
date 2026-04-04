package at.bachelor.modern.state;

public sealed interface DocumentState permits Draft, Review, Published{
}
