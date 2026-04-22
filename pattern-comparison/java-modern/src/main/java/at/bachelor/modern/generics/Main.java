package at.bachelor.modern.generics;

public class Main {
    public static void main(String[] args) {
        Either<String, Integer> result = safeDivide(10, 0);

        String message = switch (result) {
            case Left<String, Integer>(String error) -> "Error: " + error;
            case Right<String, Integer>(Integer val) -> "Result: " + val;
        };
        System.out.println(message);
    }

    static Either<String, Integer> safeDivide(int x, int y) {
        if (y == 0)
            return Either.left("Division by zero!");
        return Either.right(x / y);
    }
}