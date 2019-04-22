package candlelight.payload;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.longs.Long2FloatMap;
import it.unimi.dsi.fastutil.longs.Long2FloatOpenHashMap;
import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;

import static candlelight.mapper.IndexMapper.TD08_II_L;

public class FMatrix {
    private final Long2FloatMap core;
    private final IntSet vertices;

    public FMatrix() {
        core = new Long2FloatOpenHashMap();
        vertices = new IntOpenHashSet();
    }

    public IntSet vertices() {
        return vertices;
    }

    public float get(int i, int j) {
        return core.get(TD08_II_L(i, j));
    }

    public void put(int i, int j, float v) {
        core.put(TD08_II_L(i, j), v);
        vertices.add(i);
        vertices.add(j);
    }

    public Matrix toUndirectedMatrix() {
        int maxIndex = vertices.stream().max(Integer::compareTo).get();
        Matrix matrix = new Basic2DMatrix(maxIndex + 1, maxIndex + 1);

        for (int v0 : vertices) {
            for (int v1 : vertices) {
                matrix.set(v0, v1, get(v0, v1));
                matrix.set(v1, v0, get(v0, v1));
            }
        }

        return matrix;
    }
}
