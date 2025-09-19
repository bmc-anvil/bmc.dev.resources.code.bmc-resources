package bmc.dev.resources.code.support;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.apache.maven.model.Build;
import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;

import bmc.dev.resources.code.bmcresources.io.IOUtilities;
import lombok.SneakyThrows;

import static java.nio.file.Files.createDirectories;

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
        final Path   target      = tmpDir.resolve(testPomFile);

        createDirectories(target.getParent());

        IOUtilities.copySingleResource(source.toString(), target, testPomFile);

        final File temp = target.toFile();
        project.setFile(temp);

        return project;
    }

}
