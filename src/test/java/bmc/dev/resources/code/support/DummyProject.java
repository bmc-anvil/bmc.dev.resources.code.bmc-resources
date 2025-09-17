package bmc.dev.resources.code.support;

import java.io.File;

import org.apache.maven.model.Build;
import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;

public class DummyProject {

    public static MavenProject createWithTestBaseDir() {

        final MavenProject project = new MavenProject();
        final Build        build   = new Build();
        final Plugin       plugin  = new Plugin();

        plugin.setGroupId("bmc.dev.resources.code");
        plugin.setArtifactId("bmc-resources");
        plugin.setVersion("bmc-local");

        build.setDirectory("./");
        build.addPlugin(plugin);
        project.setBuild(build);
        File temp = new File("../../test-pom.xml");
        project.setFile(temp);

        return project;
    }

}
