package candlelight.model;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

import static candlelight.mapper.IndexMapper.TD08_II_L;

public class OMatrix<T> {
    private final Long2ObjectMap<T> core;
    private final IntSet vertices;

    public OMatrix(int V) {
        core = new Long2ObjectOpenHashMap<>(V * V);
        vertices = new IntOpenHashSet(V);
    }

    public T get(int i, int j) {
        return core.get(TD08_II_L(i, j));
    }

    public void put(int i, int j, T v) {
        core.put(TD08_II_L(i, j), v);
        vertices.add(i);
        vertices.add(j);
    }
}
