package bmc.dev.resources.code.bmcresources.utils;

import org.apache.maven.model.Plugin;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.Constants.PLUGIN_KEY;
import static bmc.dev.resources.code.bmcresources.Constants.PROP_CREATED_WITH_VERSION;
import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileReader.getMavenPropertyValue;
import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileWriter.writeMavenProperty;
import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.getMavenProject;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatColor;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.CYAN;

/**
 * Utility class for managing versioning operations related to plugins in a Maven project.
 */
@Slf4j
@UtilityClass
public class VersioningUtils {

    /**
     * Determines if the current plugin version being executed is different from the version recorded during the last execution.
     * <p>
     * NOTE: I do not care about keeping newer resources vs. older ones, if the decision is made to go back to an older version of the plugin,
     * I interpret that the intention is to re-run the plugin, probably with older versions of resources, arch, etc.
     *
     * @return {@code true} if the plugin version has changed since the last recorded execution, {@code false} otherwise.
     */
    public static Boolean hasPluginVersionChanged() {

        log.debug(formatColor.apply(CYAN, "BMC resources plugin key: [{}]"), PLUGIN_KEY);

        final Plugin plugin                   = getMavenProject().getPlugin(PLUGIN_KEY);
        final String currentPluginVersion     = plugin.getVersion().trim();
        final String createdWithPluginVersion = getMavenPropertyValue(PROP_CREATED_WITH_VERSION).orElse("");

        log.debug(formatColor.apply(CYAN, "Plugin check: executed with v.[{}] / created with v.[{}]"), currentPluginVersion, createdWithPluginVersion);

        return !currentPluginVersion.equals(createdWithPluginVersion);
    }

    /**
     * Stamps the current plugin's version as a maven property in the config.maven file.
     */
    public static void stampCurrentPluginVersion() {

        log.debug(formatColor.apply(CYAN, "BMC resources plugin key: [{}]"), PLUGIN_KEY);

        final Plugin plugin               = getMavenProject().getPlugin(PLUGIN_KEY);
        final String currentPluginVersion = plugin.getVersion().trim();

        writeMavenProperty(PROP_CREATED_WITH_VERSION, currentPluginVersion);

        log.info("Architecture and resources created with BMC plugin v.[{}]", currentPluginVersion);

    }

}
