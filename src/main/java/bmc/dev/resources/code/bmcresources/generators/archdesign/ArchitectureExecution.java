package bmc.dev.resources.code.bmcresources.generators.archdesign;

import bmc.dev.resources.code.bmcresources.config.ArchitectureConfig;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.Constants.PROP_COMPLETED_ARCH;
import static bmc.dev.resources.code.bmcresources.generators.archdesign.ArchDesignProcessor.processArchitecture;
import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileUtils.isPropertySetAndTrue;
import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileWriter.writeMavenProperty;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatColor;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.GREEN;

/**
 * Class for managing the execution of architecture design operations.
 */
@Slf4j
@UtilityClass
public class ArchitectureExecution {

    /**
     * Creates the architecture structure with its accompanying Readme Files.
     * <p>
     * This method performs either the architecture structure generation or skips the operation, depending on the configuration settings.
     *
     * @param architecture the {@link ArchitectureConfig} object defining the architecture structure generation behavior.
     */
    public static void createArchitecture(final ArchitectureConfig architecture) {

        final boolean archCompleted = isPropertySetAndTrue.test(PROP_COMPLETED_ARCH);
        log.info("Architecture execution is marked as completed: [{}]", archCompleted);

        if (architecture.isSkip() || archCompleted) {
            log.info(formatColor.apply(GREEN, "Skipping generation of architecture. Skip is [{}], Completed is [{}]"), architecture.isSkip(), archCompleted);
        } else {
            processArchitecture(architecture);
            writeMavenProperty(PROP_COMPLETED_ARCH, "true");
        }
    }

}
