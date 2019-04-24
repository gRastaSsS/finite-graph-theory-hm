package candlelight.tasks;

import candlelight.Converter;
import candlelight.FastGraph;
import candlelight.GraphUtil;
import candlelight.payload.OMatrix;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.la4j.Matrix;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static candlelight.GraphUtil.edgeBetweennessCentrality;

public class Task40 {
    public static void run(FastGraph graph) throws IOException {
        OMatrix<ObjectList<IntList>> shortestPaths = GraphUtil.findAllShortestPaths(graph);

        Int2FloatMap eigenVectorCentrality = GraphUtil.eigenVectorCentrality(graph);
        Int2FloatMap degreeCenrality = GraphUtil.degreeCentrality(graph);
        Int2FloatMap closenessCentrality = GraphUtil.closenessCentrality(graph);
        Int2FloatMap betweennessCentrality = GraphUtil.betweennessCentrality(graph, shortestPaths);
        Matrix edgeBetweennessCentrality = Converter.fMatrixToUndirectedSkipMatrix(edgeBetweennessCentrality(graph, shortestPaths));

        Path file0 = Paths.get("eigen_vector_centrality");
        Files.write(file0, Collections.singleton(eigenVectorCentrality.toString()), Charset.forName("UTF-8"));

        Path file1 = Paths.get("degree_centrality");
        Files.write(file1, Collections.singleton(degreeCenrality.toString()), Charset.forName("UTF-8"));

        Path file2 = Paths.get("closeness_centrality");
        Files.write(file2, Collections.singleton(closenessCentrality.toString()), Charset.forName("UTF-8"));

        Path file3 = Paths.get("betweenness_centrality");
        Files.write(file3, Collections.singleton(betweennessCentrality.toString()), Charset.forName("UTF-8"));

        Path file4 = Paths.get("edge_betweenness_centrality");
        Files.write(file4, Collections.singleton(edgeBetweennessCentrality.toCSV()), Charset.forName("UTF-8"));
    }
}
