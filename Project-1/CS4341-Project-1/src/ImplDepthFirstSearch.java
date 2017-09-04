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
        List<List<GraphNode>> childrenPathList = Utilities.addChildrenToPath(currentNodePath, childrenNodes);
        childrenPathList.forEach((path) -> queue.add(0, path));
        return queue;
    }
}
