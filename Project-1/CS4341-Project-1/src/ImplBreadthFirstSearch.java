import java.util.LinkedList;
import java.util.List;

public class ImplBreadthFirstSearch implements ISearchStrategy {

	@Override
	public boolean isInformed() {
		return false;
	}

	@Override
	public LinkedList<List<GraphNode>> addNodesToQueue(LinkedList<List<GraphNode>> queue,
			List<GraphNode> currentNodePath, List<GraphNode> childrenNodes) {
		if (currentNodePath == null || currentNodePath.isEmpty()) {
            return queue;
        }
		
		List<List<GraphNode>> children = Utilities.addChildrenToPath(currentNodePath, childrenNodes);
		children.forEach((path) -> queue.add(path));
		
		return queue;
	}

}
