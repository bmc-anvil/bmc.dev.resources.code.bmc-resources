package bmc.dev.resources.code.bmcresources.utils;

import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import bmc.dev.resources.code.bmcresources.Constants;
import bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector;
import bmc.dev.resources.code.support.DummyProject;

import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileWriter.writeMavenProperty;
import static bmc.dev.resources.code.bmcresources.utils.VersioningUtils.hasPluginVersionChanged;
import static bmc.dev.resources.code.bmcresources.utils.VersioningUtils.stampCurrentPluginVersion;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VersioningUtilsTest {

    @BeforeAll
    public static void setup() {

        final MavenProject project = DummyProject.createWithTestBaseDir();
        MavenProjectInjector.setMavenProject(project);
    }

    @Test
    public void testCurrentVersionExists_returnsFalse() {

        stampCurrentPluginVersion();

        final Boolean result = hasPluginVersionChanged();

        assertFalse(result);
    }

    @Test
    public void testDifferentVersionExists_returnsTrue() {

        writeMavenProperty(Constants.PROP_CREATED_WITH_VERSION, "=poteyto-potAHto");

        final Boolean result = hasPluginVersionChanged();

        assertTrue(result);
    }

    @Test
    public void testNoVersionExists_returnsTrue() {

        final Boolean result = hasPluginVersionChanged();

        assertTrue(result);
    }

}
