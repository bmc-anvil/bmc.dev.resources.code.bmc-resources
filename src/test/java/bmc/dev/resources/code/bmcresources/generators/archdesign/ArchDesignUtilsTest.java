package bmc.dev.resources.code.bmcresources.generators.archdesign;

import java.nio.file.Path;
import java.util.List;

import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bmc.dev.resources.code.bmcresources.config.ArchitectureConfig;
import bmc.dev.resources.code.bmcresources.config.ArchitectureEntry;
import bmc.dev.resources.code.bmcresources.io.IOUtilities;
import bmc.dev.resources.code.support.InjectorResetForTest;

import static bmc.dev.resources.code.bmcresources.generators.archdesign.ArchDesignUtils.*;
import static bmc.dev.resources.code.bmcresources.io.IOUtilities.copyResourceFile;
import static bmc.dev.resources.code.bmcresources.io.IOUtilities.createDirectoriesSafely;
import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.setMavenProject;
import static bmc.dev.resources.code.bmcresources.utils.BMCConfigFileUtils.calculateLeftAlignedPadding;
import static bmc.dev.resources.code.support.DummyProjectForTest.createWithTestBaseDir;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

@TestMethodOrder(OrderAnnotation.class)
class ArchDesignUtilsTest extends InjectorResetForTest {

    @Test
    void buildBaseTargetPathForArch_shouldCreateThePathAsPerMavenProjectConfiguration() {

        final MavenProject project         = createWithTestBaseDir();
        final String       artifactId      = project.getArtifactId().replace("-", "");
        final String       groupId         = project.getGroupId().replace(".", "/");
        final String       sourceDirectory = project.getBuild().getSourceDirectory();
        final Path         expectedPath    = Path.of(sourceDirectory, groupId, artifactId);

        setMavenProject(project);

        final Path baseTargetPathForArch = buildBaseTargetPathForArch();

        assertEquals(expectedPath, baseTargetPathForArch);

    }

    @SuppressWarnings("checkstyle:MethodLength")
    @Test
    @Order(MIN_VALUE)
    void logArchitectureConfiguration_shouldCorrectlyLogConfiguration() {

        final MavenProject            project              = createWithTestBaseDir();
        final ArchitectureConfig      architectureConfig   = new ArchitectureConfig();
        final Logger                  mockedLogger         = mock(Logger.class);
        final ArgumentCaptor<String>  messageCaptorString  = forClass(String.class);
        final ArgumentCaptor<Boolean> messageCaptorBoolean = forClass(Boolean.class);
        final ArgumentCaptor<Path>    messageCaptorPath    = forClass(Path.class);

        architectureConfig.setModel("test_model");
        architectureConfig.setMainReadme("test_mainReadme.md");
        architectureConfig.setSkip(true);
        architectureConfig.setSkipReadme(false);

        setMavenProject(project);

        try (final MockedStatic<LoggerFactory> mockedLoggerFactory = mockStatic(org.slf4j.LoggerFactory.class)) {
            mockedLoggerFactory.when(() -> LoggerFactory.getLogger(ArchDesignUtils.class)).thenReturn(mockedLogger);

            logArchitectureConfiguration(architectureConfig);

            verify(mockedLogger, times(2)).info(anyString());
            verify(mockedLogger, times(2)).info(anyString(), anyBoolean());
            verify(mockedLogger, times(5)).info(anyString(), anyString());
            verify(mockedLogger, times(1)).info(anyString(), any(Path.class));

            verify(mockedLogger, atLeastOnce()).info(messageCaptorString.capture());
            verify(mockedLogger, atLeastOnce()).info(messageCaptorString.capture(), messageCaptorString.capture());
            verify(mockedLogger, atLeastOnce()).info(messageCaptorString.capture(), messageCaptorBoolean.capture());
            verify(mockedLogger, atLeastOnce()).info(messageCaptorString.capture(), messageCaptorPath.capture());

            assertTrue(messageCaptorString.getAllValues().stream().anyMatch(message -> message.contains("Architecture Structure configuration")));
            assertTrue(messageCaptorString.getAllValues().stream().anyMatch(String::isEmpty));

            assertEquals(1, messageCaptorBoolean.getAllValues().stream().filter(trueValues -> trueValues.equals(Boolean.TRUE)).count());
            assertEquals(1, messageCaptorBoolean.getAllValues().stream().filter(falseValues -> falseValues.equals(Boolean.FALSE)).count());

            assertTrue(messageCaptorString.getAllValues().stream().anyMatch(message -> message.contains(architectureConfig.getModel())));
            assertTrue(messageCaptorString.getAllValues().stream().anyMatch(message -> message.contains(architectureConfig.getMainReadme())));
            assertTrue(messageCaptorString.getAllValues().stream().anyMatch(message -> message.contains(project.getArtifactId())));
            assertTrue(messageCaptorString.getAllValues().stream().anyMatch(message -> message.contains(project.getGroupId())));
            assertTrue(messageCaptorString.getAllValues().stream().anyMatch(message -> message.contains(project.getBuild().getSourceDirectory())));

            assertTrue(messageCaptorPath.getAllValues().stream().anyMatch(message -> message.equals(buildBaseTargetPathForArch())));
        }

    }

