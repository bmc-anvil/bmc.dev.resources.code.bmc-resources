package bmc.dev.resources.code.bmcresources.mojos;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import bmc.dev.resources.code.bmcresources.config.ArchitectureConfig;
import bmc.dev.resources.code.bmcresources.config.ResourcesConfig;

import static bmc.dev.resources.code.bmcresources.Constants.*;
import static bmc.dev.resources.code.bmcresources.generators.archdesign.ArchDesignStructureWriter.createArchitectureStructure;
import static bmc.dev.resources.code.bmcresources.generators.resources.ResourcesProcessor.processResources;
import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileUpdater.updateMavenConfig;
import static java.lang.Boolean.FALSE;
import static java.lang.System.getProperty;
import static java.util.Optional.ofNullable;
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
    private ArchitectureConfig architecture = new ArchitectureConfig();
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject       mavenProject;
    /**
     * Configuration for resources-related operations during the plugin execution.
     */
    @Parameter
    private ResourcesConfig    resources    = new ResourcesConfig();

    @Override
    public void execute() {

        getLog().info("%s%sArchitecture Structure and Resources creation started.%s".formatted(COLOR_BOLD, COLOR_YELLOW, COLOR_RESET));
        getLog().info("");

        executeArchitectureCreation();
        executeResourcesCreation();

        getLog().info("%s%sArchitecture Structure and Resources creation completed.%s".formatted(COLOR_BOLD, COLOR_YELLOW, COLOR_RESET));
        getLog().info("");
    }

    private void executeArchitectureCreation() {

        final Boolean archCompleted = ofNullable(getProperty(PROPERTY_COMPLETED_ARCH)).map(Boolean::valueOf).orElse(FALSE);

        if (architecture.isSkip() || archCompleted) {
            getLog().info("%sSkipping generation of architecture. Skip is [%s], Completed is [%s]%s"
                                  .formatted(COLOR_GREEN, architecture.isSkip(), archCompleted, COLOR_RESET));
        }
        else {
            createArchitectureStructure(this, mavenProject, architecture);
            updateMavenConfig(this, PROPERTY_COMPLETED_ARCH);
        }
    }

    private void executeResourcesCreation() {

        if (resources.isSkip()) {
            getLog().info("%sSkipping generation of resources. Skip is [%s]%s".formatted(COLOR_GREEN, resources.isSkip(), COLOR_RESET));
        }
        else {
            processResources(this, mavenProject, resources);
            updateMavenConfig(this, PROPERTY_COMPLETED_RESOURCE);
        }
    }

}
