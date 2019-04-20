package candlelight;

import candlelight.payload.SCC;
import candlelight.payload.WCC;
import it.unimi.dsi.fastutil.ints.*;
import org.gephi.graph.api.Graph;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static Graph srcGraph;

    public static void main(String[] args) throws Exception {
        srcGraph = Loader.load("getaway/vk-friends.gexf");

        task20(task10(new FastGraph(srcGraph)));
    }

    private static FastGraph task10(FastGraph graph) {
        WCC wcc = GraphUtil.weaklyConnectedComponents(graph);
        SCC scc = GraphUtil.stronglyConnectedComponents(graph);

        System.out.println(wcc);
        System.out.println(scc);

        return GraphUtil.ofComponent(wcc.maxComponent(), graph).makeUndirected();
    }

    private static void task20(FastGraph graph) {
        task20_1(graph);
        task20_2(graph);
    }

    private static void task20_1(FastGraph graph) {
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
                .title("Degree Distribution. Average degree: " + GraphUtil.getUndirectedAverageDegree(graph))
                .xAxisTitle("Degree range")
                .yAxisTitle("Frequency")
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

        chart.addSeries("degree group", x, y);

        new SwingWrapper<>(chart).displayChart();
    }

    private static void task20_2(FastGraph graph) {
        //for (int v : graph.getVertices()) {
        //    System.out.println(v + ": " + graph.getEdges(v));
        //}

        graph.printAllPaths(0, 1);

       // System.out.println("af");
    }
}
