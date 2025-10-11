package bmc.dev.resources.code.bmcresources.generators.archdesign;

import java.util.List;

import bmc.dev.resources.code.bmcresources.config.ArchitectureConfig;
import bmc.dev.resources.code.bmcresources.config.ArchitectureEntry;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.generators.archdesign.ArchDesignUtils.*;
import static bmc.dev.resources.code.bmcresources.io.BMCConfigFileReader.extractConfigFileEntries;
import static bmc.dev.resources.code.bmcresources.utils.BMCConfigFileUtils.*;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatBoldColor;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatColor;
import static bmc.dev.resources.code.bmcresources.utils.StringUtils.isNullOrBlank;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.YELLOW;

/**
 * Utility class for processing architecture design generation.
 * <p>
 * This class handles folder structure creation and optional README files going with the architecture model.
 */
@Slf4j
@UtilityClass
public class ArchDesignProcessor {

    /**
     * Generates architecture based on the configured model.
     * <p>
     * This method processes folder creation and readme creation at the same time (not in parallel, but during the same pass).
     * <br>This implies that we do a single pass performing folder and readme ops.
     * <p>
     * Because of the above, if we have a single method to create the structure, we could end up checking if skipReadme is true at every entry.
     * I chose to check the skipReadme flag just once and have 2 methods to create the Architecture Design.
     * <br>This will repeat a little code but will read clearer.
     *
     * @param config Configuration object containing parameters for the architecture structure creation, including:
     *               <br>- Model: Defines the architecture model to use for folder structure generation.
     *               <br>- Main README: Specifies the main README file for the architecture.
     *               <br>- Skip Readme: Allows skipping the generation of README files within the architecture structure.
     */
    public static void processArchitecture(final ArchitectureConfig config) {

        if (isNullOrBlank.test(config.getModel())) {
            log.warn(formatBoldColor.apply(YELLOW, ("Architecture Model is missing from the pom configuration, skipping.")));
        } else {

            log.info(formatColor.apply(YELLOW, ("Architecture Structure creation started.")));
            logArchitectureConfiguration(config);

            extractConfigFileEntries(getArchStructureFile.apply(config.getModel()), extractArchitectureModel)
                    .ifPresent(architectureEntries -> {
                        final List<String> archFolders = architectureEntries.stream().map(ArchitectureEntry::folder).toList();
                        final String       padding     = calculateLeftAlignedPadding.apply(getMaxStringLength.apply(archFolders));

                        if (config.isSkipReadme()) {
                            processArchitectureOnly(architectureEntries, padding);
                        } else {
                            processArchitectureAndReadme(config, architectureEntries, padding);
                        }
                    });

            log.info(formatColor.apply(YELLOW, "Architecture Structure creation completed."));
        }
    }

}
