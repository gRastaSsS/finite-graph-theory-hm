package candlelight;

import candlelight.model.*;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.gephi.graph.api.Graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {
    private static FastGraph generateFastGraph() throws Exception {
        Graph srcGraph = FileUtil.loadGephiGraph("getaway/graph.gexf");
        return Converter.gephiGraphToFastGraph(srcGraph);
    }

    public static void main(String[] args) throws Exception {
        FastGraph graph = generateFastGraph();

        WCC wcc = GraphUtil.weaklyConnectedComponents(graph);
        //SCC scc = GraphUtil.stronglyConnectedComponents(graph);

        //System.out.println(wcc);
       // System.out.println(scc);

        //FileUtil.writeToFile("components.txt", scc.toString() + "\n" + wcc.toString());

        FastGraph compGraph = Converter.componentToGraph(wcc.maxComponent(), graph).makeUndirected();

        //task20(compGraph);
        //task21(compGraph);

        //new GraphViewer(graph);

        //new GraphViewer(compGraph, GraphUtil.commonNeighborsIndex(graph1));
        //new GraphViewer(compGraph, GraphUtil.jaccardsIndex(graph1));
        //new GraphViewer(compGraph, GraphUtil.adamicAdarIndex(compGraph));
        //new GraphViewer(compGraph, GraphUtil.preferentialAttachmentIndex(graph1));

        //OMatrix<ObjectList<IntList>> shortestPath = GraphUtil.findAllShortestPaths(compGraph);

        Int2FloatMap degreeCentrality = GraphUtil.degreeCentrality(compGraph);
        //Int2FloatMap closenessCentrality = GraphUtil.closenessCentrality(compGraph);
        //Int2FloatMap eigenVectorCentrality = GraphUtil.eigenVectorCentrality(compGraph);
        //Int2FloatMap betweennessCentrality = GraphUtil.betweennessCentrality(compGraph, shortestPath);

        new GraphViewer(compGraph, degreeCentrality);
        //new GraphViewer(compGraph, closenessCentrality);
        //new GraphViewer(compGraph, eigenVectorCentrality);
        //new GraphViewer(compGraph, betweennessCentrality);

        System.out.println(degreeCentrality);

        //FileUtil.writeToFile("common_neighbors.csv", fMatrixToMatrix(GraphUtil.commonNeighborsIndex(compGraph)).toCSV());
        //FileUtil.writeToFile("jaccards.csv", fMatrixToMatrix(GraphUtil.jaccardsIndex(compGraph)).toCSV());
        //FileUtil.writeToFile("adamic_adar.csv", fMatrixToMatrix(GraphUtil.adamicAdarIndex(compGraph)).toCSV());
        //FileUtil.writeToFile("preferential_attachment.csv", fMatrixToMatrix(GraphUtil.preferentialAttachmentIndex(compGraph)).toCSV());

        //FileUtil.writeToFile("src_graph.csv", Converter.graphTola4jAdjMatrix(compGraph).toCSV());
        //FileUtil.writeToFile("max_weakly_connected_component.csv", Converter.graphTola4jAdjMatrix(compGraph).toCSV());
    }

    private static void task20(FastGraph graph) throws IOException {
        int maxDegree = GraphUtil.getMaxDegree(graph);
        IntList degrees = GraphUtil.getDegrees(graph);
        int width = 1 + (int) Math.floor(Math.log(degrees.size()) / Math.log(2));

        Int2IntMap frequency = new Int2IntOpenHashMap();

        for (int d : degrees) {
            if (frequency.containsKey(d)) {
                int curr = frequency.get(d);
                frequency.put(d, ++curr);
            }
            else frequency.put(d, 1);
        }

        Int2IntMap result = new Int2IntArrayMap();
        for (int i = 0; i < maxDegree; i+=width) {
            result.put(i, 0);
        }

        Arrays.stream(degrees.toIntArray())
                .distinct()
                .forEach(d -> {
                    int count = frequency.get(d);
                    int b = d / width * width;
                    int curr = result.get(b);
                    result.put(b, curr + count);
                });

        ChartUtil.generateAndSaveToFile(
                "Degree Distribution. Average Degree: " + GraphUtil.getUndirectedAverageDegree(graph),
                "Degree Range", "Number of vertices", "Degree Group",
                result
                        .keySet()
                        .stream()
                        .map(i -> i + "-" + (i + width - 1))
                        .collect(Collectors.toList()),

                new ArrayList<>(result.values()),

                "degree_distribution_chart"
        );
    }

    private static void task21(FastGraph graph) throws IOException {
        IMatrix shortest = GraphUtil.shortestPaths(graph);

        String res =
                "Graph diameter: " + shortest.maxmax() + "\n" +
                "Graph radius: " + shortest.minmax() + "\n" +
                "Central vertices: " + shortest.minmaxVertices() + "\n" +
                "Peripheral vertices: " + shortest.maxmaxVertices() + "\n" +
                "Average path length: " + shortest.average();

        FileUtil.writeToFile("graph_metrics.txt", res);
    }
}
