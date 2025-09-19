package bmc.dev.resources.code.bmcresources.config;

import java.util.List;

import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;

import bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector;
import bmc.dev.resources.code.support.DummyProjectForTest;
import bmc.dev.resources.code.support.InjectorResetForTest;

import static bmc.dev.resources.code.bmcresources.Constants.*;
import static bmc.dev.resources.code.bmcresources.io.IOUtilities.readAllLinesFromFile;
import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileWriter.writeMavenProperty;
import static bmc.dev.resources.code.bmcresources.utils.VersioningUtils.stampCurrentPluginVersion;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MavenConfigFileWriterTest extends InjectorResetForTest {

    @Test
    void writeMavenPropertyTest() {

        final MavenProject project               = DummyProjectForTest.createWithTestBaseDir();
        final String       resourcesCompleted    = MVN_PREFIX + PROP_COMPLETED_RESOURCE + "=true";
        final String       architectureCompleted = MVN_PREFIX + PROP_COMPLETED_ARCH + "=true";
        final String       versionStamp          = MVN_PREFIX + PROP_CREATED_WITH_VERSION + "=" + project.getPlugin(PLUGIN_KEY).getVersion();

        MavenProjectInjector.setMavenProject(project);

        writeMavenProperty(PROP_COMPLETED_ARCH, "true");
        writeMavenProperty(PROP_COMPLETED_RESOURCE, "true");
        stampCurrentPluginVersion();

        final List<String> strings = readAllLinesFromFile(project.getBasedir().toPath().resolve(".mvn", "maven.config"));

        assertEquals(3, strings.size());
        assertEquals(architectureCompleted, strings.get(0));
        assertEquals(resourcesCompleted, strings.get(1));
        assertEquals(versionStamp, strings.get(2));

    }

}
