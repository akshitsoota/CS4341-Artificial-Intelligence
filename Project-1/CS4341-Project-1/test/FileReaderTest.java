import static org.junit.Assert.*;

import org.junit.Test;

public class FileReaderTest {

	private Graph graph;
	private FileReader reader;
	
	public void init() {
		reader = new FileReader();
		graph = new Graph();
	}
	
	@Test
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
		
		assertEquals(graph.getHeuristicValue("S"), 11.0d, 0.1d);
		assertEquals(graph.getHeuristicValue("A"), 10.4d, 0.1d);
		assertEquals(graph.getHeuristicValue("D"), 8.9d, 0.1d);
		assertEquals(graph.getHeuristicValue("B"), 6.7d, 0.1d);
		assertEquals(graph.getHeuristicValue("E"), 6.9d, 0.1d);
		assertEquals(graph.getHeuristicValue("C"), 4.0d, 0.1d);
		assertEquals(graph.getHeuristicValue("F"), 3.0d, 0.1d);
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
		
		assertEquals(graph.getHeuristicValue("S"), 22.0d, 0.1d);
		assertEquals(graph.getHeuristicValue("M"), 14.0d, 0.1d);
		assertEquals(graph.getHeuristicValue("I"), 10.0d, 0.1d);
		assertEquals(graph.getHeuristicValue("J"), 8.0d, 0.1d);
		assertEquals(graph.getHeuristicValue("K"), 6.0d, 0.1d);
		assertEquals(graph.getHeuristicValue("L"), 4.0d, 0.1d);
		assertEquals(graph.getHeuristicValue("E"), 18.0d, 0.1d);
		assertEquals(graph.getHeuristicValue("C"), 16.0d, 0.1d);
		assertEquals(graph.getHeuristicValue("D"), 20.0d, 0.1d);
		assertEquals(graph.getHeuristicValue("A"), 12.0d, 0.1d);
		assertEquals(graph.getHeuristicValue("B"), 24.0d, 0.1d);
	}
}
