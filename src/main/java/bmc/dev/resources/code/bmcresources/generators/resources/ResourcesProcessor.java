package bmc.dev.resources.code.bmcresources.generators.resources;

import java.util.Map;
import java.util.Map.Entry;

import bmc.dev.resources.code.bmcresources.config.ResourcesConfig;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.Constants.*;
import static bmc.dev.resources.code.bmcresources.generators.resources.ResourcesUtils.processResourcesMap;
import static bmc.dev.resources.code.bmcresources.io.ConfigFileReader.readConfigFile;
import static bmc.dev.resources.code.bmcresources.io.OSUtilities.makeFilesExecutable;
import static java.lang.Boolean.FALSE;
import static java.lang.System.getProperty;
import static java.util.Optional.ofNullable;

@Slf4j
public class ResourcesProcessor {

    public static void processResources(final ResourcesConfig config) {

        final Boolean resourcesCompleted = ofNullable(getProperty(PROP_COMPLETED_RESOURCE)).map(Boolean::valueOf).orElse(FALSE);

        log.info("{}BMC-Resources Generation started.{}", COLOR_YELLOW, COLOR_RESET);
        log.info("{}Processing upstream Resources.{}", COLOR_YELLOW, COLOR_RESET);

        final Entry<Integer, Map<String, String>> upstreamResources = readConfigFile(FILE_RESOURCES_UPSTREAM).orElseThrow();
        processResourcesMap(upstreamResources);

        log.info("");

        if (!config.isOverwriteUserResources() && resourcesCompleted) {
            log.info("{}Skipping Processing user Resources. Overwrite is [{}], Completed is [{}]{}", COLOR_GREEN, false, true, COLOR_RESET);
        }
        else {
            log.info("{}Processing user Resources.{}", COLOR_YELLOW, COLOR_RESET);

            final Entry<Integer, Map<String, String>> userResources = readConfigFile(FILE_RESOURCES_USER).orElseThrow();
            processResourcesMap(userResources);

            log.info("");
        }

        log.info("{}Processing executable files.{}", COLOR_YELLOW, COLOR_RESET);

        final Entry<Integer, Map<String, String>> executableResources = readConfigFile(FILE_RESOURCES_EXECUTABLES).orElseThrow();
        makeFilesExecutable(executableResources);

        log.info("{}{}BMC-Resources Generation completed.{}", COLOR_BOLD, COLOR_YELLOW, COLOR_RESET);
    }

}
