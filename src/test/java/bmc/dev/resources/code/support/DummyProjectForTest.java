package bmc.dev.resources.code.support;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;

import org.apache.maven.model.Build;
import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.io.TempDir;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.io.IOUtilities.copyResourceSingle;
import static java.nio.file.Files.createDirectory;
import static java.util.Objects.requireNonNull;
import static java.util.UUID.randomUUID;

@Slf4j
public class DummyProjectForTest {

    /**
     * Creates a {@link MavenProject} configure with a test-pom.xml and a baseDir.
     * <p>
     * The test-pom.xml file is a resource at the root of the test resources.
     * <p>
     * Why not using {@link TempDir}?
     * <br>Tempdir is fine to use it here and there, but in this case I want to have easier control of filepaths for easier debugging.
     * <br>Though possible to change the location of the {@link TempDir}, it is done in a peculiar way.
     * <br>Given that I frequently need a Dummy MavenProject, this approach encapsulates all that is required in a single call.
     *
     * @return the Configured MavenProject
     */
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

        final String testPomFile     = "test-pom.xml";
        final URL    pomFileResource = DummyProjectForTest.class.getResource("/" + testPomFile);
        final Path   basePath        = Path.of(requireNonNull(pomFileResource).getPath()).getParent();
        final Path   tempDirPath     = createDirectory(basePath.resolve("tmp-" + randomUUID()));
        final String target          = tempDirPath.resolve(testPomFile).toString();

        copyResourceSingle(basePath, testPomFile, target);

        final File temp = basePath.resolve(target).toFile();
        project.setFile(temp);

        log.info("Created project for this test with path:\n[{}]", tempDirPath);

        return project;
    }

}
