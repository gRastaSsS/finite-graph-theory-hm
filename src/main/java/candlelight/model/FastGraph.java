package candlelight.model;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;

public class FastGraph {
    private Int2ObjectMap<IntSet> adj;

    public FastGraph(int V) {
        this.adj = new Int2ObjectOpenHashMap<>(V);
    }

    public void addVertex(int v) {
        adj.put(v, new IntOpenHashSet());
    }

    public void addEdge(int v, int w) {
        adj.computeIfAbsent(v, i -> new IntOpenHashSet()).add(w);
    }

    public void addUndirectedEdge(int v, int w) {
        adj.computeIfAbsent(v, i -> new IntOpenHashSet()).add(w);
        adj.computeIfAbsent(w, i -> new IntOpenHashSet()).add(v);
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
}
