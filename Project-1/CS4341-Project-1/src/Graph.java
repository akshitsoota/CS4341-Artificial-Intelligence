import java.util.*;

public class Graph {
    private static final boolean IS_GRAPH_UNDIRECTED_BY_DEFAULT = true;

    private final Map<String, GraphNode> nodeMapping;

    public Graph() {
        this.nodeMapping = new LinkedHashMap<>();
    }

    public final void addConnection(final String from, final String to, final double connectionWeight) {
        addConnection(from, to, connectionWeight, IS_GRAPH_UNDIRECTED_BY_DEFAULT);
    }

    public final void addConnection(final String from, final String to, final double connectionWeight, final boolean twoWayConnection) {
        final GraphNode fromNode;
        if (!nodeMapping.containsKey(from)) {
            fromNode = new GraphNode(from);
            nodeMapping.put(from, fromNode);
        } else {
            fromNode = nodeMapping.get(from);
        }

        final GraphNode toNode;
        if (!nodeMapping.containsKey(to)) {
            toNode = new GraphNode(to);
            nodeMapping.put(to, toNode);
        } else {
            toNode = nodeMapping.get(to);
        }

        fromNode.addOutgoingNode(toNode, connectionWeight);

        if (twoWayConnection) {
            toNode.addOutgoingNode(fromNode, connectionWeight);
        }
    }

    public final GraphNode getGraphNode(final String node) {
        if (!nodeMapping.containsKey(node)) {
            return null;
        }
        return nodeMapping.get(node);
    }

    public final void setHeuristicValue(final String nodeKey, final double heuristicValue) {
        final GraphNode node;
        if (nodeMapping.containsKey(nodeKey) && (node = nodeMapping.get(nodeKey)) != null) {
            node.setHeuristicValue(heuristicValue);
        }
    }

    public final List<GraphNode> getAllGraphNodes() {
        final List<GraphNode> nodes = new ArrayList<>();
        nodes.addAll(nodeMapping.values());

        return nodes;
    }

    /**
     * Determines if two nodes in the graph are <b>directly</b> connected
     */
    public final boolean isConnected(final String from, final String to) {
        return isConnected(from, to, IS_GRAPH_UNDIRECTED_BY_DEFAULT);
    }

    /**
     * Determines if two nodes in the graph are <b>directly</b> connected
     */
    public final boolean isConnected(final String from, final String to, final boolean twoWayConnection) {
        final GraphNode fromNode = nodeMapping.getOrDefault(from, null);
        final GraphNode toNode = nodeMapping.getOrDefault(to, null);

        if (fromNode == null && toNode == null) {
            return false;
        }

        final boolean fromConnectedWithToHuh = fromNode != null && fromNode.isConnectedTo(toNode);
        if (!twoWayConnection) {
            return fromConnectedWithToHuh;
        }

        final boolean toConnectedWithFromHuh = toNode != null && toNode.isConnectedTo(fromNode);
        return fromConnectedWithToHuh || toConnectedWithFromHuh;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        this.nodeMapping.values().forEach(graphNode -> {
            builder.append(graphNode).append("\n");
        });

        return builder.toString();
    }
}
