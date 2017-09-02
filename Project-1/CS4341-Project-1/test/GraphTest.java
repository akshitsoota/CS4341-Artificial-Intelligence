import org.junit.Assert;
import org.junit.Test;

public class GraphTest {
    private Graph mockGraph() {
        final Graph sample = new Graph();
        sample.addConnection("S", "M", 15);
        sample.addConnection("S", "A", 1);
        sample.addConnection("M", "G", 14);
        sample.addConnection("M", "L", 35);

        sample.setHeuristicValue("S", 22);
        sample.setHeuristicValue("M", 14);
        sample.setHeuristicValue("A", 12);
        sample.setHeuristicValue("G", 0);
        sample.setHeuristicValue("L", 4);

        return sample;
    }

    @Test
    public void assertGraph() {
        final Graph graph = mockGraph();

        Assert.assertTrue(graph.isConnected("S", "S"));
        Assert.assertTrue(graph.isConnected("S", "M"));
        Assert.assertTrue(graph.isConnected("S", "A"));
        Assert.assertTrue(graph.isConnected("M", "S"));
        Assert.assertTrue(graph.isConnected("A", "S"));
        Assert.assertFalse(graph.isConnected("S", "B"));
        Assert.assertFalse(graph.isConnected("S", "L"));
        Assert.assertFalse(graph.isConnected("B", "S"));
        Assert.assertFalse(graph.isConnected("X", "Y"));
        Assert.assertFalse(graph.isConnected("Y", "X"));
        Assert.assertFalse(graph.isConnected("Y", "Y"));
    }
}
