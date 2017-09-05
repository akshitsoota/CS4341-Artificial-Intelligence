import java.util.*;

public class ImplAStarSearch implements ISearchStrategy {


    @Override
    public boolean isInformed() {
        return true;
    }

    @Override
    public double valueToPrintWithPath(List<GraphNode> path) {
        return path.get(0).getHeuristicValue() + Utilities.getPathCost(path);
    }

    @Override
    public LinkedList<List<GraphNode>> addNodesToQueue(LinkedList<List<GraphNode>> queue, List<GraphNode> currentNodePath, List<GraphNode> childrenNodes) {
        LinkedList<List<GraphNode>> newQueue = new LinkedList<>();
        newQueue.addAll(queue);
        newQueue.addAll(Utilities.addChildrenToPath(currentNodePath, childrenNodes));
        newQueue.sort(ImplAStarSearch::getHeuristicAndCostComparator);

        // Need to remove multiple paths to same node, choose better path i.e. one that appears first in sorted queue
        Iterator<List<GraphNode>> queueIterator = newQueue.iterator();
        Set<GraphNode> visited = new HashSet<>();

        while (queueIterator.hasNext()) {
            List<GraphNode> path = queueIterator.next();
            if (visited.contains(path.get(0))) {
                queueIterator.remove();
            } else {
                visited.add(path.get(0));
            }
        }

        return newQueue;
    }

    /**
     * Gets the compare value of path1 and path2
     * @param path1 the first path
     * @param path2 the second path
     * @return the compare value of the two path by using heuristic and cost
     */
    private static int getHeuristicAndCostComparator(List<GraphNode> path1, List<GraphNode> path2) {
        final double heuristic1 = path1.get(0).getHeuristicValue();
        final double heuristic2 = path2.get(0).getHeuristicValue();

        final double path1Cost = Utilities.getPathCost(path1);
        final double path2Cost = Utilities.getPathCost(path2);

        final int compareValue = Double.compare(heuristic1 + path1Cost, heuristic2 + path2Cost);
        if (compareValue == 0) {
            // If we've a tie, do alphabetical sort
            return path1.get(0).getKey().compareTo(path2.get(0).getKey());
        }

        return compareValue;
    }

}
