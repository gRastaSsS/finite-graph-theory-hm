package candlelight;

import candlelight.model.FMatrix;
import candlelight.model.FastGraph;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;

import java.util.Comparator;
import java.util.stream.Collectors;

public class Converter {
    public static void printNodeAttribute(Graph graph, String name, int nodeId) {
        for (Node n : graph.getNodes()) {
            if (n.getStoreId() == nodeId) {
                System.out.println(n.getAttribute(name));
            }
        }
    }

    public static FastGraph componentToGraph(int[] component, FastGraph graph) {
        FastGraph nGraph = new FastGraph(component.length);

        for (int v0 : component) {
            for (int v1 : component) {
                if (graph.hasEdge(v0, v1)) {
                    nGraph.addEdge(v0, v1);
                }
            }
        }

        return nGraph;
    }

    public static FastGraph gephiGraphToFastGraph(Graph graph) {
        FastGraph g = new FastGraph(graph.getNodeCount());

        for (int i = 0; i < graph.getNodeCount(); ++i)
            g.addVertex(i);

        for (Node node : graph.getNodes()) {
            int index = node.getStoreId();

            for (Edge edge : graph.getEdges(node)) {
                if (edge.getSource().getStoreId() == index) {
                    g.addEdge(index, edge.getTarget().getStoreId());
                }
            }
        }

        return g;
    }

    public static Matrix fMatrixToMatrix(FMatrix fMatrix) {
        Matrix matrix = new Basic2DMatrix(fMatrix.vertices().size(), fMatrix.vertices().size());

        Int2IntMap indexMap = new Int2IntOpenHashMap();

        int index = 0;
        for (int vertex : fMatrix.vertices().stream().sorted().collect(Collectors.toList())) {
            indexMap.put(vertex, index);
            index++;
        }

        for (int v0 : fMatrix.vertices()) {
            for (int v1 : fMatrix.vertices()) {
                int index0 = indexMap.get(v0);
                int index1 = indexMap.get(v1);

                matrix.set(index0, index1, fMatrix.get(v0, v1));
            }
        }

        return matrix;
    }

    public static Matrix graphTola4jAdjMatrix(FastGraph graph) {
        int size = graph.getVertices().stream().max(Comparator.comparingInt(integer -> integer)).get() + 1;

        Matrix matrix = new Basic2DMatrix(size, size);

        for (int v0 : graph.getVertices()) {
            for (int v1 : graph.getEdges(v0)) {
                matrix.set(v0, v1, 1);
            }
        }

        return matrix;
    }
}
