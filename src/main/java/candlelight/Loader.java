package candlelight;

import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import java.io.File;

public class Loader {
    public static Graph load(String fileName) throws Exception {
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();

        ImportController importController = Lookup.getDefault().lookup(ImportController.class);

        Container container = importController.importFile(loadFile(fileName));

        importController.process(container, new DefaultProcessor(), workspace);

        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel();

        return graphModel.getGraph();
    }

    private static File loadFile(String fileName) {
        ClassLoader classLoader = Loader.class.getClassLoader();
        return new File(classLoader.getResource(fileName).getFile());
    }
}
