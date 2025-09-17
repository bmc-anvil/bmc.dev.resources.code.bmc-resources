package bmc.dev.resources.code.bmcresources.properties;

import org.apache.maven.model.Build;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;

import bmc.dev.resources.code.bmcresources.maven.MavenConfigFileWriter;
import bmc.dev.resources.code.bmcresources.utils.VersioningUtils;
import bmc.dev.resources.code.support.DummyMojo;

import static bmc.dev.resources.code.bmcresources.Constants.PROP_COMPLETED_ARCH;
import static bmc.dev.resources.code.bmcresources.Constants.PROP_COMPLETED_RESOURCE;

class MavenConfigFileWriterTest {

    @Test
    void writeMavenPropertyTest() {
        // Arrange
        final AbstractMojo mojo    = new DummyMojo();
        final MavenProject project = new MavenProject();
        final Build        build   = new Build();

        final Plugin plugin = new Plugin();
        plugin.setGroupId("bmc.dev.resources.code");
        plugin.setArtifactId("bmc-resources");
        plugin.setVersion("bmc-local"); // or any version string you wish to validate

        build.addPlugin(plugin);
        project.setBuild(build);

        // Act
        MavenConfigFileWriter.writeMavenProperty(mojo, PROP_COMPLETED_ARCH, "true", project);
        MavenConfigFileWriter.writeMavenProperty(mojo, PROP_COMPLETED_RESOURCE, "true", project);
        VersioningUtils.stampCurrentPluginVersion(mojo, project);
    }

}
