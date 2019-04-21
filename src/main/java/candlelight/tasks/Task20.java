package candlelight.tasks;

import candlelight.FastGraph;
import candlelight.GraphUtil;
import candlelight.payload.IMatrix;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntList;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.style.Styler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Task20 {
    public static void run(FastGraph graph) throws IOException {
        part1(graph);
        part2(graph);
    }

    private static void part1(FastGraph graph) throws IOException {
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

        BitmapEncoder.saveBitmapWithDPI(chart, "task20_1", BitmapEncoder.BitmapFormat.PNG, 300);
    }

    private static void part2(FastGraph graph) throws FileNotFoundException {
        IMatrix shortest = GraphUtil.undirectedShortestPaths(graph);

        StringBuilder res = new StringBuilder();

        res.append("Graph diameter: ").append(shortest.maxmax()).append("\n");
        res.append("Graph radius: ").append(shortest.minmax()).append("\n");
        res.append("Central vertices: ").append(shortest.minmaxVertices()).append("\n");
        res.append("Peripheral vertices: ").append(shortest.maxmaxVertices()).append("\n");
        res.append("Average path length: ").append(shortest.average());

        try (PrintWriter out = new PrintWriter("task20_2.txt")) {
            out.write(res.toString());
        }
    }
}
