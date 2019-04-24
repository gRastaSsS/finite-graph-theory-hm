package candlelight.model;

public class IntPair {
    public int i0, i1;

    public IntPair(int i0, int i1) {
        this.i0 = i0;
        this.i1 = i1;
    }

    @Override
    public String toString() {
        return "IntPair{" +
                "i0=" + i0 +
                ", i1=" + i1 +
                '}';
    }

    public void setI0(int i0) {
        this.i0 = i0;
    }

    public void setI1(int i1) {
        this.i1 = i1;
    }
}
