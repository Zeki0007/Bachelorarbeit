package at.bachelor.modern.generics;

public sealed interface Either<L, R> permits Left, Right {
    static <L, R> Either<L, R> left(L value) { return new Left<>(value); }
    static <L, R> Either<L, R> right(R value) { return new Right<>(value); }
}
