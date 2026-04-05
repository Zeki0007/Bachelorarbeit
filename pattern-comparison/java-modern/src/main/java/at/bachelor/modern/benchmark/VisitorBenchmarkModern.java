package at.bachelor.modern.benchmark;

import at.bachelor.modern.visitor.*;
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
public class VisitorBenchmarkModern {

    @Param({"13", "16", "19", "23"})
    public int depth;

    private Expression simpleTree;
    private Expression complexTree;
    private Map<String, Integer> environment;

    @Setup(Level.Trial)
    public void setup() {
        simpleTree = createSimpleTree(depth);
        complexTree = createComplexTree(depth, 1);

        environment = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            environment.put("x" + i, i);
        }
    }

    private Expression createSimpleTree(int currentDepth) {
        if (currentDepth == 0) return new Literal(2);
        return new Addition(createSimpleTree(currentDepth - 1), createSimpleTree(currentDepth - 1));
    }

    private Expression createComplexTree(int currentDepth, int id) {
        if (currentDepth == 0) {
            if (id % 3 == 0) {
                return new Variable("x" + (id % 10));
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
    public int testModernSimpleTree() {
        return Evaluation.evaluate(simpleTree, environment);
    }

    @Benchmark
    public int testModernComplexTree() {
        return Evaluation.evaluate(complexTree, environment);
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.runner.options.Options opt = new org.openjdk.jmh.runner.options.OptionsBuilder()
            .include(VisitorBenchmarkModern.class.getSimpleName())
            .resultFormat(org.openjdk.jmh.results.format.ResultFormatType.JSON)
            .result("results-" + VisitorBenchmarkModern.class.getSimpleName() + ".json")
            .build();
        new org.openjdk.jmh.runner.Runner(opt).run();
    }
}