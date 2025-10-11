package bmc.dev.resources.code.bmcresources.mojos;

import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import bmc.dev.resources.code.bmcresources.config.ArchitectureConfig;
import bmc.dev.resources.code.bmcresources.config.GeneralConfig;
import bmc.dev.resources.code.bmcresources.config.ResourcesConfig;
import bmc.dev.resources.code.bmcresources.generators.archdesign.ArchitectureExecution;
import bmc.dev.resources.code.bmcresources.generators.resources.ResourcesExecution;
import bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector;
import bmc.dev.resources.code.bmcresources.utils.VersioningUtils;
import bmc.dev.resources.code.support.InjectorResetForTest;

import static bmc.dev.resources.code.bmcresources.generators.archdesign.ArchitectureExecution.createArchitecture;
import static bmc.dev.resources.code.bmcresources.generators.resources.ResourcesExecution.createResources;
import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.injectMavenProject;
import static bmc.dev.resources.code.support.DummyProjectForTest.createWithTestBaseDir;
import static org.mockito.Mockito.*;

class ArchResourcesGeneratorMojoTest extends InjectorResetForTest {

    @Test
    void execute_withSkipFalseAndPluginVersionChanged_shouldCreateArchitectureAndResources() {

        final MavenProject       project            = createWithTestBaseDir();
        final ArchitectureConfig architectureConfig = new ArchitectureConfig();
        final ResourcesConfig    resourcesConfig    = new ResourcesConfig();
        final GeneralConfig      generalConfig      = new GeneralConfig();

        generalConfig.setSkip(false);

        final ArchResourcesGeneratorMojo mojo = spy(new ArchResourcesGeneratorMojo());
        mojo.setMavenProject(project);
        mojo.setGeneralConfig(generalConfig);
        mojo.setArchitecture(architectureConfig);
        mojo.setResources(resourcesConfig);

        try (final MockedStatic<ResourcesExecution> mockedResourcesExecution = mockStatic(ResourcesExecution.class);
             final MockedStatic<ArchitectureExecution> mockedArchitectureExecution = mockStatic(ArchitectureExecution.class);
             final MockedStatic<VersioningUtils> mockedVersioningUtils = mockStatic(VersioningUtils.class);
             final MockedStatic<MavenProjectInjector> mockedMavenProjectInjector = mockStatic(MavenProjectInjector.class)) {

            mockedVersioningUtils.when(VersioningUtils::hasPluginVersionChanged).thenReturn(true);

            mojo.execute();

            mockedMavenProjectInjector.verify(() -> injectMavenProject(project));
            mockedResourcesExecution.verify(() -> createResources(resourcesConfig));
            mockedArchitectureExecution.verify(() -> createArchitecture(architectureConfig));
            mockedVersioningUtils.verify(VersioningUtils::stampCurrentPluginVersion);
        }
    }

    @Test
    void execute_withSkipFalseAndPluginVersionNotChanged_shouldNotCreateAnything() {

        final MavenProject       project            = createWithTestBaseDir();
        final ArchitectureConfig architectureConfig = new ArchitectureConfig();
        final ResourcesConfig    resourcesConfig    = new ResourcesConfig();
        final GeneralConfig      generalConfig      = new GeneralConfig();

        generalConfig.setSkip(false);

        final ArchResourcesGeneratorMojo mojo = spy(new ArchResourcesGeneratorMojo());
        mojo.setMavenProject(project);
        mojo.setGeneralConfig(generalConfig);
        mojo.setArchitecture(architectureConfig);
        mojo.setResources(resourcesConfig);

        try (final MockedStatic<ResourcesExecution> mockedResourcesExecution = mockStatic(ResourcesExecution.class);
             final MockedStatic<ArchitectureExecution> mockedArchitectureExecution = mockStatic(ArchitectureExecution.class);
             final MockedStatic<VersioningUtils> mockedVersioningUtils = mockStatic(VersioningUtils.class);
             final MockedStatic<MavenProjectInjector> mockedMavenProjectInjector = mockStatic(MavenProjectInjector.class)) {

            mockedVersioningUtils.when(VersioningUtils::hasPluginVersionChanged).thenReturn(false);

            mojo.execute();
            mockedMavenProjectInjector.verify(() -> injectMavenProject(project));
            mockedResourcesExecution.verifyNoInteractions();
            mockedArchitectureExecution.verifyNoInteractions();
            mockedVersioningUtils.verify(VersioningUtils::hasPluginVersionChanged);
            mockedVersioningUtils.verify(VersioningUtils::stampCurrentPluginVersion, never());
        }
    }

    @Test
    void execute_withSkipTrue_shouldNotCreateAnything() {

        final MavenProject       project            = createWithTestBaseDir();
        final ArchitectureConfig architectureConfig = new ArchitectureConfig();
        final ResourcesConfig    resourcesConfig    = new ResourcesConfig();
        final GeneralConfig      generalConfig      = new GeneralConfig();

        generalConfig.setSkip(true);

        final ArchResourcesGeneratorMojo mojo = spy(new ArchResourcesGeneratorMojo());
        mojo.setMavenProject(project);
        mojo.setGeneralConfig(generalConfig);
        mojo.setArchitecture(architectureConfig);
        mojo.setResources(resourcesConfig);

        try (final MockedStatic<ResourcesExecution> mockedResourcesExecution = mockStatic(ResourcesExecution.class);
             final MockedStatic<ArchitectureExecution> mockedArchitectureExecution = mockStatic(ArchitectureExecution.class);
             final MockedStatic<VersioningUtils> mockedVersioningUtils = mockStatic(VersioningUtils.class);
             final MockedStatic<MavenProjectInjector> mockedMavenProjectInjector = mockStatic(MavenProjectInjector.class)) {

            mojo.execute();

            mockedMavenProjectInjector.verify(() -> injectMavenProject(project));
            mockedResourcesExecution.verifyNoInteractions();
            mockedArchitectureExecution.verifyNoInteractions();
            mockedVersioningUtils.verifyNoInteractions();
        }
    }

}
