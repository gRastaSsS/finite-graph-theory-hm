package candlelight;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntStack;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;

//https://www.geeksforgeeks.org/strongly-connected-components/
public class FastGraph {
    private IntList[] adj;

    //private Int2ObjectMap<IntList> adj;

    public FastGraph(Graph graph) {
        this.adj = new IntList[graph.getNodeCount()];

        for (int i = 0; i < V(); ++i)
            adj[i] = new IntArrayList();

        for (Node node : graph.getNodes()) {
            int index = node.getStoreId();

            for (Edge edge : graph.getEdges(node)) {
                if (edge.getSource().getStoreId() == index) {
                    adj[index].add(edge.getTarget().getStoreId());
                }
            }
        }
    }

    public FastGraph(int V) {
        this.adj = new IntList[V];

        for (int i = 0; i < V; ++i)
            adj[i] = new IntArrayList();
    }

    public void addEdge(int v, int w) {
        adj[v].add(w);
    }

    public boolean hasEdge(int v, int w) {
        return adj[v].contains(w);
    }

    public int V() {
        return adj.length;
    }

    public FastGraph makeUndirected() {
        FastGraph copy = new FastGraph(V());

        for (int from = 0; from < adj.length; from++) {
            for (int to : adj[from]) {
                copy.addEdge(from, to);

                if (!copy.hasEdge(to, from)) {
                    copy.addEdge(to, from);
                }
            }
        }

        return copy;
    }

    public FastGraph makeTranspose() {
        FastGraph g = new FastGraph(V());

        for (int v = 0; v < V(); v++) {
            for (int i : adj[v])
                g.adj[i].add(v);
        }

        return g;
    }

    public void dfs(int v, boolean[] visited, IntList result) {
        visited[v] = true;
        result.add(v);

        for (int n : adj[v]) {
            if (!visited[n]) {
                dfs(n, visited, result);
            }
        }
    }

    public void fillOrder(int v, boolean[] visited, IntStack stack) {
        visited[v] = true;

        for (int n : adj[v]) {
            if (!visited[n])
                fillOrder(n, visited, stack);
        }

        stack.push(v);
    }
}
