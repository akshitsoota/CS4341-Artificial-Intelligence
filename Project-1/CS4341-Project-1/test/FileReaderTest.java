import static org.junit.Assert.*;

import org.junit.Test;

public class FileReaderTest {
	 
	private Graph graph;
	private FileReader reader;
	
	public void init() {
		reader = new FileReader();
		graph = new Graph();
	}
	
	public void testFirstSample() {
		init();
		reader.convertFileToGraph(graph, "graph.txt");
		assertTrue(graph.isConnected("S", "A"));
		assertTrue(graph.isConnected("A", "S"));
		assertTrue(graph.isConnected("S", "D"));
		assertTrue(graph.isConnected("D", "S"));
		assertTrue(graph.isConnected("A", "B"));
		assertTrue(graph.isConnected("B", "A"));
		assertTrue(graph.isConnected("B", "C"));
		assertTrue(graph.isConnected("C", "B"));
		assertTrue(graph.isConnected("A", "D"));
		assertTrue(graph.isConnected("D", "A"));
		assertTrue(graph.isConnected("B", "E"));
		assertTrue(graph.isConnected("E", "B"));
		assertTrue(graph.isConnected("D", "E"));
		assertTrue(graph.isConnected("E", "D"));
		assertTrue(graph.isConnected("F", "E"));
		assertTrue(graph.isConnected("E", "F"));
		assertTrue(graph.isConnected("G", "F"));
		assertTrue(graph.isConnected("F", "G"));
		
		assertEquals(graph.getGraphNode("S").getHeuristicValue(), 11.0d, 0.1d);
		assertEquals(graph.getGraphNode("A").getHeuristicValue(), 10.4d, 0.1d);
		assertEquals(graph.getGraphNode("D").getHeuristicValue(), 8.9d, 0.1d);
		assertEquals(graph.getGraphNode("B").getHeuristicValue(), 6.7d, 0.1d);
		assertEquals(graph.getGraphNode("E").getHeuristicValue(), 6.9d, 0.1d);
		assertEquals(graph.getGraphNode("C").getHeuristicValue(), 4.0d, 0.1d);
		assertEquals(graph.getGraphNode("F").getHeuristicValue(), 3.0d, 0.1d);
	}

	@Test
	public void testSecondSample() {
		init();
		FileReader reader = new FileReader();
		Graph graph = new Graph();
		reader.convertFileToGraph(graph, "graph2.txt");
		assertTrue(graph.isConnected("S", "M"));
		assertTrue(graph.isConnected("M", "S"));
		assertTrue(graph.isConnected("S", "A"));
		assertTrue(graph.isConnected("A", "S"));
		assertTrue(graph.isConnected("M", "G"));
		assertTrue(graph.isConnected("G", "M"));
		assertTrue(graph.isConnected("A", "I"));
		assertTrue(graph.isConnected("I", "A"));
		assertTrue(graph.isConnected("A", "C"));
		assertTrue(graph.isConnected("C", "A"));
		assertTrue(graph.isConnected("A", "B"));
		assertTrue(graph.isConnected("B", "A"));
		assertTrue(graph.isConnected("C", "E"));
		assertTrue(graph.isConnected("E", "C"));
		assertTrue(graph.isConnected("C", "D"));
		assertTrue(graph.isConnected("D", "C"));
		assertTrue(graph.isConnected("I", "J"));
		assertTrue(graph.isConnected("J", "I"));
		assertTrue(graph.isConnected("J", "K"));
		assertTrue(graph.isConnected("K", "J"));
		assertTrue(graph.isConnected("J", "L"));
		assertTrue(graph.isConnected("L", "J"));
		assertTrue(graph.isConnected("K", "L"));
		assertTrue(graph.isConnected("L", "K"));
		assertTrue(graph.isConnected("L", "M"));
		assertTrue(graph.isConnected("M", "L"));
		
		assertEquals(graph.getGraphNode("S").getHeuristicValue(), 22.0d, 0.1d);
		assertEquals(graph.getGraphNode("M").getHeuristicValue(), 14.0d, 0.1d);
		assertEquals(graph.getGraphNode("I").getHeuristicValue(), 10.0d, 0.1d);
		assertEquals(graph.getGraphNode("J").getHeuristicValue(), 8.0d, 0.1d);
		assertEquals(graph.getGraphNode("K").getHeuristicValue(), 6.0d, 0.1d);
		assertEquals(graph.getGraphNode("L").getHeuristicValue(), 4.0d, 0.1d);
		assertEquals(graph.getGraphNode("E").getHeuristicValue(), 18.0d, 0.1d);
		assertEquals(graph.getGraphNode("C").getHeuristicValue(), 16.0d, 0.1d);
		assertEquals(graph.getGraphNode("D").getHeuristicValue(), 20.0d, 0.1d);
		assertEquals(graph.getGraphNode("A").getHeuristicValue(), 12.0d, 0.1d);
		assertEquals(graph.getGraphNode("B").getHeuristicValue(), 24.0d, 0.1d);
	}
}
