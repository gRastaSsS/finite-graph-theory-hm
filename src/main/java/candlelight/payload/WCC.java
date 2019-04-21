package candlelight.payload;

import java.util.Arrays;

public class WCC {
    private final int[][] components;

    public WCC(int[][] components) {
        this.components = components;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("Number of weakly connected components: " + components.length + ".\n");
        out.append("Components: \n");

        for (int[] c : components) {
            out.append(Arrays.toString(c)).append("; Number of vertices: ").append(c.length).append(".\n");
        }

        out.append("\n||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n\n");
        out.append("Max weakly connected component size: ").append(maxComponent().length).append(".\n");
        out.append("Max weakly connected component size (relatively): ").append(maxComponentSizeRelative()).append(".\n");

        return out.toString();
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
