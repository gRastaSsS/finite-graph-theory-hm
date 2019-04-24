package candlelight;

import candlelight.model.*;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.Node;
import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.decomposition.EigenDecompositor;
import org.la4j.decomposition.MatrixDecompositor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static candlelight.Constants.MAX_MATRIX_VALUE;

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
        Int2BooleanMap visited = new Int2BooleanOpenHashMap();

        IntStack stack = new IntArrayList();

        for (int v : graph.getVertices()) {
            if (!visited.get(v))
                fillOrder(v, visited, stack, graph);
        }

        graph = graph.makeTranspose();

        visited.clear();

        List<int[]> components = new ArrayList<>();
        IntList temp = new IntArrayList();

        while (!stack.isEmpty()) {
            int v = stack.popInt();

            if (!visited.get(v)) {
                dfs(v, visited, temp, graph);
                components.add(temp.toIntArray());
                temp.clear();
            }
        }

        return new SCC(components.toArray(new int[0][]));
    }

    public static WCC weaklyConnectedComponents(FastGraph graph) {
        Int2BooleanMap visited = new Int2BooleanOpenHashMap();

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

            if (!visited.get(from)) {
                dfs(from, visited, temp, graph);
                components.add(temp.toIntArray());
                temp.clear();
            }
        }

        return new WCC(components.toArray(new int[0][]));
    }

    public static void dfs(int v, Int2BooleanMap visited, IntList result, FastGraph g) {
        visited.put(v, true);

        result.add(v);

        for (int n : g.getEdges(v)) {
            if (!visited.get(n)) {
                dfs(n, visited, result, g);
            }
        }
    }

    private static void fillOrder(int v, Int2BooleanMap visited, IntStack stack, FastGraph g) {
        visited.put(v, true);

        for (int n : g.getEdges(v)) {
            if (!visited.get(n))
                fillOrder(n, visited, stack, g);
        }

        stack.push(v);
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

        for (int v : graph.getVertices())
            max = Math.max(graph.getEdges(v).size(), max);

        return max;
    }

    public static IMatrix undirectedShortestPaths(FastGraph graph) {
        IMatrix res = new IMatrix();

        for (int v0 : graph.getVertices()) {
            for (int v1 : graph.getVertices()) {
                if (graph.hasEdge(v0, v1)) {
                    res.put(v0, v1, 1);
                    res.put(v1, v0, 1);
                }

                else {
                    res.put(v0, v1, MAX_MATRIX_VALUE);
                    res.put(v1, v0, MAX_MATRIX_VALUE);
                }
            }
        }

        for (int k : graph.getVertices()) {
            for (int i : graph.getVertices()) {
                for (int j : graph.getVertices()) {
                    int val = Math.min(res.get(i, j), res.get(i, k) + res.get(k, j));
                    res.put(i, j, val);
                }
            }
        }

        return res;
    }

    public static FMatrix commonNeighborsIndex(FastGraph graph) {
        FMatrix res = new FMatrix();

        for (int v0 : graph.getVertices()) {
            for (int v1 : graph.getVertices()) {
                if (v0 == v1) continue;

                IntSet intersection = new IntOpenHashSet(graph.getEdges(v0));
                intersection.retainAll(graph.getEdges(v1));

                res.put(v0, v1, intersection.size());
            }
        }

        return res;
    }

    public static FMatrix jaccardsIndex(FastGraph graph) {
        FMatrix res = new FMatrix();

        for (int v0 : graph.getVertices()) {
            for (int v1 : graph.getVertices()) {
                if (v0 == v1) continue;

                IntSet intersection = new IntOpenHashSet(graph.getEdges(v0));
                intersection.retainAll(graph.getEdges(v1));

                IntSet union = new IntOpenHashSet(graph.getEdges(v0));
                union.addAll(graph.getEdges(v1));

                res.put(v0, v1, (float) intersection.size() / union.size());
            }
        }

        return res;
    }

    public static FMatrix adamicAdarIndex(FastGraph graph) {
        FMatrix res = new FMatrix();

        for (int v0 : graph.getVertices()) {
            for (int v1 : graph.getVertices()) {
                if (v0 == v1) continue;

                IntSet intersection = new IntOpenHashSet(graph.getEdges(v0));
                intersection.retainAll(graph.getEdges(v1));

                float r = 0;

                for (int v : intersection) {
                    int edges = graph.getEdges(v).size();
                    r = r + 1f / edges;
                }

                res.put(v0, v1, r);
            }
        }

        return res;
    }

    public static FMatrix preferentialAttachmentIndex(FastGraph graph) {
        FMatrix res = new FMatrix();

        for (int v0 : graph.getVertices()) {
            for (int v1 : graph.getVertices()) {
                if (v0 == v1) continue;

                res.put(v0, v1, graph.getEdges(v0).size() * graph.getEdges(v1).size());
            }
        }

        return res;
    }

    public static Int2FloatMap eigenVectorCentrality(FastGraph graph) {
        Int2FloatMap result = new Int2FloatOpenHashMap();

        Matrix adjMatrix = Converter.graphToAdjMatrix(graph);

        MatrixDecompositor d = new EigenDecompositor(adjMatrix);

        Matrix[] res = d.decompose();

        Matrix lambdas = res[1];

        double maxLambda = Double.MIN_VALUE;
        int vecIndex = 0;

        for (int i = 0; i < lambdas.columns(); i++) {
            if (maxLambda < lambdas.get(i, i)) {
                maxLambda = lambdas.get(i, i);
                vecIndex = i;
            }
        }

        Vector coeff = res[0].getColumn(vecIndex);

        for (int v : graph.getVertices()) {
            result.put(v, (float) coeff.get(v));
        }

        return result;
    }

    public static Int2FloatMap degreeCentrality(FastGraph graph) {
        Int2FloatMap result = new Int2FloatOpenHashMap();

        for (int v : graph.getVertices()) {
            result.put(v, graph.getEdges(v).size());
        }

        return result;
    }

    public static Int2FloatMap closenessCentrality(FastGraph graph) {
        IMatrix paths = undirectedShortestPaths(graph);

        IntSet vertices = graph.getVertices();

        Int2FloatMap result = new Int2FloatOpenHashMap();

        for (int v : graph.getVertices()) {
            float s = 0;
            for (int e : vertices)
                s += paths.get(v, e);

            s = (vertices.size() - 1) / s;
            result.put(v, s);
        }

        return result;
    }

    public static Int2FloatMap betweennessCentrality(FastGraph graph, OMatrix<ObjectList<IntList>> shortestPaths) {
        Int2FloatMap result = new Int2FloatOpenHashMap();

        for (int v : graph.getVertices()) {
            float res = 0;

            for (int s : graph.getVertices()) {
                for (int t : graph.getVertices()) {
                    if (s != t && s != v && t != v) {
                        ObjectList<IntList> paths = shortestPaths.get(s, t);

                        int sigma = paths.size();

                        int sigmaV = (int) paths
                                .stream()
                                .filter(integers -> integers.contains(v))
                                .count();

                        res = res + (float) sigmaV / sigma;
                    }
                }
            }

            result.put(v, res);
        }

        return result;
    }

    public static FMatrix edgeBetweennessCentrality(FastGraph graph, OMatrix<ObjectList<IntList>> shortestPaths) {
        FMatrix result = new FMatrix();

        for (int v0 : graph.getVertices()) {
            for (int v1 : graph.getVertices()) {
                if (graph.hasEdge(v0, v1)) {
                    float res = 0;

                    for (int s : graph.getVertices()) {
                        for (int t : graph.getVertices()) {
                            if (s != t) {
                                ObjectList<IntList> paths = shortestPaths.get(s, t);

                                int sigma = paths.size();

                                int sigmaV = (int) paths
                                        .stream()
                                        .filter(integers -> {
                                            int v0Index = integers.indexOf(v0);

                                            if (v0Index != -1) {
                                                return integers.size() > (v0Index + 1) && integers.getInt(v0Index + 1) == v1;
                                            }

                                            return false;
                                        })
                                        .count();

                                res = res + (float) sigmaV / sigma;
                            }
                        }
                    }

                    result.put(v0, v1, res);
                }
            }
        }

        return result;
    }

    public static OMatrix<ObjectList<IntList>> findAllShortestPaths(FastGraph graph) {
        IMatrix shortest = GraphUtil.undirectedShortestPaths(graph);

        OMatrix<ObjectList<IntList>> shortestPaths = new OMatrix<>(graph.getVertices().size() * graph.getVertices().size());

        for (int s : graph.getVertices()) {
            for (int t : graph.getVertices()) {
                if (s != t && shortestPaths.get(s, t) == null) {
                    ObjectList<IntList> res = findAllPaths(graph, s, t, shortest.get(s, t) + 1);
                    shortestPaths.put(s, t, res);
                    shortestPaths.put(t, s, res);
                }
            }
        }

        return shortestPaths;
    }

    private static ObjectList<IntList> findAllPaths(FastGraph graph, int s, int d, int maxPathLength) {
        Int2BooleanMap isVisited = new Int2BooleanOpenHashMap(graph.V());

        ObjectList<IntList> result = new ObjectArrayList<>();

        IntList pathList = new IntArrayList();
        pathList.add(s);

        findAllPathsRec(graph, s, d, isVisited, pathList, maxPathLength, result);

        return result;
    }

    private static void findAllPathsRec(FastGraph graph, int u, int d, Int2BooleanMap isVisited, IntList tempList, int maxPathLength, ObjectList<IntList> result) {
        if (tempList.size() > maxPathLength) return;

        isVisited.put(u, true);

        if (u == d) {
            result.add(new IntArrayList(tempList));
            isVisited.put(u, false);
            return;
        }

        for (int i : graph.getEdges(u)) {
            if (!isVisited.get(i)) {
                tempList.add(i);
                findAllPathsRec(graph, i, d, isVisited, tempList, maxPathLength, result);
                tempList.rem(i);
            }
        }

        isVisited.put(u, false);
    }
}
