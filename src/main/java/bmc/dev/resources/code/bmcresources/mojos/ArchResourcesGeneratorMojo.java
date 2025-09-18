package bmc.dev.resources.code.bmcresources.mojos;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import bmc.dev.resources.code.bmcresources.config.ArchitectureConfig;
import bmc.dev.resources.code.bmcresources.config.GeneralConfig;
import bmc.dev.resources.code.bmcresources.config.ResourcesConfig;
import bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.Constants.*;
import static bmc.dev.resources.code.bmcresources.generators.archdesign.ArchitectureExecution.createArchitecture;
import static bmc.dev.resources.code.bmcresources.generators.resources.ResourcesExecution.createResources;
import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.setMavenProject;
import static bmc.dev.resources.code.bmcresources.utils.VersioningUtils.hasPluginVersionChanged;
import static bmc.dev.resources.code.bmcresources.utils.VersioningUtils.stampCurrentPluginVersion;
import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_SOURCES;

/**
 * Generator of the initial architecture folder structure for a project.
 * <p>
 * Architecture design files are packaged in the plugin itself, so no connectivity is required after the initial download.
 */
@Slf4j
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

    /**
     * This is the Plugin's entry point.
     * <p>
     * I played with having one for architecture and one for resources but settled for a single entry point.
     * It is easier to configure and maintain.
     * <p>
     * Probably it should not be, but the order is relevant in executing a few things.
     * <p>
     * It is paramount that setting the maven project is the first thing that happens. Check {@link MavenProjectInjector} for details.
     * <br> Because this is an intermediate project between maven 3.9.x and 4.x.x, the current Maven "DI" is not used, so the injector above works as
     * some innocent man's DI.
     * <p>
     * The project is made so a Gradle savvy person can make it into a Gradle plugin with more ease than if I've plugged mavenProject and MOJO all over.
     * <br> The same will go for myself when porting this to Maven 4.
     */
    @Override
    public void execute() {

        setMavenProject(mavenProject);

        if (generalConfig.isSkip()) {
            log.info("{}{}Plugin execution skip is set to true. Nothing to do.{}", COLOR_BOLD, COLOR_YELLOW, COLOR_RESET);
        }
        else if (hasPluginVersionChanged()) {
            log.info("{}{}New Project or different plugin version. Generating Architecture and Resources.{}", COLOR_BOLD, COLOR_YELLOW, COLOR_RESET);
            log.info("{}{}Architecture Structure and Resources creation started.{}", COLOR_BOLD, COLOR_YELLOW, COLOR_RESET);

            createResources(resources);
            createArchitecture(architecture);
            stampCurrentPluginVersion();

            log.info("{}{}Architecture Structure and Resources creation completed.{}", COLOR_BOLD, COLOR_YELLOW, COLOR_RESET);
        }
        else {
            log.info("{}{}No change in plugin version since last run. Nothing to do.{}", COLOR_BOLD, COLOR_YELLOW, COLOR_RESET);
        }

        log.info("");
    }

}
