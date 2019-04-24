package candlelight;

import candlelight.model.FMatrix;
import candlelight.model.FastGraph;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.function.IntConsumer;

public class GraphViewer {
    private MouseEvent last;
    private double zoomLevel = 1.0;

    public GraphViewer(FastGraph graph) throws IOException {
        Graph vg = toGraphstreamGraph(graph);
        Viewer viewer = vg.display(true);
        bindPanel(viewer);
    }

    public GraphViewer(FastGraph graph, FMatrix edgeMetrics) throws IOException {
        String edgeStyle = FileUtil.loadResourceAsString("getaway/edge_style.css");
        Graph vg = toGraphstreamGraph(graph, edgeMetrics, edgeStyle);
        Viewer viewer = vg.display(true);
        bindPanel(viewer);
    }

    public GraphViewer(FastGraph graph, Int2FloatMap nodeMetrics) throws IOException {
        String edgeStyle = FileUtil.loadResourceAsString("getaway/node_style.css");
        Graph vg = toGraphstreamGraph(graph, nodeMetrics, edgeStyle);
        Viewer viewer = vg.display(true);
        bindPanel(viewer);
    }

    private void bindPanel(Viewer viewer) {
        ViewPanel viewPanel = viewer.getDefaultView();

        viewPanel.addMouseWheelListener(e -> {
            if (e.getWheelRotation() == -1) {
                zoomLevel = zoomLevel - 0.1;

                if (zoomLevel < 0.1) {
                    zoomLevel = 0.1;
                }

                viewPanel.getCamera().setViewPercent(zoomLevel);
            }
            if (e.getWheelRotation() == 1) {
                zoomLevel = zoomLevel + 0.1;
                viewPanel.getCamera().setViewPercent(zoomLevel);
            }
        });

        viewPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                last = null;
            }
        });

        viewPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent event) {
                if (last != null) {
                    Point3 p1 = viewPanel.getCamera().getViewCenter();
                    Point3 p2 = viewPanel.getCamera().transformGuToPx(p1.x,p1.y,0);

                    int xdelta = event.getX() - last.getX();
                    int ydelta = event.getY() - last.getY();

                    p2.x -= xdelta;
                    p2.y -= ydelta;

                    Point3 p3 = viewPanel.getCamera().transformPxToGu(p2.x,p2.y);
                    viewPanel.getCamera().setViewCenter(p3.x,p3.y, 0);
                }

                last = event;
            }
        });
    }

    private static Graph toGraphstreamGraph(FastGraph graph) {
        Graph g = new SingleGraph(graph.toString());
        g.addAttribute("ui.quality");

        graph.getVertices().forEach((IntConsumer) i -> g.addNode(String.valueOf(i)));

        Set<String> processed = new HashSet<>();

        for (int v0 : graph.getVertices()) {
            for (int v1 : graph.getEdges(v0)) {
                if (!processed.contains(v1 + " " + v0)) {
                    String id = v0 + " " + v1;
                    processed.add(id);

                    g.addEdge(id, String.valueOf(v0), String.valueOf(v1));
                }
            }
        }

        return g;
    }

    private static Graph toGraphstreamGraph(FastGraph graph, FMatrix edgeMetrics, String style) {
        Graph g = new SingleGraph(graph.toString());
        g.addAttribute("ui.quality");
        g.addAttribute("ui.stylesheet", style);

        graph.getVertices().forEach((IntConsumer) i -> g.addNode(String.valueOf(i)));

        Set<String> processed = new HashSet<>();

        float max = edgeMetrics.maxmax();
        float min = edgeMetrics.minmin();

        for (int v0 : graph.getVertices()) {
            for (int v1 : graph.getEdges(v0)) {
                if (!processed.contains(v1 + " " + v0)) {
                    String id = v0 + " " + v1;
                    processed.add(id);

                    Edge edge = g.addEdge(id, String.valueOf(v0), String.valueOf(v1));
                    edge.setAttribute("ui.color", ((double) edgeMetrics.get(v0, v1) - min) / (max - min));
                }
            }
        }

        return g;
    }

    private static Graph toGraphstreamGraph(FastGraph graph, Int2FloatMap vertexMetrics, String style) {
        Graph g = new SingleGraph(graph.toString());
        g.addAttribute("ui.quality");
        g.addAttribute("ui.stylesheet", style);

        float max = vertexMetrics.values().stream().max(Float::compareTo).get();
        float min = vertexMetrics.values().stream().min(Float::compareTo).get();

        graph.getVertices().forEach((IntConsumer) i -> {
            Node n = g.addNode(String.valueOf(i));
            n.setAttribute("ui.color", ((double) vertexMetrics.get(i) - min) / (max - min));
        });

        Set<String> processed = new HashSet<>();

        for (int v0 : graph.getVertices()) {
            for (int v1 : graph.getEdges(v0)) {
                if (!processed.contains(v1 + " " + v0)) {
                    String id = v0 + " " + v1;
                    processed.add(id);
                    g.addEdge(id, String.valueOf(v0), String.valueOf(v1));
                }
            }
        }

        return g;
    }
}
