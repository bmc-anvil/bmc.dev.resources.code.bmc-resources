package bmc.dev.resources.code.bmcresources.generators.resources;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import bmc.dev.resources.code.bmcresources.config.ResourceEntry;
import bmc.dev.resources.code.bmcresources.io.IOUtilities;
import bmc.dev.resources.code.bmcresources.io.OSUtilities;
import bmc.dev.resources.code.support.InjectorResetForTest;

import static bmc.dev.resources.code.bmcresources.generators.resources.ResourcesUtils.getJarUrl;
import static bmc.dev.resources.code.bmcresources.generators.resources.ResourcesUtils.processResourceEntries;
import static bmc.dev.resources.code.bmcresources.io.ConfigFileReader.extractConfigFileEntries;
import static bmc.dev.resources.code.bmcresources.io.IOUtilities.copyResourceFile;
import static bmc.dev.resources.code.bmcresources.io.IOUtilities.copyResourceFolder;
import static bmc.dev.resources.code.bmcresources.io.OSUtilities.makeFileExecutable;
import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.setMavenProject;
import static bmc.dev.resources.code.bmcresources.utils.BMCConfigFileUtils.extractResources;
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
    void processResourceEntries_withAssortedEntries_shouldCallEveryMethodAccordingly() {

        final MavenProject        mavenProject  = createWithTestBaseDir();
        final List<ResourceEntry> userResources = extractConfigFileEntries(TEST_UPSTREAM_RESOURCES_CONFIG_FILE, extractResources).orElseGet(ArrayList::new);
        setMavenProject(mavenProject);

        try (final MockedStatic<OSUtilities> mockedOSUtils = mockStatic(OSUtilities.class);
             final MockedStatic<IOUtilities> mockedIOUtils = mockStatic(IOUtilities.class);
             final MockedStatic<ResourcesUtils> mockedResourceUtils = mockStatic(ResourcesUtils.class)) {

            mockedResourceUtils.when(ResourcesUtils::getJarUrl).thenReturn(testJarSupplier);
            mockedResourceUtils.when(() -> processResourceEntries(any())).thenCallRealMethod();

            processResourceEntries(userResources);

            mockedIOUtils.verify(() -> copyResourceFolder(any(), any(), any()), times(1));
            mockedIOUtils.verify(() -> copyResourceFile(any(), any(), any()), times(2));
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
