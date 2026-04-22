package at.bachelor.modern.visitor_hybrid;

public class Metrics {
    public record MetricResult(int totalNodes, int maxDepth) {
        @Override
        public String toString() {
            return "totalNodes=" + totalNodes + ", maxDepth=" + maxDepth;
        }
    }

    public static MetricResult getMetrics(Expression expression) {
        return switch (expression) {
            case Literal(int value) -> new MetricResult(1, 1);
            case Variable(String name) -> new MetricResult(1, 1);
            case Negation(Expression innerValue) -> {
                MetricResult innerMetrics = getMetrics(innerValue);
                yield new MetricResult(innerMetrics.totalNodes() + 1, innerMetrics.maxDepth() + 1);
            }
            case Addition(Expression left, Expression right) -> {
                MetricResult leftMetrics = getMetrics(left);
                MetricResult rightMetrics = getMetrics(right);

                int depth = Math.max(leftMetrics.maxDepth(), rightMetrics.maxDepth())+1;
                yield new MetricResult(leftMetrics.totalNodes() + rightMetrics.totalNodes()+1,depth);
            }
            case Operation unknown -> throw new IllegalArgumentException("Unknown operation!");
        };
    }
}
