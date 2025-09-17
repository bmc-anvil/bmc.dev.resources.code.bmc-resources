package bmc.dev.resources.code.bmcresources.generators.resources;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import bmc.dev.resources.code.bmcresources.properties.ResourcesConfig;

import static bmc.dev.resources.code.bmcresources.Constants.*;
import static bmc.dev.resources.code.bmcresources.generators.resources.ResourcesUtils.processResourcesMap;
import static bmc.dev.resources.code.bmcresources.io.ConfigFileReader.readConfigFile;
import static bmc.dev.resources.code.bmcresources.io.OSUtilities.makeFilesExecutable;
import static java.lang.Boolean.FALSE;
import static java.lang.System.getProperty;
import static java.util.Optional.ofNullable;

public class ResourcesProcessor {

    public static void processResources(final AbstractMojo mojo, final MavenProject mavenProject, final ResourcesConfig config) {

        final Log     log                = mojo.getLog();
        final Boolean resourcesCompleted = ofNullable(getProperty(PROP_COMPLETED_RESOURCE)).map(Boolean::valueOf).orElse(FALSE);

        log.info("%sBMC-Resources Generation started.%s".formatted(COLOR_YELLOW, COLOR_RESET));
        log.info("%sProcessing upstream Resources.%s".formatted(COLOR_YELLOW, COLOR_RESET));

        final Entry<Integer, Map<String, String>> upstreamResources = readConfigFile(mojo, FILE_RESOURCES_UPSTREAM).orElseThrow();
        processResourcesMap(mojo, mavenProject, upstreamResources);

        log.info("");

        if (!config.isOverwriteUserResources() && resourcesCompleted) {
            log.info("%sSkipping Processing user Resources. Overwrite is [%s], Completed is [%s]%s".formatted(COLOR_GREEN, false, true, COLOR_RESET));
        }
        else {
            log.info("%sProcessing user Resources.%s".formatted(COLOR_YELLOW, COLOR_RESET));

            final Entry<Integer, Map<String, String>> userResources = readConfigFile(mojo, FILE_RESOURCES_USER).orElseThrow();
            processResourcesMap(mojo, mavenProject, userResources);

            log.info("");
        }

        log.info("%sProcessing executable files.%s".formatted(COLOR_YELLOW, COLOR_RESET));

        final Entry<Integer, Map<String, String>> executableResources = readConfigFile(mojo, FILE_RESOURCES_EXECUTABLES).orElseThrow();
        makeFilesExecutable(mojo, executableResources);

        log.info("%s%sBMC-Resources Generation completed.%s".formatted(COLOR_BOLD, COLOR_YELLOW, COLOR_RESET));
    }

}
