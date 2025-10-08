package bmc.dev.resources.code.bmcresources.generators.archdesign;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

import bmc.dev.resources.code.bmcresources.config.ArchitectureConfig;
import bmc.dev.resources.code.bmcresources.maven.MavenConfigFileWriter;

import static bmc.dev.resources.code.bmcresources.Constants.PROP_COMPLETED_ARCH;
import static bmc.dev.resources.code.bmcresources.generators.archdesign.ArchDesignProcessor.processArchitecture;
import static bmc.dev.resources.code.bmcresources.generators.archdesign.ArchitectureExecution.createArchitecture;
import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileWriter.writeMavenProperty;
import static java.lang.System.getProperties;
import static java.util.Optional.ofNullable;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

class ArchitectureExecutionTest {

    public static Stream<Arguments> skippingArchitecture() {

        final ArchitectureConfig architectureConfigSkipTrue  = new ArchitectureConfig();
        final ArchitectureConfig architectureConfigSkipFalse = new ArchitectureConfig();

        architectureConfigSkipTrue.setSkip(true);
        architectureConfigSkipFalse.setSkip(false);

        return Stream.of(Arguments.of(architectureConfigSkipTrue, null),
                         Arguments.of(architectureConfigSkipTrue, "notTrue"),  // should be interpreted as false
                         Arguments.of(architectureConfigSkipTrue, "true"),
                         Arguments.of(architectureConfigSkipFalse, "true"));
    }

    @Test
    void createArchitecture_withSkipFalseAndAchNotCompleted_shouldProcessArchitectureAnWriteToMaven() {

        final ArchitectureConfig architectureConfig = new ArchitectureConfig();
        architectureConfig.setSkip(false);

        try (final MockedStatic<ArchDesignProcessor> mockedArchProcessor = mockStatic(ArchDesignProcessor.class);
             final MockedStatic<MavenConfigFileWriter> mockedMavenConfigFileWriter = mockStatic(MavenConfigFileWriter.class)) {

            createArchitecture(architectureConfig);

            mockedArchProcessor.verify(() -> processArchitecture(architectureConfig), times(1));
            mockedMavenConfigFileWriter.verify(() -> writeMavenProperty(PROP_COMPLETED_ARCH, "true"));
        }
    }

    @ParameterizedTest
    @MethodSource("skippingArchitecture")
    void createArchitecture_withSkipTrueOrAchCompleted_shouldReturnAndDoNothing(final ArchitectureConfig architectureConfig, final String archCompleted) {

        ofNullable(archCompleted).ifPresent(completed -> getProperties().setProperty(PROP_COMPLETED_ARCH, completed));

        try (final MockedStatic<ArchDesignProcessor> mockedArchProcessor = mockStatic(ArchDesignProcessor.class);
             final MockedStatic<MavenConfigFileWriter> mockedMavenConfigFileWriter = mockStatic(MavenConfigFileWriter.class)) {

            createArchitecture(architectureConfig);

            mockedArchProcessor.verifyNoInteractions();
            mockedMavenConfigFileWriter.verifyNoInteractions();
        }
    }

}