    @Test
    void processArchitectureAndReadme_shouldProcessDirectoriesAndReadmes() {

        final MavenProject       project            = createWithTestBaseDir();
        final ArchitectureConfig architectureConfig = new ArchitectureConfig();
        final List<ArchitectureEntry> architectureEntryList = List.of(new ArchitectureEntry("folder", of("readme")),
                                                                      new ArchitectureEntry("folder2", of("readme2")));
        final String padding = calculateLeftAlignedPadding.apply("folder".length());

        architectureConfig.setModel("test_model");
        architectureConfig.setMainReadme("test_mainReadme.md");
        architectureConfig.setSkip(true);
        architectureConfig.setSkipReadme(false);
        setMavenProject(project);

        final Path path = buildBaseTargetPathForArch();

        try (final MockedStatic<IOUtilities> mockedIOUtilities = mockStatic(IOUtilities.class);
             final MockedStatic<ArchDesignUtils> mockedArchDesignUtils = mockStatic(ArchDesignUtils.class)) {

            mockedArchDesignUtils.when(ArchDesignUtils::buildBaseTargetPathForArch).thenReturn(path);
            mockedArchDesignUtils.when(() -> processArchitectureAndReadme(any(), any(), any())).thenCallRealMethod();

            processArchitectureAndReadme(architectureConfig, architectureEntryList, padding);

            mockedIOUtilities.verify(() -> createDirectoriesSafely(any()), times(architectureEntryList.size()));
            mockedArchDesignUtils.verify(() -> processReadme(any(), any(), any()), times(architectureEntryList.size() + 1));
        }
    }

