package candlelight;

public class IndexMapper {
    public static long TD08_II_L(int i0, int i1) {
        return (((long)i1) << 32) | (i0 & 0xFFFFFFFFL);
    }

    public static int TD08_L_0I(long v) {
        return (int) (v);
    }

    public static int TD08_L_1I(long v) {
        return (int) ((v >> 32));
    }
}
