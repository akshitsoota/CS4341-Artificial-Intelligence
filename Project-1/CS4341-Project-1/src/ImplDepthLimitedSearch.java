import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ImplDepthLimitedSearch implements ISearchStrategy {
    private static final int DEFAULT_DEPTH_LIMIT = 2;

    private final int depthLimit;

    public ImplDepthLimitedSearch() {
        this(DEFAULT_DEPTH_LIMIT);
    }

    public ImplDepthLimitedSearch(final int depthLimit) {
        this.depthLimit = depthLimit;
    }

    @Override
    public boolean isInformed() {
        return false;
    }

    @Override
    public final LinkedList<List<GraphNode>> addNodesToQueue(final LinkedList<List<GraphNode>> queue, final List<GraphNode> currentNodePath, final List<GraphNode> childrenNodes) {
        final LinkedList<List<GraphNode>> toReturn = new LinkedList<>(queue);

        if (currentNodePath.size() == depthLimit + 1) {
            // We've reached out depth limit
            return toReturn;
        }

        // For each child, add the child to the front of the currentNodePath and add to the front of the queue
        final List<List<GraphNode>> toAddToFrontOfQueue = new ArrayList<>(Utilities.addChildrenToPath(currentNodePath, childrenNodes));

        Collections.reverse(toAddToFrontOfQueue);

        for (final List<GraphNode> toAdd : toAddToFrontOfQueue) {
            toReturn.addFirst(toAdd);
        }

        return toReturn;
    }
}
