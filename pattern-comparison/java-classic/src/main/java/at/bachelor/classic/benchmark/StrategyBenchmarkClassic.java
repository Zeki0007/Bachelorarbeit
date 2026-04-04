package at.bachelor.classic.benchmark;

import at.bachelor.classic.strategy.*;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(value = 1, jvmArgs = {"-Xmx4G"})
public class StrategyBenchmarkClassic {

    private List<ShoppingCart> carts;
    private DiscountStrategy strategy;

    @Setup(Level.Trial)
    public void setup() {
        carts = new ArrayList<>(10_000_000);
        strategy = new PercentageDiscount(0.20);

        for (int i = 0; i < 10_000_000; i++) {
            // Generates values from 0.0 to 1999.0
            double value = (i * 137) % 2000;
            carts.add(new ShoppingCart(value, strategy));
        }
    }

    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Benchmark
    public double testStrictPipeline() {
        List<Double> discounted = new ArrayList<>(carts.size());
        for (ShoppingCart cart : carts) {
            discounted.add(strategy.applyDiscount(cart.calculateFinalPrice()));
        }

        List<Double> filtered = new ArrayList<>();
        for (Double price : discounted) {
            if (price > 1000.0) {
                filtered.add(price);
            }
        }

        double sum = 0;
        for (int i = 0; i < Math.min(5, filtered.size()); i++) {
            sum += filtered.get(i);
        }
        return sum;
    }

    @Benchmark
    public double testImperativeLoop() {
        double sum = 0;
        int count = 0;

        for (ShoppingCart cart : carts) {
            double price = strategy.applyDiscount(cart.calculateFinalPrice());
            if (price > 1000.0) {
                sum += price;
                count++;
                if (count == 5) {
                    break;
                }
            }
        }
        return sum;
    }

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.runner.options.Options opt = new org.openjdk.jmh.runner.options.OptionsBuilder()
            .include(StrategyBenchmarkClassic.class.getSimpleName())
            .resultFormat(org.openjdk.jmh.results.format.ResultFormatType.JSON)
            .result("results-" + StrategyBenchmarkClassic.class.getSimpleName() + ".json")
            .build();
        new org.openjdk.jmh.runner.Runner(opt).run();
    }
}
