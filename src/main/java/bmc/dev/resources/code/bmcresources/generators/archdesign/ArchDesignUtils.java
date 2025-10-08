package bmc.dev.resources.code.bmcresources.generators.archdesign;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.maven.project.MavenProject;

import bmc.dev.resources.code.bmcresources.config.ArchitectureConfig;
import bmc.dev.resources.code.bmcresources.config.ArchitectureEntry;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.Constants.*;
import static bmc.dev.resources.code.bmcresources.io.IOUtilities.copyResourceFile;
import static bmc.dev.resources.code.bmcresources.io.IOUtilities.createDirectoriesSafely;
import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.getMavenProject;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatBoldColor;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatColor;
import static bmc.dev.resources.code.bmcresources.utils.StringUtils.isNullOrBlank;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.BLUE;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.CYAN;
import static java.util.Optional.ofNullable;

/**
 * Utility class for handling architecture design operations.
 */
@Slf4j
@UtilityClass
public class ArchDesignUtils {

    public static Function<String, String>           getArchStructureFile     = model -> FOLDER_ARCH_MODELS + "/" + model + "/" + model + CONFIG_EXT;
    public static BiFunction<String, String, String> getMainReadmeFile        = (readme, model) -> ofNullable(readme).orElse(model + ".md");
    public static Function<String, String>           getReadmesSourceForModel = model -> FOLDER_ARCH_MODELS + "/" + model + "/" + FOLDER_TEMPLATES + "/";

    /**
     * Build the base target path for architecture-related directories based on the Maven project configuration.
     * <p>
     * The path is derived from the project's source directory, group ID (transformed into a directory structure),
     * and artifact ID (with dashes removed).
     * <p>
     * examples:
     * <br>artifactId: my-project -> myproject
     * <br>groupId: com.my.group -> com/my/group
     * <br>sourceDirectory: some/dir/project/dir/src/main/java
     * <p>
     * Resulting path: some/dir/project/dir/src/main/java/com/my/group/myproject
     *
     * @return A {@link Path} representing the base target directory path for the architecture.
     */
    public static Path buildBaseTargetPathForArch() {

        final MavenProject mavenProject = getMavenProject();

        final String artifactId      = mavenProject.getArtifactId().replace("-", "");
        final String groupId         = mavenProject.getGroupId().replace(".", "/");
        final String sourceDirectory = mavenProject.getBuild().getSourceDirectory();

        return Path.of(sourceDirectory, groupId, artifactId);
    }

    /**
     * Logs detailed information about the architecture configuration.
     *
     * @param config the {@link ArchitectureConfig} object containing parameters for the architecture structure generation:
     */
    public static void logArchitectureConfiguration(final ArchitectureConfig config) {

        final MavenProject mavenProject = getMavenProject();

        log.info(formatBoldColor.apply(BLUE, "Architecture Structure configuration:"));
        log.info(formatColor.apply(BLUE, " --- skip: [{}]"), config.isSkip());
        log.info(formatColor.apply(BLUE, " --- skipReadmes: [{}]"), config.isSkipReadme());
        log.info(formatColor.apply(BLUE, " --- model: [{}]"), config.getModel());
        log.info(formatColor.apply(BLUE, " --- mainReadme: [{}]"), config.getMainReadme());
        log.info(formatColor.apply(BLUE, " --- artifactId: [{}]"), mavenProject.getArtifactId());
        log.info(formatColor.apply(BLUE, " --- groupId: [{}]"), mavenProject.getGroupId());
        log.info(formatColor.apply(BLUE, " --- mavenSourceDirectory: [{}]"), mavenProject.getBuild().getSourceDirectory());
        log.info(formatColor.apply(BLUE, " --- archTargetDirectory: [{}]"), buildBaseTargetPathForArch());
        log.info("");
    }

    /**
     * Processes the creation of folder structures and accompanying README files for the specified
     * architecture model based on the provided configuration.
     * <p>
     * This method combines both folder and README creation in a single pass.
     *
     * @param config              the {@link ArchitectureConfig} object specifying parameters for architecture structure generation.
     * @param architectureEntries the list of {@link ArchitectureEntry} representing folders to create and associated optional README
     *                            file paths.
     * @param padding             A string used for formatting log messages, to align the log output visually.
     */
    public static void processArchitectureAndReadme(final ArchitectureConfig config, final List<ArchitectureEntry> architectureEntries, final String padding) {

        final Path   baseTargetPathForArch = buildBaseTargetPathForArch();
        final String mainDefaultReadme     = getMainReadmeFile.apply(config.getMainReadme(), config.getModel());

        architectureEntries.forEach(configEntry -> {
            final String           folder          = configEntry.folder();
            final Optional<String> readme          = configEntry.readme();
            final Path             targetDirectory = baseTargetPathForArch.resolve(folder);

            createDirectoriesSafely(targetDirectory);
            readme.ifPresent(readmeFile -> processReadme(config, folder, readmeFile));

            log.info("Created folder [{}] with readme [{}]", padding.formatted(folder), readme.orElse("none configured"));
        });

        processReadme(config, "", mainDefaultReadme);
    }

    /**
     * Processes the creation of folder structures only based on a list of architecture entries.
     *
     * @param architectureEntries A list of {@link ArchitectureEntry} objects, each representing a folder to be created.
     * @param padding             A string used for formatting log messages, to align the log output visually.
     */
    public static void processArchitectureOnly(final List<ArchitectureEntry> architectureEntries, final String padding) {

        final Path baseTargetPathForArch = buildBaseTargetPathForArch();

        architectureEntries.forEach(configEntry -> {
            final String folder          = configEntry.folder();
            final Path   targetDirectory = baseTargetPathForArch.resolve(folder);

            createDirectoriesSafely(targetDirectory);

            log.info("Created folder [{}]", padding.formatted(folder));
        });
    }

    /**
     * Processes a specific README file based on the provided configuration and paths.
     * <p>
     * This method sets up the copying of a given readme to its target folder.
     *
     * @param config       The {@link ArchitectureConfig} object that includes architecture parameters such as the model type for
     *                     determining the source directory structure.
     * @param targetFolder The target folder path where the README file should be copied. If null or empty, the file
     *                     will be copied with its original name at the base target path.
     * @param readmeFile   The name of the README file to be processed. If it is null or blank, the method
     *                     will log a message and exit without performing any action.
     */
    public static void processReadme(final ArchitectureConfig config, final String targetFolder, final String readmeFile) {

        if (isNullOrBlank.test(readmeFile)) {
            log.debug(formatColor.apply(CYAN, "Readme [{}] is null or blank. Nothing to process"), readmeFile);
            return;
        }

        final Path   baseTargetPathForArch = buildBaseTargetPathForArch();
        final String sourceResourceName    = getReadmesSourceForModel.apply(config.getModel()) + readmeFile;
        final String targetResourceName    = isNullOrBlank.test(targetFolder) ? readmeFile : targetFolder + "/" + readmeFile;

        copyResourceFile(baseTargetPathForArch, sourceResourceName, targetResourceName);
    }

}
