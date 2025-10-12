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
import bmc.dev.resources.code.support.InjectorResetForTest;

import static bmc.dev.resources.code.bmcresources.generators.archdesign.ArchitectureExecution.createArchitecture;
import static bmc.dev.resources.code.bmcresources.generators.resources.ResourcesExecution.createResources;
import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.injectMavenProject;
import static bmc.dev.resources.code.support.DummyProjectForTest.createWithTestBaseDir;
import static org.mockito.Mockito.mockStatic;

class ArchResourcesGeneratorMojoTest extends InjectorResetForTest {

    @Test
    void execute_withSkipFalse_shouldCreateArchitectureAndResources() {

        final MavenProject               project = createWithTestBaseDir();
        final ArchResourcesGeneratorMojo mojo    = getArchResourcesGeneratorMojo(project);

        mojo.getGeneralConfig().setSkip(false);

        try (final MockedStatic<ResourcesExecution> mockedResourcesExecution = mockStatic(ResourcesExecution.class);
             final MockedStatic<ArchitectureExecution> mockedArchitectureExecution = mockStatic(ArchitectureExecution.class);
             final MockedStatic<MavenProjectInjector> mockedMavenProjectInjector = mockStatic(MavenProjectInjector.class)) {

            mojo.execute();

            mockedMavenProjectInjector.verify(() -> injectMavenProject(project));
            mockedResourcesExecution.verify(() -> createResources(mojo.getResources()));
            mockedArchitectureExecution.verify(() -> createArchitecture(mojo.getArchitecture()));
        }
    }

    @Test
    void execute_withSkipTrue_shouldNotCreateAnything() {

        final MavenProject               project = createWithTestBaseDir();
        final ArchResourcesGeneratorMojo mojo    = getArchResourcesGeneratorMojo(project);

        mojo.getGeneralConfig().setSkip(true);

        try (final MockedStatic<ResourcesExecution> mockedResourcesExecution = mockStatic(ResourcesExecution.class);
             final MockedStatic<ArchitectureExecution> mockedArchitectureExecution = mockStatic(ArchitectureExecution.class);
             final MockedStatic<MavenProjectInjector> mockedMavenProjectInjector = mockStatic(MavenProjectInjector.class)) {

            mojo.execute();

            mockedMavenProjectInjector.verify(() -> injectMavenProject(project));
            mockedResourcesExecution.verifyNoInteractions();
            mockedArchitectureExecution.verifyNoInteractions();
        }
    }

    private static ArchResourcesGeneratorMojo getArchResourcesGeneratorMojo(final MavenProject project) {

        final ArchitectureConfig         architectureConfig = new ArchitectureConfig();
        final ResourcesConfig            resourcesConfig    = new ResourcesConfig();
        final GeneralConfig              generalConfig      = new GeneralConfig();
        final ArchResourcesGeneratorMojo mojo               = new ArchResourcesGeneratorMojo();
        mojo.setMavenProject(project);
        mojo.setGeneralConfig(generalConfig);
        mojo.setArchitecture(architectureConfig);
        mojo.setResources(resourcesConfig);
        return mojo;
    }

}
