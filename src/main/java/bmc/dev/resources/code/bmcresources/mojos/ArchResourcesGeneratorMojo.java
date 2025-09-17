package bmc.dev.resources.code.bmcresources.mojos;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import bmc.dev.resources.code.bmcresources.properties.ArchitectureConfig;
import bmc.dev.resources.code.bmcresources.properties.GeneralConfig;
import bmc.dev.resources.code.bmcresources.properties.ResourcesConfig;

import static bmc.dev.resources.code.bmcresources.Constants.*;
import static bmc.dev.resources.code.bmcresources.generators.archdesign.ArchitectureExecution.createArchitecture;
import static bmc.dev.resources.code.bmcresources.generators.resources.ResourcesExecution.createResources;
import static bmc.dev.resources.code.bmcresources.utils.VersioningUtils.hasPluginVersionChanged;
import static bmc.dev.resources.code.bmcresources.utils.VersioningUtils.stampCurrentPluginVersion;
import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_SOURCES;

/**
 * Generator of the initial architecture folder structure for a project.
 * <p>
 * Architecture design files are packaged in the plugin itself, so no connectivity is required after the initial download.
 */
@Mojo(name = "generate-architecture", defaultPhase = GENERATE_SOURCES, threadSafe = true)
public class ArchResourcesGeneratorMojo extends AbstractMojo {

    /**
     * Configuration for architecture-related operations during the plugin execution.
     */
    @Parameter
    private final ArchitectureConfig architecture  = new ArchitectureConfig();
    /**
     * General plugin configuration options.
     */
    @Parameter
    private final GeneralConfig      generalConfig = new GeneralConfig();
    /**
     * Configuration for resources-related operations during the plugin execution.
     */
    @Parameter
    private final ResourcesConfig    resources     = new ResourcesConfig();
    /**
     * The Maven project:
     * <p>
     * This is automatically bound to the project in which the plugin is executed.
     * It provides access to various Maven-specific information such as dependencies,
     * build configurations, and properties associated with the project.
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private       MavenProject       mavenProject;

    @Override
    public void execute() {

        final Log log = getLog();

        if (generalConfig.isSkip()) {
            log.info("%s%sPlugin execution skip is set to true. Nothing to do.%s".formatted(COLOR_BOLD, COLOR_YELLOW, COLOR_RESET));
        }
        else if (hasPluginVersionChanged(this, mavenProject)) {
            log.info("%s%sNew Project or different plugin version. Generating Architecture and Resources.%s".formatted(COLOR_BOLD, COLOR_YELLOW, COLOR_RESET));
            log.info("%s%sArchitecture Structure and Resources creation started.%s".formatted(COLOR_BOLD, COLOR_YELLOW, COLOR_RESET));

            createResources(resources, this, mavenProject);
            createArchitecture(architecture, this, mavenProject);
            stampCurrentPluginVersion(this, mavenProject);

            log.info("%s%sArchitecture Structure and Resources creation completed.%s".formatted(COLOR_BOLD, COLOR_YELLOW, COLOR_RESET));
        }
        else {
            log.info("%s%sNo change in plugin version since last run. Nothing to do.%s".formatted(COLOR_BOLD, COLOR_YELLOW, COLOR_RESET));
        }

        log.info("");
    }

}
