package bmc.dev.resources.code.bmcresources.generators.resources;

import java.util.Map;
import java.util.Map.Entry;

import bmc.dev.resources.code.bmcresources.config.ResourcesConfig;
import bmc.dev.resources.code.bmcresources.io.OSUtilities;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.Constants.*;
import static bmc.dev.resources.code.bmcresources.generators.resources.ResourcesUtils.processResourcesMap;
import static bmc.dev.resources.code.bmcresources.io.ConfigFileReader.readConfigFile;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatBoldColor;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatColor;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.GREEN;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.YELLOW;
import static java.lang.Boolean.FALSE;
import static java.lang.System.getProperty;
import static java.util.Optional.ofNullable;

@Slf4j
@UtilityClass
public class ResourcesProcessor {

    public static void processResources(final ResourcesConfig config) {

        final Boolean resourcesCompleted = ofNullable(getProperty(PROP_COMPLETED_RESOURCE)).map(Boolean::valueOf).orElse(FALSE);

        log.info(formatColor.apply(YELLOW, "BMC-Resources Generation started."));
        log.info(formatColor.apply(YELLOW, "Processing upstream Resources."));

        final Entry<Integer, Map<String, String>> upstreamResources = readConfigFile(FILE_RESOURCES_UPSTREAM).orElseThrow();
        processResourcesMap(upstreamResources);

        log.info("");

        if (!config.isOverwriteUserResources() && resourcesCompleted) {
            log.info(formatColor.apply(GREEN, "Skipping Processing user Resources. Overwrite is [{}], Completed is [{}]"), false, true);
        } else {
            log.info(formatColor.apply(YELLOW, "Processing user Resources."));

            final Entry<Integer, Map<String, String>> userResources = readConfigFile(FILE_RESOURCES_USER).orElseThrow();
            processResourcesMap(userResources);

            log.info("");
        }

        log.info(formatColor.apply(YELLOW, "Processing executable files."));

        readConfigFile(FILE_RESOURCES_EXECUTABLES).orElseThrow()
                                                  .getValue()
                                                  .keySet()
                                                  .forEach(OSUtilities::makeFileExecutable);

        log.info(formatBoldColor.apply(YELLOW, "BMC-Resources Generation completed."));
    }

}
