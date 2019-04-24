package candlelight;

import candlelight.model.FastGraph;
import candlelight.model.IMatrix;
import candlelight.model.SCC;
import candlelight.model.WCC;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntList;
import org.gephi.graph.api.Graph;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.style.Styler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception {
        Graph srcGraph = FileUtil.loadGephiGraph("getaway/vk-friends.gexf");

        FastGraph graph0 = Converter.gephiGraphToFastGraph(srcGraph);

        //WCC wcc = GraphUtil.weaklyConnectedComponents(graph0);
        //SCC scc = GraphUtil.stronglyConnectedComponents(graph0);

        //FileUtil.writeToFile("components.txt", scc.toString() + "\n" + wcc.toString());

        //FastGraph graph1 = Converter.componentToGraph(wcc.maxComponent(), graph0).makeUndirected();

        //task20(graph1);
        //task21(graph1);

        //new GraphViewer(graph0);

        //new GraphViewer(graph1, GraphUtil.commonNeighborsIndex(graph1));
        //new GraphViewer(graph1, GraphUtil.jaccardsIndex(graph1));
        //new GraphViewer(graph1, GraphUtil.adamicAdarIndex(graph1));
        //new GraphViewer(graph1, GraphUtil.preferentialAttachmentIndex(graph1));

        //new GraphViewer(graph1, GraphUtil.degreeCentrality(graph1));
        //new GraphViewer(graph1, GraphUtil.closenessCentrality(graph1));
        //new GraphViewer(graph1, GraphUtil.eigenVectorCentrality(graph1));

        //OMatrix<ObjectList<IntList>> shortestPaths = GraphUtil.findAllShortestPaths(graph1);

        //new GraphViewer(graph1, GraphUtil.betweennessCentrality(graph1, shortestPaths));
        //new GraphViewer(graph1, GraphUtil.edgeBetweennessCentrality(graph1, shortestPaths));

        //FileUtil.writeToFile("common_neighbors.csv", fMatrixToMatrix(GraphUtil.commonNeighborsIndex(graph1)).toCSV());
        //FileUtil.writeToFile("jaccards.csv", fMatrixToMatrix(GraphUtil.jaccardsIndex(graph1)).toCSV());
        //FileUtil.writeToFile("adamic_adar.csv", fMatrixToMatrix(GraphUtil.adamicAdarIndex(graph1)).toCSV());
        //FileUtil.writeToFile("preferential_attachment.csv", fMatrixToMatrix(GraphUtil.preferentialAttachmentIndex(graph1)).toCSV());

        //FileUtil.writeToFile("src_graph.csv", Converter.graphTola4jAdjMatrix(graph0).toCSV());
        //FileUtil.writeToFile("max_weakly_connected_component.csv", Converter.graphTola4jAdjMatrix(graph1).toCSV());

        //Task20.run(graph);
        //Task30.run(graph);
        //Task40.run(graph);

        //new GraphViewer(graph, GraphUtil.preferentialAttachmentIndex(graph));
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

        CategoryChart chart = new CategoryChartBuilder()
                .width(800)
                .height(600)
                .title("Degree Distribution. Average Degree: " + GraphUtil.getUndirectedAverageDegree(graph))
                .xAxisTitle("Degree Range")
                .yAxisTitle("Number of vertices")
                .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setAvailableSpaceFill(0.99);
        chart.getStyler().setOverlapped(true);

        List x = result
                .keySet()
                .stream()
                .map(i -> i + "-" + (i + width - 1))
                .collect(Collectors.toList());

        List y = new ArrayList<>(result.values());

        chart.addSeries("Degree Group", x, y);

        BitmapEncoder.saveBitmapWithDPI(chart, "degree_distribution_chart", BitmapEncoder.BitmapFormat.PNG, 300);
    }

    private static void task21(FastGraph graph) throws IOException {
        IMatrix shortest = GraphUtil.undirectedShortestPaths(graph);

        String res =
                "Graph diameter: " + shortest.maxmax() + "\n" +
                "Graph radius: " + shortest.minmax() + "\n" +
                "Central vertices: " + shortest.minmaxVertices() + "\n" +
                "Peripheral vertices: " + shortest.maxmaxVertices() + "\n" +
                "Average path length: " + shortest.average();

        FileUtil.writeToFile("graph_metrics.txt", res);
    }
}
