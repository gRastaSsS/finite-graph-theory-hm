package candlelight;

import candlelight.datapack.SCC;
import candlelight.datapack.WCC;
import org.gephi.graph.api.Graph;

public class Main {
    private static Graph srcGraph;

    public static void main(String[] args) throws Exception {
        srcGraph = Loader.load("getaway/vk-friends.gexf");

        FastGraph maxComponent = task10();

        System.out.println(maxComponent.V());
    }

    public static FastGraph task10() {
        FastGraph simpleGraph = new FastGraph(srcGraph);

        WCC wcc = GraphUtil.weaklyConnectedComponents(simpleGraph);
        SCC scc = GraphUtil.stronglyConnectedComponents(simpleGraph);

        System.out.println(wcc);
        System.out.println(scc);

        return GraphUtil.ofComponent(wcc.maxComponent(), simpleGraph).makeUndirected();
    }
}
