package candlelight.model;

import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.longs.Long2FloatMap;
import it.unimi.dsi.fastutil.longs.Long2FloatOpenHashMap;

import static candlelight.IndexMapper.TD08_II_L;

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

    public float maxmax() {
        float res = 0;

        for (float v : core.values())
            res = Math.max(res, v);

        return res;
    }

    public float minmin() {
        float res = Float.MAX_VALUE;

        for (float v : core.values())
            res = Math.min(res, v);

        return res;
    }
}
