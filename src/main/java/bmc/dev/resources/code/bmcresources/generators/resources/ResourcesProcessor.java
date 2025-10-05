package bmc.dev.resources.code.bmcresources.generators.resources;

import java.util.List;

import bmc.dev.resources.code.bmcresources.config.ResourceEntry;
import bmc.dev.resources.code.bmcresources.config.ResourcesConfig;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.Constants.*;
import static bmc.dev.resources.code.bmcresources.generators.resources.ResourcesUtils.processResourcesMap;
import static bmc.dev.resources.code.bmcresources.io.ConfigFileReader.readResourcesConfigFile;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatBoldColor;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatColor;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.GREEN;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.YELLOW;
import static java.lang.Boolean.FALSE;
import static java.lang.System.getProperty;
import static java.util.Optional.ofNullable;

/**
 * Class for processing resources.
 */
@Slf4j
@UtilityClass
public class ResourcesProcessor {

    /**
     * Processes resources based on a general configuration that can be modified on the pom.xml itself.
     * <p>
     * Resources' config files are divided into upstream and user.
     * <p>
     * Upstream resources are always written and overwritten if there was a change in the plugin version, while user resources are written once to allow user
     * modification.
     * <br>User resources can be overwritten via configuration.
     *
     * @param config The configuration object defining parameters for resource processing.
     */
    public static void processResources(final ResourcesConfig config) {

        final Boolean resourcesCompleted = ofNullable(getProperty(PROP_COMPLETED_RESOURCE)).map(Boolean::valueOf).orElse(FALSE);

        log.info(formatColor.apply(YELLOW, "BMC-Resources Generation started."));
        log.info(formatColor.apply(YELLOW, "Processing upstream Resources."));

        final List<ResourceEntry> upstreamResources = readResourcesConfigFile(FILE_RESOURCES_UPSTREAM);
        processResourcesMap(upstreamResources);

        log.info("");

        if (!config.isOverwriteUserResources() && resourcesCompleted) {
            log.info(formatColor.apply(GREEN, "Skipping Processing user Resources. Overwrite is [{}], Completed is [{}]"), false, true);
        } else {
            log.info(formatColor.apply(YELLOW, "Processing user Resources."));

            final List<ResourceEntry> userResources = readResourcesConfigFile(FILE_RESOURCES_USER);
            processResourcesMap(userResources);

            log.info("");
        }

        log.info(formatBoldColor.apply(YELLOW, "BMC-Resources Generation completed."));
    }

}
