import java.io.File;
import java.io.IOException;
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
		} catch (IOException e) {
			System.out.println("Please ensure the provided file is valid and not locked");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processFile(Graph graph, Scanner graphScanner) {
		String graphLine = "";
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

		while (graphScanner.hasNext()) {
			String[] graphEdgeComponents = graphScanner.nextLine().split(" ");
			if (graphEdgeComponents.length == 2) {
				String node = graphEdgeComponents[0];
				double heuristicVal = Double.parseDouble(graphEdgeComponents[1]);
				
				System.out.printf("Adding heuristic value to Node %s with value %f\n", node, heuristicVal);
				
				graph.setHeuristicValue(node, heuristicVal);
			}
		}
	}

}
