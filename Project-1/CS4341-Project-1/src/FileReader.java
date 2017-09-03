import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FileReader {
	
	public FileReader() {}

	/**
	 * Stores the graph information from the provided text file URL to the provided Graph object
	 * @param graph The Graph object to store nodes and edges in
	 * @param fileName The relative path (from the project directory) to the file containing the Graph object
	 */
	public void convertFileToGraph(Graph graph, String fileName) {
		try {
			File graphFile = new File(fileName);
			Scanner graphScanner = new Scanner(graphFile);
			processFile(graph, graphScanner);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Helper function to iterate through the provided text file and parse data
	 * @param graph Graph object to store the nodes and edges to
	 * @param graphScanner Scanner object to read the file
	 */
	private void processFile(Graph graph, Scanner graphScanner) {
		String graphLine = "";
		
		// Scan for edge information until we reach the file divider (#####)
		while (graphScanner.hasNext() && !(graphLine = graphScanner.nextLine()).startsWith("###")) {
			String[] graphEdgeComponents = graphLine.split(" ");
			if (graphEdgeComponents.length == 3) {
				String fromNode = graphEdgeComponents[0];
				String toNode = graphEdgeComponents[1];
				double connectionWeight = Double.parseDouble(graphEdgeComponents[2]);
				
				System.out.printf("Adding connection from Node %s to Node %s with weight %f\n", fromNode, toNode, connectionWeight);
				
				graph.addConnection(fromNode, toNode, connectionWeight, true);
			}
		}
		
		// Next, scan for node heuristic values until we reach EOF
		while (graphScanner.hasNext() && !(graphLine = graphScanner.nextLine()).equals("")) {
			String[] graphEdgeComponents = graphLine.split(" ");
			if (graphEdgeComponents.length == 2) {
				String node = graphEdgeComponents[0];
				double heuristicVal = Double.parseDouble(graphEdgeComponents[1]);
				
				System.out.printf("Adding heuristic value to Node %s with value %f\n", node, heuristicVal);
				
				graph.setHeuristicValue(node, heuristicVal);
			}
		}
	}

}
