public class Main {
    public static void main(final String[] args) {
        final FileReader graphReader = new FileReader();
        final Graph fileGraph = new Graph();

        // FIXME: Accept args[0]
        graphReader.convertFileToGraph(fileGraph, "graph.txt");

        System.out.println("Depth First Search:");
        GeneralSearch.general_Search(fileGraph, new ImplDepthFirstSearch());
        System.out.println("");

        System.out.println("Depth Limited Search (depth-limit = DEFAULT = 2):");
        GeneralSearch.general_Search(fileGraph, new ImplDepthLimitedSearch());
        System.out.println("");

        System.out.println("Iterative Deepening Search:");
        System.out.println("l = 0");
        GeneralSearch.general_Search(fileGraph, ImplIterativeDeepeningSearch.getInstance(GeneralSearch.makeQueueWithInitialState(fileGraph)));
        System.out.println("");

        System.out.println("Greedy Search:");
        GeneralSearch.general_Search(fileGraph, new ImplGreedySearch());
        System.out.println("");

        System.out.println("A* Search:");
        GeneralSearch.general_Search(fileGraph, new ImplAStarSearch());
        System.out.println("");

        System.out.println("Beam Search (w = DEFAULT = 3):");
        GeneralSearch.general_Search(fileGraph, new ImplBeamSearch());
        System.out.println("");
    }
}
