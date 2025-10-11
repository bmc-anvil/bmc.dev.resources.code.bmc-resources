package bmc.dev.resources.code.bmcresources.utils;

import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;

import bmc.dev.resources.code.bmcresources.Constants;
import bmc.dev.resources.code.support.DummyProjectForTest;
import bmc.dev.resources.code.support.InjectorResetForTest;

import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileWriter.writeMavenProperty;
import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.injectMavenProject;
import static bmc.dev.resources.code.bmcresources.utils.VersioningUtils.hasPluginVersionChanged;
import static bmc.dev.resources.code.bmcresources.utils.VersioningUtils.stampCurrentPluginVersion;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VersioningUtilsTest extends InjectorResetForTest {

    @Test
    public void currentVersionExists_returnsFalse() {

        final MavenProject project = DummyProjectForTest.createWithTestBaseDir();
        injectMavenProject(project);

        stampCurrentPluginVersion();

        final Boolean result = hasPluginVersionChanged();

        assertFalse(result);
    }

    @Test
    public void differentVersionExists_returnsTrue() {

        final MavenProject project = DummyProjectForTest.createWithTestBaseDir();
        injectMavenProject(project);

        writeMavenProperty(Constants.PROP_CREATED_WITH_VERSION, "=poteyto-potAHto");

        final Boolean result = hasPluginVersionChanged();

        assertTrue(result);
    }

    @Test
    public void noVersionExists_returnsTrue() {

        final MavenProject project = DummyProjectForTest.createWithTestBaseDir();
        injectMavenProject(project);

        final Boolean result = hasPluginVersionChanged();

        assertTrue(result);
    }

}
