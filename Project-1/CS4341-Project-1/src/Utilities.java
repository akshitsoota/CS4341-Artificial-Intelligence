import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Utilities {
    public static final Comparator<List<GraphNode>> HEURISTIC_BASED_COMPARATOR = (path1, path2) -> {
        final double heuristic1 = path1.get(0).getHeuristicValue();
        final double heuristic2 = path2.get(0).getHeuristicValue();

        final int compareValue = Double.compare(heuristic1, heuristic2);
        if (compareValue == 0) {
            // If we've a tie, do alphabetical sort
            return path1.get(0).getKey().compareTo(path2.get(0).getKey());
        }

        return compareValue;
    };

    private Utilities() {

    }

    /**
     * Adds the child to the front of the currentNodePath and returns all possible combinations
     * @return All possible combinations of the children followed by the currentNodePath
     */
    public static List<List<GraphNode>> addChildrenToPath(final List<GraphNode> currentNodePath, final List<GraphNode> childrenNodes) {
        final List<List<GraphNode>> toReturn = new ArrayList<>();

        for (final GraphNode child : childrenNodes) {
            // If child is in the path, don't add (prevent cycles)
            if (currentNodePath.contains(child)) {
                continue;
            }

            final List<GraphNode> currentNodePathCopy = new ArrayList<>();
            currentNodePathCopy.add(child);
            currentNodePathCopy.addAll(currentNodePath);

            toReturn.add(currentNodePathCopy);
        }

        return toReturn;
    }

    /**
     * Gets the cost of the entire path
     * @param path a path or a list of connected nodes
     * @return the total cost of the path
     */
    public static double getPathCost(List<GraphNode> path) {

        if (path.size() < 2) {
            return 0.0;
        }

        double totalPathCost = 0.0;

        for (int i = 0; i < path.size() - 1; i++) {
            totalPathCost += path.get(i).getAllOutgoingNodes().get(path.get(i + 1));
        }

        return totalPathCost;
    }
}
