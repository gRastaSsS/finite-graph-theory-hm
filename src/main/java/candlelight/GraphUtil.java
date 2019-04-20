package candlelight;

import candlelight.payload.SCC;
import candlelight.payload.WCC;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntStack;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class GraphUtil {
    public static void printNodeAttribute(Graph graph, String name, int nodeId) {
        for (Node n : graph.getNodes()) {
            if (n.getStoreId() == nodeId) {
                System.out.println(n.getAttribute(name));
            }
        }
    }

    public static FastGraph ofComponent(int[] component, FastGraph graph) {
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

    public static SCC stronglyConnectedComponents(FastGraph graph) {
        boolean[] visited = new boolean[graph.V()];

        IntStack stack = new IntArrayList();

        for (int i = 0; i < graph.V(); i++)
            if (!visited[i])
                graph.fillOrder(i, visited, stack);

        graph = graph.makeTranspose();

        Arrays.fill(visited, false);

        List<int[]> components = new ArrayList<>();
        IntList temp = new IntArrayList();

        while (!stack.isEmpty()) {
            int v = stack.popInt();

            if (!visited[v]) {
                graph.dfs(v, visited, temp);
                components.add(temp.toIntArray());
                temp.clear();
            }
        }

        return new SCC(components.toArray(new int[0][]));
    }

    public static WCC weaklyConnectedComponents(FastGraph graph) {
        boolean[] visited = new boolean[graph.V()];

        IntStack nodes = IntStream.range(0, graph.V()).collect(
                (Supplier<IntStack>) IntArrayList::new,
                IntStack::push,
                (integers, integers2) -> { }
        );

        graph = graph.makeUndirected();

        IntList temp = new IntArrayList();
        List<int[]> components = new ArrayList<>();

        while (!nodes.isEmpty()) {
            int from = nodes.popInt();

            if (!visited[from]) {
                graph.dfs(from, visited, temp);
                components.add(temp.toIntArray());
                temp.clear();
            }
        }

        return new WCC(components.toArray(new int[0][]));
    }

    public static IntList getDegrees(FastGraph graph) {
        IntList degrees = new IntArrayList();

        for (int v : graph.getVertices()) {
            degrees.add(graph.getEdges(v).size());
        }

        return degrees;
    }

    public static float getUndirectedAverageDegree(FastGraph graph) {
        return (float) graph.E() / graph.V();
    }

    public static int getMaxDegree(FastGraph graph) {
        int max = 0;

        for (int v : graph.getVertices()) {
            max = Math.max(graph.getEdges(v).size(), max);
        }

        return max;
    }
}
