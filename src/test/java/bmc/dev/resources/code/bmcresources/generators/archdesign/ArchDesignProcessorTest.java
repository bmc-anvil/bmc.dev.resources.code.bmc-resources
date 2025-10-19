package bmc.dev.resources.code.bmcresources.generators.archdesign;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import bmc.dev.resources.code.bmcresources.config.ArchitectureConfig;
import bmc.dev.resources.code.bmcresources.io.BMCConfigFileReader;

import static bmc.dev.resources.code.bmcresources.generators.archdesign.ArchDesignProcessor.processArchitecture;
import static bmc.dev.resources.code.bmcresources.generators.archdesign.ArchDesignUtils.*;
import static bmc.dev.resources.code.bmcresources.io.BMCConfigFileReader.extractConfigFileEntries;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ArchDesignProcessorTest {

    @Test
    void processArchitecture_withExistingModelAndNotSkippingReadme_shouldProcessBothArchitectureAndReadme() {

        final ArchitectureConfig architectureConfig = new ArchitectureConfig();
        architectureConfig.setModel("clean_ddd_hexa_for_tests");
        architectureConfig.setSkipReadme(false);

        try (final MockedStatic<ArchDesignUtils> mockedArchUtils = mockStatic(ArchDesignUtils.class);
             final MockedStatic<BMCConfigFileReader> mockedBMCFileReader = mockStatic(BMCConfigFileReader.class)) {

            mockedBMCFileReader.when(() -> extractConfigFileEntries(any(), any())).thenCallRealMethod();

            processArchitecture(architectureConfig);

            mockedArchUtils.verify(() -> logArchitectureConfiguration(any(), any()), times(1));
            mockedBMCFileReader.verify(() -> extractConfigFileEntries(any(), any()), times(1));
            mockedArchUtils.verify(() -> processArchitectureOnly(any(), any()), never());
            mockedArchUtils.verify(() -> processArchitectureAndReadme(any(), any(), any()), times(1));
        }
    }

    @Test
    void processArchitecture_withExistingModelAndSkipReadme_shouldProcessArchitectureAndSkipReadmeCreation() {

        final ArchitectureConfig architectureConfig = new ArchitectureConfig();
        architectureConfig.setModel("clean_ddd_hexa_for_tests");
        architectureConfig.setSkipReadme(true);

        try (final MockedStatic<ArchDesignUtils> mockedArchUtils = mockStatic(ArchDesignUtils.class);
             final MockedStatic<BMCConfigFileReader> mockedBMCFileReader = mockStatic(BMCConfigFileReader.class)) {

            mockedBMCFileReader.when(() -> extractConfigFileEntries(any(), any())).thenCallRealMethod();

            processArchitecture(architectureConfig);

            mockedArchUtils.verify(() -> logArchitectureConfiguration(any(), any()), times(1));
            mockedBMCFileReader.verify(() -> extractConfigFileEntries(any(), any()), times(1));
            mockedArchUtils.verify(() -> processArchitectureOnly(any(), any()), times(1));
            mockedArchUtils.verify(() -> processArchitectureAndReadme(any(), any(), any()), never());
        }
    }

    @Test
    void processArchitecture_withNullModel_shouldSkipCreation() {

        final ArchitectureConfig architectureConfig = new ArchitectureConfig();
        architectureConfig.setModel(null);

        try (final MockedStatic<ArchDesignUtils> mockedArchUtils = mockStatic(ArchDesignUtils.class);
             final MockedStatic<BMCConfigFileReader> mockedBMCFileReader = mockStatic(BMCConfigFileReader.class)) {

            processArchitecture(architectureConfig);

            mockedArchUtils.verifyNoInteractions();
            mockedBMCFileReader.verifyNoInteractions();
        }
    }

}
