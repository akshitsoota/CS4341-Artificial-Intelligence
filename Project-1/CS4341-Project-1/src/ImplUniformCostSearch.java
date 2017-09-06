import java.util.LinkedList;
import java.util.List;

public class ImplUniformCostSearch implements ISearchStrategy {

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
		
		LinkedList<List<GraphNode>> children = new LinkedList<>(queue);
		children.addAll(Utilities.addChildrenToPath(currentNodePath, childrenNodes));
		children.sort(Utilities.PATH_BASED_COMPARATOR);
		
		return children;
	}
	
	
	@Override
	public double valueToPrintWithPath(List<GraphNode> path) {
		return Utilities.getPathCost(path);
	}


}
