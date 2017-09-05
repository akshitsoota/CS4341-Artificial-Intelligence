import java.util.LinkedList;
import java.util.List;

public interface ISearchStrategy {

    /**
     * Checks if the search method is informed
     * @return true if search is informed, false otherwise
     */
    boolean isInformed();


    /**
     * By default, the heuristic value should be printed except for searches like A star
     * @param path the path to print the information of
     * @return the value to print
     */
    default double valueToPrintWithPath(List<GraphNode> path) {
        return path.get(0).getHeuristicValue();
    }

    /**
     * adds the childrenNodes by appending it to the currentNodePath in the queue using the strategy defined by
     * the search method <br />
     * Ex (dfs) <br />
     * queue - [[A,S],[B,S]] <br />
     * currentNodePath - [C,S] <br />
     * childrenNodes - [D,E,F] <br />
     *<br />
     * with dfs strategy, should return queue - [[D,C,S],[E,C,S],[F,C,S],[A,S],[B,S]] <br />
     *<br />
     * @param queue - The current queue after extracting out the current node path being explored
     * @param currentNodePath - The ongoing path of the current node being explored including the node itself
     * @param childrenNodes - Children of the current node sorted alphabetically
     * @return - The updated queue after adding all the children to the path
     */
    LinkedList<List<GraphNode>> addNodesToQueue(LinkedList<List<GraphNode>> queue,
                                                List<GraphNode> currentNodePath, List<GraphNode> childrenNodes);

}
