package bmc.dev.resources.code.bmcresources.generators.archdesign;

import bmc.dev.resources.code.bmcresources.config.ArchitectureConfig;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.Constants.PROP_COMPLETED_ARCH;
import static bmc.dev.resources.code.bmcresources.generators.archdesign.ArchDesignStructureWriter.processArchitecture;
import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileWriter.writeMavenProperty;
import static bmc.dev.resources.code.bmcresources.utils.LogFormattingUtils.formatGreen;
import static java.lang.Boolean.FALSE;
import static java.lang.System.getProperty;
import static java.util.Optional.ofNullable;

@Slf4j
public class ArchitectureExecution {

    private ArchitectureExecution() {}

    public static void createArchitecture(final ArchitectureConfig architecture) {

        final Boolean archCompleted = ofNullable(getProperty(PROP_COMPLETED_ARCH)).map(Boolean::valueOf).orElse(FALSE);

        if (architecture.isSkip() || archCompleted) {
            log.info(formatGreen("Skipping generation of architecture. Skip is [{}], Completed is [{}]"), architecture.isSkip(), archCompleted);
        }
        else {
            processArchitecture(architecture);
            writeMavenProperty(PROP_COMPLETED_ARCH, "true");
        }
    }

}
