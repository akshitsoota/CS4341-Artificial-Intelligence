import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ImplIterativeDeepeningSearch implements ISearchStrategy {

    private static final int INITIAL_DEPTH = 0;

    private int depth;
    private LinkedList<List<GraphNode>> queueWithInitialNode;

    private ImplIterativeDeepeningSearch(LinkedList<List<GraphNode>> queueWithInitialNode) {
        this.queueWithInitialNode = queueWithInitialNode;
        this.depth = ImplIterativeDeepeningSearch.INITIAL_DEPTH;
    }

    static ImplIterativeDeepeningSearch getInstance(LinkedList<List<GraphNode>> queueWithInitialNode) {
        if (queueWithInitialNode == null || queueWithInitialNode.isEmpty()) {
            System.err.print("Error, invalid initial queue for Iterative deepening search");
            throw new IllegalStateException();
        }
        return new ImplIterativeDeepeningSearch(queueWithInitialNode);
    }

    @Override
    public boolean isInformed() {
        return false;
    }

    @Override
    public LinkedList<List<GraphNode>> addNodesToQueue(LinkedList<List<GraphNode>> queue, List<GraphNode> currentNodePath, List<GraphNode> childrenNodes) {
        if (currentNodePath == null || currentNodePath.isEmpty()) {
            return queue;
        }
        if (currentNodePath.size() -1 == this.depth) {
            if (queue.isEmpty()) {
                this.depth++;
                System.out.println();
                System.out.println("l = " + this.depth);
                return getQueueWithInitialNodeCopy();
            }
            return queue;
        }
        Collections.reverse(childrenNodes);
        List<List<GraphNode>> childrenPathList = Utilities.addChildrenToPath(currentNodePath, childrenNodes);
        childrenPathList.forEach((path) -> queue.add(0, path));
        return queue;
    }

    /**
     * Gets the queue with just initial node for next iteration
     * @return the queue with initial node
     */
    private LinkedList<List<GraphNode>> getQueueWithInitialNodeCopy() {
        List<GraphNode> newGraphNodeList = new ArrayList<>();
        newGraphNodeList.addAll(this.queueWithInitialNode.get(0));
        LinkedList<List<GraphNode>> newQueue = new LinkedList<>();
        newQueue.add(newGraphNodeList);
        return newQueue;
    }

}
