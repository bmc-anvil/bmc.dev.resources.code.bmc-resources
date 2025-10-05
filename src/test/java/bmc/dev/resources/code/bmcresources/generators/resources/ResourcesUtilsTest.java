package bmc.dev.resources.code.bmcresources.generators.resources;

import java.net.URL;
import java.util.List;

import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import bmc.dev.resources.code.bmcresources.config.ResourceEntry;
import bmc.dev.resources.code.bmcresources.io.IOUtilities;
import bmc.dev.resources.code.bmcresources.io.OSUtilities;
import bmc.dev.resources.code.support.InjectorResetForTest;

import static bmc.dev.resources.code.bmcresources.generators.resources.ResourcesUtils.getJarUrl;
import static bmc.dev.resources.code.bmcresources.generators.resources.ResourcesUtils.processResourcesMap;
import static bmc.dev.resources.code.bmcresources.io.ConfigFileReader.readResourcesConfigFile;
import static bmc.dev.resources.code.bmcresources.io.IOUtilities.copyResourceFolder;
import static bmc.dev.resources.code.bmcresources.io.IOUtilities.copyResourceSingle;
import static bmc.dev.resources.code.bmcresources.io.OSUtilities.makeFileExecutable;
import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.setMavenProject;
import static bmc.dev.resources.code.support.ConstantsForTest.TEST_JAR_NAME;
import static bmc.dev.resources.code.support.ConstantsForTest.TEST_UPSTREAM_RESOURCES_CONFIG_FILE;
import static bmc.dev.resources.code.support.DummyProjectForTest.createWithTestBaseDir;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

class ResourcesUtilsTest extends InjectorResetForTest {

    private final URL testJarSupplier = this.getClass().getResource("/" + TEST_JAR_NAME);

    @Test
    void processResourcesMap_withAssortedEntries_shouldCallEveryMethodAccordingly() {

        final MavenProject        mavenProject  = createWithTestBaseDir();
        final List<ResourceEntry> userResources = readResourcesConfigFile(TEST_UPSTREAM_RESOURCES_CONFIG_FILE);
        setMavenProject(mavenProject);

        try (final MockedStatic<OSUtilities> mockedOSUtils = mockStatic(OSUtilities.class);
             final MockedStatic<IOUtilities> mockedIOUtils = mockStatic(IOUtilities.class);
             final MockedStatic<ResourcesUtils> mockedResourceUtils = mockStatic(ResourcesUtils.class)) {

            mockedResourceUtils.when(ResourcesUtils::getJarUrl).thenReturn(testJarSupplier);
            mockedResourceUtils.when(() -> processResourcesMap(any())).thenCallRealMethod();

            processResourcesMap(userResources);

            mockedIOUtils.verify(() -> copyResourceFolder(any(), any(), any()), times(1));
            mockedIOUtils.verify(() -> copyResourceSingle(any(), any(), any()), times(2));
            mockedOSUtils.verify(() -> makeFileExecutable(any()), times(1));
        }
    }

    @Test
    void setJarUrlSupplier_shouldReturnDefault() {

        final URL defaultJarUrl = ResourcesUtils.class.getProtectionDomain()
                                                      .getCodeSource()
                                                      .getLocation();

        assertEquals(defaultJarUrl, getJarUrl());
    }

}
