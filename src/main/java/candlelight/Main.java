package candlelight;

import candlelight.tasks.Task10;
import org.gephi.graph.api.Graph;

public class Main {
    public static void main(String[] args) throws Exception {
        Graph srcGraph = FileUtil.load("getaway/vk-friends.gexf");

        FastGraph graph0 = new FastGraph(srcGraph);

        //new GraphViewer(graph0);

        FastGraph graph1 = Task10.run(graph0);
        new GraphViewer(graph1);

        //Task20.run(graph);
        //Task30.run(graph);
        //Task40.run(graph);

        //new GraphViewer(graph, GraphUtil.preferentialAttachmentIndex(graph));
    }
}
