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
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class FileUtil {
    public static Graph load(String fileName) throws Exception {
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();

        ImportController importController = Lookup.getDefault().lookup(ImportController.class);

        Container container = importController.importFile(loadResourceFile(fileName));

        importController.process(container, new DefaultProcessor(), workspace);

        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getGraphModel();

        return graphModel.getGraph();
    }

    public static File loadResourceFile(String filePath) {
        ClassLoader classLoader = FileUtil.class.getClassLoader();
        return new File(classLoader.getResource(filePath).getFile());
    }

    public static void writeToFile(String filePath, String data) throws IOException {
        Path file0 = Paths.get(filePath);
        Files.write(file0, Collections.singleton(data), Charset.forName("UTF-8"));
    }
}
