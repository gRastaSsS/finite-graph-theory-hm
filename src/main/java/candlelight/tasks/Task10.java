package candlelight.tasks;

import candlelight.FastGraph;
import candlelight.GraphUtil;
import candlelight.payload.SCC;
import candlelight.payload.WCC;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Task10 {
    public static FastGraph run(FastGraph graph) throws FileNotFoundException {
        WCC wcc = GraphUtil.weaklyConnectedComponents(graph);
        SCC scc = GraphUtil.stronglyConnectedComponents(graph);

        try (PrintWriter out = new PrintWriter("task10.txt")) {
            out.write(scc.toString());
            out.write("\n||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||\n\n");
            out.write(wcc.toString());
        }

        return GraphUtil.ofComponent(wcc.maxComponent(), graph).makeUndirected();
    }
}
