package bmc.dev.resources.code.support;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.apache.maven.model.Build;
import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;

import lombok.SneakyThrows;

import static bmc.dev.resources.code.bmcresources.io.IOUtilities.copyResourceSingle;

public class DummyProjectForTest {

    @SneakyThrows
    public static MavenProject createWithTestBaseDir() {

        final MavenProject project = new MavenProject();
        final Build        build   = new Build();
        final Plugin       plugin  = new Plugin();

        plugin.setGroupId("bmc.dev.resources.code");
        plugin.setArtifactId("bmc-resources");
        plugin.setVersion("bmc-local");

        build.addPlugin(plugin);
        project.setBuild(build);

        final String testPomFile = "test-pom.xml";
        final Path   source      = Path.of(DummyProjectForTest.class.getResource("/" + testPomFile).getPath()).getParent();
        final Path   tmpDir      = Files.createDirectory(source.resolve("tmp-" + UUID.randomUUID()));
        final String target      = tmpDir.resolve(testPomFile).toString();

        copyResourceSingle(source, testPomFile, target);

        final File temp = source.resolve(target).toFile();
        project.setFile(temp);

        return project;
    }

}
