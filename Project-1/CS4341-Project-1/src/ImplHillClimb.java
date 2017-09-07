import java.util.LinkedList;
import java.util.List;

public class ImplHillClimb implements ISearchStrategy {

	@Override
	public boolean isInformed() {
		return true;
	}

	@Override
	public LinkedList<List<GraphNode>> addNodesToQueue(LinkedList<List<GraphNode>> queue,
			List<GraphNode> currentNodePath, List<GraphNode> childrenNodes) {
		if (currentNodePath == null || currentNodePath.isEmpty()) {
            return queue;
        }
		
		List<List<GraphNode>> a = Utilities.addChildrenToPath(currentNodePath, childrenNodes);
		a.sort(Utilities.HEURISTIC_BASED_COMPARATOR);
		LinkedList<List<GraphNode>> children = new LinkedList<List<GraphNode>>();
		children.addAll(a);
		children.addAll(queue);
				
		return children;
	}

}
