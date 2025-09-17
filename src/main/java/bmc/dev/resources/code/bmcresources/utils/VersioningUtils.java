package bmc.dev.resources.code.bmcresources.utils;

import java.util.Optional;

import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import static bmc.dev.resources.code.bmcresources.Constants.PLUGIN_KEY;
import static bmc.dev.resources.code.bmcresources.Constants.PROP_CREATED_WITH_VERSION;
import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileReader.readMavenProperty;
import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileWriter.writeMavenProperty;

public class VersioningUtils {

    /**
     * Determines if the current plugin version being executed is different from the version recorded during the last execution.
     * <p>
     * NOTE: I do not care about keeping newer resources vs. older ones, if the decision is made to go back to an older version of the plugin,
     * I interpret that the intention is to re-run the plugin, probably with older versions of resources, arch, etc.
     *
     * @param mojo         The instance of the Mojo class containing the plugin execution context and logging utilities.
     * @param mavenProject The Maven project instance that provides access to properties, dependencies, and plugins associated with the project.
     *
     * @return {@code true} if the plugin version has changed since the last recorded execution, {@code false} otherwise.
     */
    public static Boolean hasPluginVersionChanged(final AbstractMojo mojo, final MavenProject mavenProject) {

        final Log log = mojo.getLog();

        log.debug("BMC resources plugin key: " + PLUGIN_KEY);

        final Plugin           plugin                   = mavenProject.getPlugin(PLUGIN_KEY);
        final String           currentPluginVersion     = plugin.getVersion().trim();
        final Optional<String> createdWithPluginVersion = readMavenProperty(mojo, mavenProject, PROP_CREATED_WITH_VERSION);

        log.debug("Plugin check: executed with v.[%s] / created with v.[%s]".formatted(currentPluginVersion, createdWithPluginVersion.orElse("")));

        return !currentPluginVersion.equals(createdWithPluginVersion.orElse(""));
    }

    public static void stampCurrentPluginVersion(final AbstractMojo mojo, final MavenProject mavenProject) {

        final Log log = mojo.getLog();

        log.debug("BMC resources plugin key: " + PLUGIN_KEY);

        final Plugin plugin               = mavenProject.getPlugin(PLUGIN_KEY);
        final String currentPluginVersion = plugin.getVersion().trim();

        writeMavenProperty(mojo, PROP_CREATED_WITH_VERSION, currentPluginVersion, mavenProject);

        log.info("Architecture and resources created with BMC plugin v.[%s]".formatted(currentPluginVersion));

    }

}
