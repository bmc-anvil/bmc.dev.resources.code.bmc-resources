package bmc.dev.resources.code.bmcresources.utils;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;

import bmc.dev.resources.code.support.DummyMojo;
import bmc.dev.resources.code.support.DummyProject;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class VersioningUtilsTest {

    @Test
    public void testHasPluginVersion_whenNoRecordedVersion_Changed_returnsTrue() {
        // Arrange
        final AbstractMojo mojo    = new DummyMojo();
        final MavenProject project = DummyProject.createWithTestBaseDir();

        // Act
        final Boolean result = VersioningUtils.hasPluginVersionChanged(mojo, project);

        // Assert
        // When no recorded version exists (readProperty returns empty),
        // the method should report "different version" as true.
        assertTrue(result);
    }

}
