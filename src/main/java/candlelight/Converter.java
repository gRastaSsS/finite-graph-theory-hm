package candlelight;

import candlelight.payload.FMatrix;
import candlelight.payload.IMatrix;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;

import java.util.Comparator;
import java.util.stream.Collectors;

public class Converter {
    public static Matrix iMatrixToUndirectedSkipMatrix(IMatrix iMatrix) {
        Matrix matrix = new Basic2DMatrix(iMatrix.vertices().size(), iMatrix.vertices().size());

        Int2IntMap indexMap = new Int2IntOpenHashMap();

        int index = 0;
        for (int vertex : iMatrix.vertices().stream().sorted().collect(Collectors.toList())) {
            indexMap.put(vertex, index);
            index++;
        }

        for (int v0 : iMatrix.vertices()) {
            for (int v1 : iMatrix.vertices()) {
                int index0 = indexMap.get(v0);
                int index1 = indexMap.get(v1);

                matrix.set(index0, index1, iMatrix.get(v0, v1));
                matrix.set(index1, index0, iMatrix.get(v0, v1));
            }
        }

        return matrix;
    }

    public static Matrix fMatrixToUndirectedSkipMatrix(FMatrix fMatrix) {
        Matrix matrix = new Basic2DMatrix(fMatrix.vertices().size(), fMatrix.vertices().size());

        Int2IntMap indexMap = new Int2IntOpenHashMap();

        int index = 0;
        for (int vertex : fMatrix.vertices().stream().sorted().collect(Collectors.toList())) {
            indexMap.put(vertex, index);
            index++;
        }

        for (int v0 : fMatrix.vertices()) {
            for (int v1 : fMatrix.vertices()) {
                int index0 = indexMap.get(v0);
                int index1 = indexMap.get(v1);

                matrix.set(index0, index1, fMatrix.get(v0, v1));
                matrix.set(index1, index0, fMatrix.get(v0, v1));
            }
        }

        return matrix;
    }

    public static Matrix iMatrixToUndirectedMatrix(IMatrix iMatrix) {
        Matrix matrix = new Basic2DMatrix(iMatrix.vertices().size(), iMatrix.vertices().size());

        for (int v0 : iMatrix.vertices()) {
            for (int v1 : iMatrix.vertices()) {
                matrix.set(v0, v1, iMatrix.get(v0, v1));
                matrix.set(v1, v0, iMatrix.get(v0, v1));
            }
        }

        return matrix;
    }

    public static Matrix fMatrixToUndirectedMatrix(FMatrix fMatrix) {
        Matrix matrix = new Basic2DMatrix(fMatrix.vertices().size(), fMatrix.vertices().size());

        for (int v0 : fMatrix.vertices()) {
            for (int v1 : fMatrix.vertices()) {
                matrix.set(v0, v1, fMatrix.get(v0, v1));
                matrix.set(v1, v0, fMatrix.get(v0, v1));
            }
        }

        return matrix;
    }

    public static Matrix graphToAdjMatrix(FastGraph graph) {
        int size = graph.getVertices().stream().max(Comparator.comparingInt(integer -> integer)).get() + 1;

        Matrix matrix = new Basic2DMatrix(size, size);

        for (int v0 : graph.getVertices()) {
            for (int v1 : graph.getEdges(v0)) {
                matrix.set(v0, v1, 1);
            }
        }

        return matrix;
    }
}
