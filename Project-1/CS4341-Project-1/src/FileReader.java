import java.util.Scanner;

public class FileReader {

    private Scanner scanner;

    public FileReader() {
	
    }

    public void convertFileToGraph(Graph graph, String fileName) {
	try {
	    File graphFile = new File(fileName);
	    Scanner graphScanner = new Scanner(graphFile);
	    processFile(graph, graphScanner);
	} catch(IOException e) {
	    e.printStackTrace();
	} catch(Exception e) {
	    e.printStackTrace();
	}
    }

    private void processFile(Graph graph, Scanner graphScanner) {
	String graphLine = "";
	while(graphScanner.hasNext() && !(graphLine = graphScanner.nextLine()).beginsWith("###")) {
	    String[] graphEdgeComponents = graphScanner.nextLine().split(" ");
	    if(graphEdgeComponents.length == 3) {
		String fromNode = graphEdgeComponents[0];
		String toNode = graphEdgeComponents[1];
		double connectionWeight = Double.parseDouble(graphEdgeComponents[2]);
	    }
	    graph.addConnection(fromNode, toNode, connectionWeight, true);
	}

	graphScanner.nextLine();
	while(graphScanner.hasNext()) {
	    String[] graphEdgeComponents = graphScanner.nextLine().split(" ");
	    if(graphEdgeComponents.length == 2) {
		String node = graphEdgeComponents[0];
		double heuristicVal = Double.parseDouble(graphEdgeComponents[1]);
	    }
	    graph.setHeuristicValue(node, heuristicVal);
	}
    }

}
