package candlelight;

import candlelight.tasks.Task10;
import candlelight.tasks.Task20;
import candlelight.tasks.Task30;
import candlelight.tasks.Task40;
import org.gephi.graph.api.Graph;

public class Main {
    public static void main(String[] args) throws Exception {
        Graph srcGraph = Loader.load("getaway/vk-friends.gexf");

        FastGraph graph = Task10.run(new FastGraph(srcGraph));
        Task20.run(graph);
        Task30.run(graph);
        Task40.run(graph);
    }
}
