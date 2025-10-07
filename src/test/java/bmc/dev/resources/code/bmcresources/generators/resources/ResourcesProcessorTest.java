package bmc.dev.resources.code.bmcresources.generators.resources;

import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import bmc.dev.resources.code.bmcresources.config.ResourceEntry;
import bmc.dev.resources.code.bmcresources.config.ResourcesConfig;
import bmc.dev.resources.code.bmcresources.io.ConfigFileReader;
import bmc.dev.resources.code.support.InjectorResetForTest;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.Constants.*;
import static bmc.dev.resources.code.bmcresources.generators.resources.ResourcesProcessor.processResources;
import static bmc.dev.resources.code.bmcresources.generators.resources.ResourcesUtils.processResourceEntries;
import static bmc.dev.resources.code.bmcresources.io.ConfigFileReader.extractConfigFileEntries;
import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.setMavenProject;
import static bmc.dev.resources.code.bmcresources.utils.BMCConfigFileUtils.extractResources;
import static bmc.dev.resources.code.support.ConstantsForTest.*;
import static bmc.dev.resources.code.support.DummyProjectForTest.createWithTestBaseDir;
import static java.lang.System.getProperties;
import static java.nio.file.Files.exists;
import static java.util.Optional.ofNullable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

@Slf4j
class ResourcesProcessorTest extends InjectorResetForTest {

    private final URL testJarSupplier = this.getClass().getResource("/" + TEST_JAR_NAME);

    @Test
    void processResourcesMap_withNoExistingResources_shouldWriteAllResources() {

        final MavenProject mavenProject = createWithTestBaseDir();
        final Path         testBasePath = mavenProject.getBasedir().toPath();
        setMavenProject(mavenProject);

        final ResourcesConfig resourcesConfig = new ResourcesConfig();
        getProperties().setProperty(PROP_COMPLETED_RESOURCE, "false");

        final Optional<List<ResourceEntry>> upstreamResources = extractConfigFileEntries(TEST_UPSTREAM_RESOURCES_CONFIG_FILE, extractResources);
        final Optional<List<ResourceEntry>> userResources     = extractConfigFileEntries(TEST_USER_RESOURCES_CONFIG_FILE, extractResources);

        checkResourcesExistence(upstreamResources.orElseThrow(), testBasePath, Assertions::assertFalse);
        checkResourcesExistence(userResources.orElseGet(ArrayList::new), testBasePath, Assertions::assertFalse);

        mockClassesAndProcessResources(upstreamResources.orElseGet(ArrayList::new), userResources.orElseGet(ArrayList::new), testJarSupplier, resourcesConfig);

        checkResourcesExistence(upstreamResources.orElseGet(ArrayList::new), testBasePath, Assertions::assertTrue);
        checkResourcesExistence(userResources.orElseGet(ArrayList::new), testBasePath, Assertions::assertTrue);
    }

    @Test
    void processResourcesMap_withOverwriteFalseAndResourcesCompleted_shouldSkipWritingUserResources() {

        final MavenProject mavenProject = createWithTestBaseDir();
        final Path         testBasePath = mavenProject.getBasedir().toPath();
        setMavenProject(mavenProject);

        final ResourcesConfig resourcesConfig = new ResourcesConfig();
        resourcesConfig.setOverwriteUserResources(false);
        getProperties().setProperty(PROP_COMPLETED_RESOURCE, "true");

        final Optional<List<ResourceEntry>> upstreamResources = extractConfigFileEntries(TEST_UPSTREAM_RESOURCES_CONFIG_FILE, extractResources);
        final Optional<List<ResourceEntry>> userResources     = extractConfigFileEntries(TEST_USER_RESOURCES_CONFIG_FILE, extractResources);

        mockClassesAndProcessResources(upstreamResources.orElseGet(ArrayList::new), userResources.orElseGet(ArrayList::new), testJarSupplier, resourcesConfig);

        checkResourcesExistence(upstreamResources.orElseGet(ArrayList::new), testBasePath, Assertions::assertTrue);
        checkResourcesExistence(userResources.orElseGet(ArrayList::new), testBasePath, Assertions::assertFalse);
    }

    @Test
    void processResourcesMap_withOverwriteTrueAndResourcesCompleted_shouldWriteAllResources() {

        final MavenProject mavenProject = createWithTestBaseDir();
        final Path         testBasePath = mavenProject.getBasedir().toPath();
        setMavenProject(mavenProject);

        final ResourcesConfig resourcesConfig = new ResourcesConfig();
        resourcesConfig.setOverwriteUserResources(true);
        getProperties().setProperty(PROP_COMPLETED_RESOURCE, "true");

        final Optional<List<ResourceEntry>> upstreamResources = extractConfigFileEntries(TEST_UPSTREAM_RESOURCES_CONFIG_FILE, extractResources);
        final Optional<List<ResourceEntry>> userResources     = extractConfigFileEntries(TEST_USER_RESOURCES_CONFIG_FILE, extractResources);

        mockClassesAndProcessResources(upstreamResources.orElseGet(ArrayList::new), userResources.orElseGet(ArrayList::new), testJarSupplier, resourcesConfig);

        checkResourcesExistence(upstreamResources.orElseGet(ArrayList::new), testBasePath, Assertions::assertTrue);
        checkResourcesExistence(userResources.orElseGet(ArrayList::new), testBasePath, Assertions::assertTrue);
    }

    private static void checkResourcesExistence(final List<ResourceEntry> resourceEntries, final Path mavenProject, final Consumer<Boolean> assertion) {

        resourceEntries.stream().map(ResourceEntry::target).forEach(resourceTarget -> {
            log.info("Checking resource existence [{}]", resourceTarget);
            assertion.accept(exists(mavenProject.resolve(resourceTarget)));
        });
    }

    private static void mockClassesAndProcessResources(final List<ResourceEntry> upstreamResources, final List<ResourceEntry> userResources,
            final URL testJarSupplier, final ResourcesConfig resourcesConfig) {

        try (final MockedStatic<ConfigFileReader> mockedConfigReader = mockStatic(ConfigFileReader.class);
             final MockedStatic<ResourcesUtils> mockedResourceUtils = mockStatic(ResourcesUtils.class)) {
            mockedConfigReader.when(() -> extractConfigFileEntries(FILE_RESOURCES_UPSTREAM, extractResources)).thenReturn(ofNullable(upstreamResources));
            mockedConfigReader.when(() -> extractConfigFileEntries(FILE_RESOURCES_USER, extractResources)).thenReturn(ofNullable(userResources));
            mockedResourceUtils.when(() -> processResourceEntries(any())).thenCallRealMethod();
            mockedResourceUtils.when(ResourcesUtils::getJarUrl).thenReturn(testJarSupplier);

            processResources(resourcesConfig);
        }
    }

}
