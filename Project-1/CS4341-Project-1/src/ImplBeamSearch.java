import java.util.LinkedList;
import java.util.List;

public class ImplBeamSearch implements ISearchStrategy {
    static final int DEFAULT_BEAMING_VALUE = 2; // w

    private final int beamingValue;

    public ImplBeamSearch() {
        this(DEFAULT_BEAMING_VALUE);
    }

    public ImplBeamSearch(final int beamingValue) {
        this.beamingValue = beamingValue;
    }

    @Override
    public boolean isInformed() {
        return true;
    }

    @Override
    public LinkedList<List<GraphNode>> addNodesToQueue(final LinkedList<List<GraphNode>> queue, final List<GraphNode> currentNodePath, final List<GraphNode> childrenNodes) {
        final LinkedList<List<GraphNode>> toReturn = new LinkedList<>(queue);
        toReturn.addAll(Utilities.addChildrenToPath(currentNodePath, childrenNodes));

        // If we've reached the end of a level and we've enough elements to beam off, beam them off the queue
        if (hasLevelBeenFullyExpanded(toReturn) && toReturn.size() > beamingValue) {
            // Drop the max heuristic values till we have the exact size
            while (toReturn.size() > beamingValue) {
                int maxHeuristicAtIndex = 0;
                double maxHeuristicValue = toReturn.get(0).get(0).getHeuristicValue();

                for (int idx = 1; idx < toReturn.size(); idx++) {
                    if (toReturn.get(idx).get(0).getHeuristicValue() > maxHeuristicValue) {
                        maxHeuristicValue = toReturn.get(idx).get(0).getHeuristicValue();
                        maxHeuristicAtIndex = idx;
                    }
                }

                toReturn.remove(maxHeuristicAtIndex);
            }
        }

        return toReturn;
    }

    private boolean hasLevelBeenFullyExpanded(final LinkedList<List<GraphNode>> queue) {
        if (queue.size() == 0) {
            return false;
        }

        int maxPathLength = queue.get(0).size();
        int minPathLength = queue.get(0).size();

        for(final List<GraphNode> path : queue) {
            final int pathSize = path.size();

            if (pathSize > maxPathLength) {
                maxPathLength = pathSize;
            }

            if (minPathLength > pathSize) {
                minPathLength = pathSize;
            }
        }

        // To reach the end of a level, all paths must have the same length
        return minPathLength == maxPathLength;
    }
}