    @Test
    void processArchitectureOnly_shouldProcessDirectoriesOnly() {

        final MavenProject       project            = createWithTestBaseDir();
        final ArchitectureConfig architectureConfig = new ArchitectureConfig();
        final List<ArchitectureEntry> architectureEntryList = List.of(new ArchitectureEntry("folder", of("readme")),
                                                                      new ArchitectureEntry("folder2", of("readme2")));
        final String padding = calculateLeftAlignedPadding.apply("folder".length());

        architectureConfig.setModel("test_model");
        architectureConfig.setMainReadme("test_mainReadme.md");
        architectureConfig.setSkip(true);
        architectureConfig.setSkipReadme(false);
        setMavenProject(project);

        final Path path = buildBaseTargetPathForArch();

        try (final MockedStatic<IOUtilities> mockedIOUtilities = mockStatic(IOUtilities.class);
             final MockedStatic<ArchDesignUtils> mockedArchDesignUtils = mockStatic(ArchDesignUtils.class)) {

            mockedArchDesignUtils.when(ArchDesignUtils::buildBaseTargetPathForArch).thenReturn(path);
            mockedArchDesignUtils.when(() -> processArchitectureOnly(any(), any())).thenCallRealMethod();

            processArchitectureOnly(architectureEntryList, padding);

            mockedIOUtilities.verify(() -> createDirectoriesSafely(any()), times(architectureEntryList.size()));
            mockedArchDesignUtils.verify(() -> processReadme(any(), any(), any()), times(0));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"    ", ""})
    @NullSource
    void processReadme_withABlankReadMeFile_shouldProcessTheFile(final String readmeFile) {

        final MavenProject       project            = createWithTestBaseDir();
        final ArchitectureConfig architectureConfig = new ArchitectureConfig();

        architectureConfig.setModel("test_model");
        architectureConfig.setMainReadme("test_mainReadme.md");
        architectureConfig.setSkip(true);
        architectureConfig.setSkipReadme(false);
        setMavenProject(project);

        final Path path = buildBaseTargetPathForArch();

        try (final MockedStatic<IOUtilities> mockedIOUtilities = mockStatic(IOUtilities.class);
             final MockedStatic<ArchDesignUtils> mockedArchDesignUtils = mockStatic(ArchDesignUtils.class)) {

            mockedArchDesignUtils.when(ArchDesignUtils::buildBaseTargetPathForArch).thenReturn(path);
            mockedArchDesignUtils.when(() -> processReadme(any(), any(), any())).thenCallRealMethod();

            processReadme(architectureConfig, "", readmeFile);

            mockedIOUtilities.verify(() -> copyResourceFile(any(), any(), any()), times(0));
        }
    }

    @Test
    void processReadme_withAReadMeFileAndBlankTarget_shouldProcessTheFile() {

        final MavenProject       project            = createWithTestBaseDir();
        final ArchitectureConfig architectureConfig = new ArchitectureConfig();

        architectureConfig.setModel("test_model");
        architectureConfig.setMainReadme("test_mainReadme.md");
        architectureConfig.setSkip(true);
        architectureConfig.setSkipReadme(false);
        setMavenProject(project);

        final Path path = buildBaseTargetPathForArch();

        try (final MockedStatic<IOUtilities> mockedIOUtilities = mockStatic(IOUtilities.class);
             final MockedStatic<ArchDesignUtils> mockedArchDesignUtils = mockStatic(ArchDesignUtils.class)) {

            mockedArchDesignUtils.when(ArchDesignUtils::buildBaseTargetPathForArch).thenReturn(path);
            mockedArchDesignUtils.when(() -> processReadme(any(), any(), any())).thenCallRealMethod();

            processReadme(architectureConfig, "", "test_readme");

            mockedIOUtilities.verify(() -> copyResourceFile(any(), any(), any()), times(1));
        }
    }

    @Test
    void processReadme_withAReadMeFileAndNonBlankTarget_shouldProcessTheFile() {

        final MavenProject       project            = createWithTestBaseDir();
        final ArchitectureConfig architectureConfig = new ArchitectureConfig();

        architectureConfig.setModel("test_model");
        architectureConfig.setMainReadme("test_mainReadme.md");
        architectureConfig.setSkip(true);
        architectureConfig.setSkipReadme(false);
        setMavenProject(project);

        final Path path = buildBaseTargetPathForArch();

        try (final MockedStatic<IOUtilities> mockedIOUtilities = mockStatic(IOUtilities.class);
             final MockedStatic<ArchDesignUtils> mockedArchDesignUtils = mockStatic(ArchDesignUtils.class)) {

            mockedArchDesignUtils.when(ArchDesignUtils::buildBaseTargetPathForArch).thenReturn(path);
            mockedArchDesignUtils.when(() -> processReadme(any(), any(), any())).thenCallRealMethod();

            processReadme(architectureConfig, "target", "test_readme");

            mockedIOUtilities.verify(() -> copyResourceFile(any(), any(), any()), times(1));
        }
    }

}
