package at.bachelor.modern.generics;

public record Right<L, R>(R value) implements Either<L, R> {
}
