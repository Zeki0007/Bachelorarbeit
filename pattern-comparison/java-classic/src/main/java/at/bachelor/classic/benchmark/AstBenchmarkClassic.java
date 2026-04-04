package at.bachelor.classic.benchmark;

import at.bachelor.classic.visitor.*;
import org.openjdk.jmh.annotations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(value = 1, jvmArgs = {"-Xmx6G"})
public class AstBenchmarkClassic {

    @Param({"13", "16", "19", "23"})
    public int depth;

    private Expression simpleTree;
    private Expression complexTree;
    private EvaluationVisitor evaluator;
    private Map<String, Integer> environment;

    @Setup(Level.Trial)
    public void setup() {
        simpleTree = createSimpleTree(depth);
        complexTree = createComplexTree(depth, 1);

        environment = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            environment.put("x" + i, i);
        }

        evaluator = new EvaluationVisitor(environment);
    }

    // Generator 1: Only addition and literals
    private Expression createSimpleTree(int currentDepth) {
        if (currentDepth == 0) return new Literal(2);
        return new Addition(createSimpleTree(currentDepth - 1), createSimpleTree(currentDepth - 1));
    }

    // Generator 2: Addition, negation, variables and literals
    private Expression createComplexTree(int currentDepth, int id) {
        if (currentDepth == 0) {
            if (id % 3 == 0) {
                return new Variable("x" + (id % 10)); // Variables x0 to x9
            } else {
                return new Literal(id % 100);
            }
        }

        Expression left = createComplexTree(currentDepth - 1, id * 2);
        Expression right = createComplexTree(currentDepth - 1, id * 2 + 1);

        if (currentDepth % 4 == 0) {
            return new Addition(new Negation(left), right);
        }
        return new Addition(left, right);
    }

    @Benchmark
    public int testClassicSimpleTree() {
        return simpleTree.accept(evaluator);
    }

    @Benchmark
    public int testClassicComplexTree() {
        return complexTree.accept(evaluator);
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.runner.options.Options opt = new org.openjdk.jmh.runner.options.OptionsBuilder()
            .include(AstBenchmarkClassic.class.getSimpleName())
            .resultFormat(org.openjdk.jmh.results.format.ResultFormatType.JSON)
            .result("results-" + AstBenchmarkClassic.class.getSimpleName() + ".json")
            .build();
        new org.openjdk.jmh.runner.Runner(opt).run();
    }
}