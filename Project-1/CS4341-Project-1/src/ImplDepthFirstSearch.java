import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ImplDepthFirstSearch implements ISearchStrategy {

    @Override
    public boolean isInformed() {
        return false;
    }

    @Override
    public LinkedList<List<GraphNode>> addNodesToQueue(LinkedList<List<GraphNode>> queue, List<GraphNode> currentNodePath, List<GraphNode> childrenNodes) {
        if (currentNodePath == null || currentNodePath.isEmpty()) {
            return queue;
        }
        Collections.reverse(childrenNodes);
        for (GraphNode child : childrenNodes) {
            if (currentNodePath.contains(child)) {
                continue;
            }
            List<GraphNode> childNodePath = new ArrayList<>();
            childNodePath.addAll(currentNodePath);
            childNodePath.add(0, child);
            queue.addFirst(childNodePath);
        }
        return queue;
    }
}
