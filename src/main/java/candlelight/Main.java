package candlelight;

import candlelight.payload.FMatrix;
import candlelight.tasks.Task10;
import candlelight.tasks.Task20;
import org.gephi.graph.api.Graph;

public class Main {
    private static Graph srcGraph;

    public static void main(String[] args) throws Exception {
        srcGraph = Loader.load("getaway/vk-friends.gexf");

        FastGraph graph = Task10.run(new FastGraph(srcGraph));

        Task20.run(graph);
    }

    private static FastGraph task30(FastGraph graph) {
        for (int v0 : graph.getVertices()) {
            for (int v1 : graph.getVertices()) {
                System.out.println("Pair " + v0 + " : " + v1);
                System.out.println("Common neighbors: " + GraphUtil.commonNeighborsIndex(graph, v0, v1));
                System.out.println("Jaccards: " + GraphUtil.jaccardsIndex(graph, v0, v1));
                System.out.println("Adamic-Adar: " + GraphUtil.adamicAdarIndex(graph, v0, v1));
                System.out.println("Preferential Attachment: " + GraphUtil.preferentialAttachmentIndex(graph, v0, v1));
            }
        }

        return graph;
    }

    private static void task40(FastGraph graph) {
        FMatrix f = GraphUtil.edgeBetweennessCentrality(graph);

        for (int v0 : f.vertices()) {
            for (int v1 : f.vertices()) {
                System.out.println(v0 + " " + v1 + ": " + f.get(v0, v1));
            }
        }
    }
}
