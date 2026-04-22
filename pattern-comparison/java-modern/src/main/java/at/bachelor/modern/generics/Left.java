package at.bachelor.modern.generics;

public record Left<L, R>(L value) implements Either<L, R> {
}
