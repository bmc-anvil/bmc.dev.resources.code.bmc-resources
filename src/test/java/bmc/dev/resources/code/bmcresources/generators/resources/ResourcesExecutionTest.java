package bmc.dev.resources.code.bmcresources.generators.resources;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import bmc.dev.resources.code.bmcresources.config.ResourcesConfig;
import bmc.dev.resources.code.bmcresources.maven.MavenConfigFileWriter;

import static bmc.dev.resources.code.bmcresources.generators.resources.ResourcesExecution.createResources;
import static bmc.dev.resources.code.bmcresources.generators.resources.ResourcesProcessor.processResources;
import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileWriter.writeMavenProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

class ResourcesExecutionTest {

    @Test
    void createResources_withSkippingFalse_shouldProcessResources() {

        final ResourcesConfig resourcesConfig = new ResourcesConfig();
        resourcesConfig.setSkip(false);

        try (final MockedStatic<ResourcesProcessor> mockedResourcesProcessor = mockStatic(ResourcesProcessor.class);
             final MockedStatic<MavenConfigFileWriter> mockedMavenConfigWriter = mockStatic(MavenConfigFileWriter.class)) {

            createResources(resourcesConfig);

            mockedResourcesProcessor.verify(() -> processResources(any()), times(1));
            mockedMavenConfigWriter.verify(() -> writeMavenProperty(any(), any()), times(1));
        }
    }

    @Test
    void createResources_withSkippingTrue_shouldExitWithoutProcessingAnything() {

        final ResourcesConfig resourcesConfig = new ResourcesConfig();
        resourcesConfig.setSkip(true);

        try (final MockedStatic<ResourcesProcessor> mockedResourcesProcessor = mockStatic(ResourcesProcessor.class);
             final MockedStatic<MavenConfigFileWriter> mockedMavenConfigWriter = mockStatic(MavenConfigFileWriter.class)) {

            createResources(resourcesConfig);

            mockedResourcesProcessor.verify(() -> processResources(any()), times(0));
            mockedMavenConfigWriter.verify(() -> writeMavenProperty(any(), any()), times(0));
        }

    }

}
