import java.util.*;

public class GeneralSearch {

    private static final String INITIAL_STATE = "S";
    private static final String GOAL_STATE = "G";

    /**
     * General_Search algorithm to be used by all search methods
     * @param graph the problem graph to find a path
     * @param searchStrategy the Search method to be used to find the path
     * @return the path from the initial state to the goal state found
     */
    public static List<GraphNode> general_Search(Graph graph, ISearchStrategy searchStrategy) {
        LinkedList<List<GraphNode>> queue = GeneralSearch.makeQueueWithInitialState(graph);
        while (!queue.isEmpty()) {
            GeneralSearch.printQueueSnapshot(queue, searchStrategy.isInformed());
            List<GraphNode> front = queue.removeFirst();
            if (GeneralSearch.isSolution(graph, front)) {
                System.out.println("goal reached!");
                return front;
            }
            List<GraphNode> openedNodes = GeneralSearch.expand(front);
            queue = searchStrategy.addNodesToQueue(queue, front, openedNodes);
        }
        return null;
    }

    /**
     * Creates a graph with an initial state path (ex - [[S]])
     * @param graph the graph to create the queue
     * @return the created queue with the initial state
     */
    private static LinkedList<List<GraphNode>> makeQueueWithInitialState(Graph graph) {
        GraphNode initialState = graph.getGraphNode(INITIAL_STATE);
        if (initialState == null) {
            System.err.println("Cannot find the Initial State Node");
            throw new IllegalStateException();
        }
        List<GraphNode> initialStateList = new ArrayList<>();
        initialStateList.add(initialState);
        LinkedList<List<GraphNode>> queue = new LinkedList<>();
        queue.add(initialStateList);
        return queue;
    }

    /**
     * Checks if path has reached the goal i.e. if the first GraphNode in path is the goal Node.
     * @param graph the graph or the problem
     * @param path the path to check for goal
     * @return whether or not the goal is reached
     */
    private static boolean isSolution(Graph graph, List<GraphNode> path) {
        GraphNode goalState = graph.getGraphNode(GOAL_STATE);
        return path.get(0).equals(goalState);
    }

    /**
     * Expands the node to fetch its children/neighbors
     * @param path the path to the node to be expanded
     * @return the list of the discovered children/neighbors
     */
    private static List<GraphNode> expand(List<GraphNode> path) {
        GraphNode lastNodeInPath = path.get(0);

        final List<GraphNode> paths = new ArrayList<>(lastNodeInPath.getAllOutgoingNodes().keySet());
        paths.sort(Comparator.comparing(GraphNode::getKey));

        return paths;
    }

    /**
     * Prints a snapshot of the queue
     * @param queue the queue to print the snapshot
     * @param showHeuristic whether to show the heuristics in the snapshot
     */
    private static void printQueueSnapshot(LinkedList<List<GraphNode>> queue, boolean showHeuristic) {
        if (queue.isEmpty()) {
            System.out.print("\t\t\t[]\n");
            return;
        }

        System.out.print("\t" + queue.get(0).get(0).getKey() + "\t");
        System.out.print("[");

        for (List<GraphNode> path : queue) {
            StringBuilder pathBuilder = new StringBuilder();
            String prefix = "";

            if (showHeuristic) {
                pathBuilder.append(String.format("%.1f", path.get(0).getHeuristicValue()));
            }

            pathBuilder.append("<");
            for (GraphNode node : path) {
                pathBuilder.append(prefix);
                prefix = ",";
                pathBuilder.append(node.getKey());
            }

            String pathToPrint = pathBuilder.toString();
            pathToPrint = pathToPrint + "> ";
            System.out.print(pathToPrint);
        }

        System.out.print("]");
        System.out.println("\t");
    }

}
