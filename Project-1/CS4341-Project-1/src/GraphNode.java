import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class GraphNode {
    private static final double DEFAULT_HEURISTIC_VALUE = 0.0;

    private final String key;
    private double heuristicValue;
    private final Map<GraphNode, Double> outgoingNodes;

    public GraphNode(final String key) {
        this(key, DEFAULT_HEURISTIC_VALUE, null);
    }

    public GraphNode(final String key, final double heuristicValue) {
        this(key, heuristicValue, null);
    }

    public GraphNode(final String key, final double heuristicValue, final Map<GraphNode, Double> outgoingNodes) {
        this.key = key;
        this.heuristicValue = heuristicValue;

        this.outgoingNodes = new LinkedHashMap<>();
        if (outgoingNodes != null && outgoingNodes.size() != 0) {
            this.outgoingNodes.putAll(outgoingNodes);
        }
    }

    public final String getKey() {
        return key;
    }

    public void setHeuristicValue(final double heuristicValue) {
        this.heuristicValue = heuristicValue;
    }

    public final double getHeuristicValue() {
        return heuristicValue;
    }

    public final void addOutgoingNode(final GraphNode to, final double connectionWeight) {
        outgoingNodes.put(to, connectionWeight);
    }

    public final Map<GraphNode, Double> getAllOutgoingNodes() {
        final Map<GraphNode, Double> toReturn = new HashMap<>();
        toReturn.putAll(outgoingNodes);

        return toReturn;
    }

    public final boolean isConnectedTo(final GraphNode to) {
        return to != null && (this == to || outgoingNodes.containsKey(to));
    }

    @Override
    public int hashCode() {
        // Ideally heuristicValue and outgoingNodes (circular dependency) should be included but....
        return 31 + 37 * key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof GraphNode)) {
            return false;
        }

        final GraphNode other = GraphNode.class.cast(obj);
        if (!this.key.equals(other.key)) {
            return false;
        }

        if (this.heuristicValue != other.heuristicValue) {
            return false;
        }

        if (!this.outgoingNodes.equals(other.outgoingNodes)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append(key).append(" (h = ").append(heuristicValue).append(") connects to ");
        for (final Iterator<Map.Entry<GraphNode, Double>> iterator = outgoingNodes.entrySet().iterator(); iterator.hasNext(); ) {
            final Map.Entry<GraphNode, Double> entry = iterator.next();
            builder.append(entry.getKey().getKey()).append("->").append(entry.getValue());

            if (iterator.hasNext()) {
                builder.append("; ");
            }
        }

        return builder.toString();
    }
}
