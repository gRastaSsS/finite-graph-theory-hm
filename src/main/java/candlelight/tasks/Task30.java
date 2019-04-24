package candlelight.tasks;

import candlelight.Converter;
import candlelight.FastGraph;
import candlelight.GraphUtil;
import org.la4j.Matrix;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import static candlelight.GraphUtil.*;

public class Task30 {
    public static void run(FastGraph graph) throws IOException {
        Matrix commonNeighbors = Converter.fMatrixToUndirectedSkipMatrix(commonNeighborsIndex(graph));
        Matrix jaccards = Converter.fMatrixToUndirectedSkipMatrix(jaccardsIndex(graph));
        Matrix adamicAdar = Converter.fMatrixToUndirectedSkipMatrix(adamicAdarIndex(graph));
        Matrix preferentialAttachment = Converter.fMatrixToUndirectedSkipMatrix(preferentialAttachmentIndex(graph));

        Path file0 = Paths.get("common_neighbors");
        Files.write(file0, Collections.singleton(commonNeighbors.toCSV()), Charset.forName("UTF-8"));

        Path file1 = Paths.get("jaccards");
        Files.write(file1, Collections.singleton(jaccards.toCSV()), Charset.forName("UTF-8"));

        Path file2 = Paths.get("adamic_adar");
        Files.write(file2, Collections.singleton(adamicAdar.toCSV()), Charset.forName("UTF-8"));

        Path file3 = Paths.get("preferential_attachment");
        Files.write(file3, Collections.singleton(preferentialAttachment.toCSV()), Charset.forName("UTF-8"));
    }
}
