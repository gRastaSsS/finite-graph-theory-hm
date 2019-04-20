package candlelight;

import it.unimi.dsi.fastutil.ints.*;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;

import java.util.ArrayList;
import java.util.List;

//https://www.geeksforgeeks.org/strongly-connected-components/
public class FastGraph {
    private Int2ObjectMap<IntSet> adj;

    public FastGraph(Graph graph) {
        this.adj = new Int2ObjectOpenHashMap<>(graph.getNodeCount());

        for (int i = 0; i < graph.getNodeCount(); ++i)
            adj.put(i, new IntOpenHashSet());

        for (Node node : graph.getNodes()) {
            int index = node.getStoreId();

            for (Edge edge : graph.getEdges(node)) {
                if (edge.getSource().getStoreId() == index) {
                    adj.get(index).add(edge.getTarget().getStoreId());
                }
            }
        }
    }

    public FastGraph(int V) {
        this.adj = new Int2ObjectOpenHashMap<>(V);
    }

    public void addEdge(int v, int w) {
        adj.computeIfAbsent(v, i -> new IntOpenHashSet()).add(w);
    }

    public boolean hasEdge(int v, int w) {
        IntSet s = adj.get(v);
        return s != null && s.contains(w);
    }

    public IntSet getEdges(int v) {
        return adj.computeIfAbsent(v, i -> new IntOpenHashSet());
    }

    public IntSet getVertices() {
        return adj.keySet();
    }

    public int V() {
        return adj.size();
    }

    public int E() {
        int r = 0;

        for (int v : getVertices()) {
            IntSet edges = getEdges(v);
            r += edges.size();
        }

        return r;
    }

    public FastGraph makeUndirected() {
        FastGraph copy = new FastGraph(V());

        for (int from = 0; from < adj.size(); from++) {
            for (int to : getEdges(from)) {
                if (!copy.hasEdge(from, to))
                    copy.addEdge(from, to);

                if (!copy.hasEdge(to, from))
                    copy.addEdge(to, from);
            }
        }

        return copy;
    }

    public FastGraph makeTranspose() {
        FastGraph g = new FastGraph(V());

        for (int v = 0; v < V(); v++) {
            for (int i : getEdges(v))
                g.addEdge(i, v);
        }

        return g;
    }

    public void dfs(int v, boolean[] visited, IntList result) {
        visited[v] = true;
        result.add(v);

        for (int n : getEdges(v)) {
            if (!visited[n]) {
                dfs(n, visited, result);
            }
        }
    }

    public void fillOrder(int v, boolean[] visited, IntStack stack) {
        visited[v] = true;

        for (int n : getEdges(v)) {
            if (!visited[n])
                fillOrder(n, visited, stack);
        }

        stack.push(v);
    }
}
