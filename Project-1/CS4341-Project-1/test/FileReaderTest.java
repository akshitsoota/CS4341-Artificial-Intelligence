import static org.junit.Assert.*;

import org.junit.Test;

public class FileReaderTest {

	@Test
	public void testConstructor() {
		FileReader reader = new FileReader();
		Graph graph = new Graph();
		reader.convertFileToGraph(graph, "graph.txt");
		
	}

}
