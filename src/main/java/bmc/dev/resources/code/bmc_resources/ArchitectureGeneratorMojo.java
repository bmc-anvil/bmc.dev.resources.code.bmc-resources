package bmc.dev.resources.code.bmc_resources;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import static bmc.dev.resources.code.bmc_resources.ArchDesignStructureReader.readArchStructureFromModel;
import static bmc.dev.resources.code.bmc_resources.ArchDesignStructureWriter.createArchitectureStructure;
import static bmc.dev.resources.code.bmc_resources.Constants.*;
import static bmc.dev.resources.code.bmc_resources.MavenConfigFileUpdater.updateArchitectureCompletion;
import static java.io.File.separator;
import static java.lang.Boolean.FALSE;
import static java.lang.System.getProperty;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_SOURCES;

@Mojo(name = "generate-architecture", defaultPhase = GENERATE_SOURCES, threadSafe = true)
public class ArchitectureGeneratorMojo extends AbstractMojo {

    /**
     * The name of the architecture's main README file, placed at the root level of the generated folder structure.
     */
    @Parameter(property = "architecture.main.readme", alias = "architecture.main.readme")
    private String       architectureMainReadme;
    /**
     * The architecture's model to use for folder structure generation.
     */
    @Parameter(property = "architecture.model", alias = "architecture.model")
    private String       architectureModel;
    /**
     * Overwrites existing readme files.
     */
    @Parameter(property = "architecture.overwriteReadmes", alias = "architecture.overwriteReadmes", defaultValue = "true")
    private boolean      architectureOverwriteReadmes;
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject mavenProject;
    /**
     * Skip the plugin execution altogether.
     */
    @Parameter(property = "architecture.skip", alias = "architecture.skip", defaultValue = "false")
    private boolean      skipGeneration;

    @Override
    public void execute() {

        final Boolean architectureCompleted = ofNullable(getProperty(PROPERTY_ARCH_COMPLETED)).map(Boolean::valueOf).orElse(FALSE);

        if (skipGeneration) {
            getLog().info("Skipping generation of architecture");
            return;
        }
        if (architectureCompleted) {
            getLog().info("Architecture Structure Creation completed previously. Nothing to do");
            return;
        } else if (isBlank(architectureModel)) {
            getLog().info("Architecture Structure is blank, skipping.");
            return;
        }

        final String artifactId                = mavenProject.getArtifactId().replace("-", "_");
        final String groupId                   = mavenProject.getGroupId().replace(".", "/");
        final String sourceDirectory           = mavenProject.getBuild().getSourceDirectory();
        final String targetRootDirectory       = sourceDirectory + separator + groupId + separator + artifactId;
        final String architectureStructureFile = "/" + FOLDER_MODELS + "/" + architectureModel + "/" + architectureModel + STRUCTURE_POSTFIX;

        getLog().info("Architecture Structure configuration: ---");
        getLog().info("%s --- architecture.completed: %s%s".formatted(COLOR_BLUE, architectureCompleted, COLOR_RESET));
        getLog().info("%s --- architecture.model: %s%s".formatted(COLOR_BLUE, architectureModel, COLOR_RESET));
        getLog().info("%s --- architecture.main.readme: %s%s".formatted(COLOR_BLUE, architectureMainReadme, COLOR_RESET));
        getLog().info("%s --- architecture.overwriteReadmes: %s%s".formatted(COLOR_BLUE, architectureOverwriteReadmes, COLOR_RESET));
        getLog().info("%s --- artifactId: %s%s".formatted(COLOR_BLUE, artifactId, COLOR_RESET));
        getLog().info("%s --- groupId: %s%s".formatted(COLOR_BLUE, groupId, COLOR_RESET));
        getLog().info("%s --- sourceDirectory: %s%s".formatted(COLOR_BLUE, sourceDirectory, COLOR_RESET));
        getLog().info("%s --- targetRootDirectory: %s%s".formatted(COLOR_BLUE, targetRootDirectory, COLOR_RESET));
        getLog().info("Architecture Structure configuration ---.\n");

        getLog().info("%sArchitecture Structure creation started.%s\n".formatted(COLOR_YELLOW, COLOR_RESET));

        final Entry<Integer, Map<String, String>> computedStructureMap =
                readArchStructureFromModel(architectureStructureFile, getLog(), this.getClass()).orElseThrow();

        createArchitectureStructure(getLog(), this.getClass(),targetRootDirectory, computedStructureMap, architectureMainReadme, architectureModel,
                                    architectureOverwriteReadmes);
        updateArchitectureCompletion(getLog());

        getLog().info("%s%sArchitecture Structure creation completed.%s\n".formatted(COLOR_BOLD, COLOR_YELLOW, COLOR_RESET));
    }

}
