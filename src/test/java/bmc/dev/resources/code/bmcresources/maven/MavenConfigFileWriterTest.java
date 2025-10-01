package bmc.dev.resources.code.bmcresources.maven;

import java.util.List;

import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;

import bmc.dev.resources.code.support.InjectorResetForTest;

import static bmc.dev.resources.code.bmcresources.Constants.*;
import static bmc.dev.resources.code.bmcresources.io.IOUtilities.readAllLinesFromFile;
import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileWriter.writeMavenProperty;
import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.setMavenProject;
import static bmc.dev.resources.code.bmcresources.utils.VersioningUtils.stampCurrentPluginVersion;
import static bmc.dev.resources.code.support.DummyProjectForTest.createWithTestBaseDir;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MavenConfigFileWriterTest extends InjectorResetForTest {

    @Test
    void writeAllProperties_modifyingExistingProperty_propertyIsModifiedInPlaceRestIsUntouched() {

        final MavenProject project                  = createWithTestBaseDir();
        final String       resourcesNotCompleted    = MVN_PREFIX + PROP_COMPLETED_RESOURCE + "=false";
        final String       architectureNotCompleted = MVN_PREFIX + PROP_COMPLETED_ARCH + "=false";
        final String       property_0               = "test.property.0";
        final String       property_1               = "test.property.1";
        final String       property_2               = "test.property.2";
        setMavenProject(project);

        writeMavenProperty(property_0, "00");
        writeMavenProperty(PROP_COMPLETED_ARCH, "true");
        writeMavenProperty(property_1, "01");
        stampCurrentPluginVersion();
        writeMavenProperty(PROP_COMPLETED_RESOURCE, "true");
        writeMavenProperty(property_2, "02");

        // rewrite an existing prop to test inplace modification
        writeMavenProperty(PROP_COMPLETED_ARCH, "false");
        writeMavenProperty(PROP_COMPLETED_RESOURCE, "false");

        final List<String> strings = readAllLinesFromFile(project.getBasedir().toPath().resolve(".mvn", "maven.config"));

        assertEquals(architectureNotCompleted, strings.get(1));
        assertEquals(resourcesNotCompleted, strings.get(4));
    }

    @Test
    void writeAllProperties_readsAllProperties() {

        final MavenProject project               = createWithTestBaseDir();
        final String       resourcesCompleted    = MVN_PREFIX + PROP_COMPLETED_RESOURCE + "=true";
        final String       architectureCompleted = MVN_PREFIX + PROP_COMPLETED_ARCH + "=true";
        final String       versionStamp          = MVN_PREFIX + PROP_CREATED_WITH_VERSION + "=" + project.getPlugin(PLUGIN_KEY).getVersion();

        setMavenProject(project);

        writeMavenProperty(PROP_COMPLETED_ARCH, "true");
        writeMavenProperty(PROP_COMPLETED_RESOURCE, "true");
        stampCurrentPluginVersion();

        final List<String> strings = readAllLinesFromFile(project.getBasedir().toPath().resolve(".mvn", "maven.config"));

        assertEquals(3, strings.size());
        assertEquals(architectureCompleted, strings.get(0));
        assertEquals(resourcesCompleted, strings.get(1));
        assertEquals(versionStamp, strings.get(2));
    }

    @Test
    void writeAllProperties_withAlreadyExistingProperties_readsAllPropertiesAndExistingAreUntouched() {

        final MavenProject project               = createWithTestBaseDir();
        final String       resourcesCompleted    = MVN_PREFIX + PROP_COMPLETED_RESOURCE + "=true";
        final String       architectureCompleted = MVN_PREFIX + PROP_COMPLETED_ARCH + "=true";
        final String       versionStamp          = MVN_PREFIX + PROP_CREATED_WITH_VERSION + "=" + project.getPlugin(PLUGIN_KEY).getVersion();
        final String       property_0            = "test.property.0";
        final String       property_1            = "test.property.1";
        final String       property_2            = "test.property.2";
        setMavenProject(project);

        writeMavenProperty(property_0, "00");
        writeMavenProperty(PROP_COMPLETED_ARCH, "true");
        writeMavenProperty(PROP_COMPLETED_RESOURCE, "true");
        writeMavenProperty(property_1, "01");
        stampCurrentPluginVersion();
        writeMavenProperty(property_2, "02");

        final List<String> strings = readAllLinesFromFile(project.getBasedir().toPath().resolve(".mvn", "maven.config"));

        assertEquals(6, strings.size());
        assertEquals(MVN_PREFIX + property_0 + "=00", strings.get(0));
        assertEquals(architectureCompleted, strings.get(1));
        assertEquals(resourcesCompleted, strings.get(2));
        assertEquals(MVN_PREFIX + property_1 + "=01", strings.get(3));
        assertEquals(versionStamp, strings.get(4));
        assertEquals(MVN_PREFIX + property_2 + "=02", strings.get(5));
    }

}
