package candlelight.datapack;

public class WCC {
    private final int[][] components;

    public WCC(int[][] components) {
        this.components = components;
    }

    @Override
    public String toString() {
        return "Number of weakly connected components: " + components.length + "\n" +
                "Max weakly connected component size: " + maxComponent().length + "\n" +
                "Max weakly connected component size (relatively): " + maxComponentSizeRelative() + "\n";
    }

    public int[] maxComponent() {
        int[] component = null;
        int max = 0;

        for (int[] c : components) {
            if (c.length > max) {
                max = c.length;
                component = c;
            }
        }

        return component;
    }

    public float maxComponentSizeRelative() {
        int max = 0;
        for (int[] c : components) {
            max = Math.max(c.length, max);
        }

        int c = 0;
        for (int[] component : components) {
            c += component.length;
        }

        return (float) max / c;
    }
}
