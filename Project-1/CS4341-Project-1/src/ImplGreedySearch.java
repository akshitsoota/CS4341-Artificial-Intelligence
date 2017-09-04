import java.util.*;

public class ImplGreedySearch implements ISearchStrategy {
    @Override
    public boolean isInformed() {
        return true;
    }

    @Override
    public LinkedList<List<GraphNode>> addNodesToQueue(final LinkedList<List<GraphNode>> queue, final List<GraphNode> currentNodePath, final List<GraphNode> childrenNodes) {
        final LinkedList<List<GraphNode>> toReturn = new LinkedList<>(queue);
        toReturn.addAll(Utilities.addChildrenToPath(currentNodePath, childrenNodes));
        toReturn.sort(Utilities.HEURISTIC_BASED_COMPARATOR);

        return toReturn;
    }
}
