package candlelight.payload;

import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;

import static candlelight.Constants.MAX_MATRIX_VALUE;

public class VMatrix {
    private final Long2IntMap core;
    private final IntSet vertices;

    public VMatrix() {
        core = new Long2IntOpenHashMap();
        vertices = new IntOpenHashSet();
    }

    private long TD08_II_L(int v0, int v1) {
        return (((long)v1) << 32) | (v0 & 0xFFFFFFFFL);
    }

    public int get(int i, int j) {
        return core.get(TD08_II_L(i, j));
    }

    public void put(int i, int j, int v) {
        core.put(TD08_II_L(i, j), v);
        vertices.add(i);
        vertices.add(j);
    }

    public float average() {
        float res = 0;

        for (int v0 : vertices) {
            for (int v1 : vertices) {
                int a = get(v0, v1);

                if (v0 != v1 && a < MAX_MATRIX_VALUE) {
                    res += a;
                }
            }
        }

        res = res / (vertices.size() * (vertices.size() - 1));
        return res;
    }

    public IntList maxmaxVertices() {
        IntList res = new IntArrayList();

        int maxmax = maxmax();

        for (int v0 : vertices) {
            int e = 0;
            for (int v1 : vertices)
                e = Math.max(e, get(v0, v1));

            if (e == maxmax) res.add(v0);
        }

        return res;
    }

    public int maxmax() {
        int res = 0;

        for (int v : core.values())
            res = Math.max(res, v);

        return res;
    }

    public IntList minmaxVertices() {
        IntList res = new IntArrayList();

        int minmax = minmax();

        for (int v0 : vertices) {
            int e = 0;
            for (int v1 : vertices)
                e = Math.max(e, get(v0, v1));

            if (e == minmax) res.add(v0);
        }

        return res;
    }

    public int minmax() {
        int res = Integer.MAX_VALUE;

        for (int v0 : vertices) {
            int e = 0;
            for (int v1 : vertices)
                e = Math.max(e, get(v0, v1));

            res = Math.min(res, e);
        }

        return res;
    }
}
