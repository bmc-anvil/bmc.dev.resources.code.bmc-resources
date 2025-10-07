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

@Slf4j
@UtilityClass
public class ArchDesignUtils {

    public static Function<String, String>           getArchStructureFile     = model -> FOLDER_ARCH_MODELS + "/" + model + "/" + model + CONFIG_EXT;
    public static BiFunction<String, String, String> getMainReadmeFile        = (readme, model) -> ofNullable(readme).orElse(model + ".md");
    public static Function<String, String>           getReadmesSourceForModel = model -> FOLDER_ARCH_MODELS + "/" + model + "/" + FOLDER_TEMPLATES + "/";

    public static Path buildBaseTargetPathForArch() {

        final MavenProject mavenProject = getMavenProject();

        final String artifactId      = mavenProject.getArtifactId().replace("-", "");
        final String groupId         = mavenProject.getGroupId().replace(".", "/");
        final String sourceDirectory = mavenProject.getBuild().getSourceDirectory();

        return Path.of(sourceDirectory, groupId, artifactId);
    }

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

    public static void processArchitectureAndReadme(final ArchitectureConfig config, final List<ArchitectureEntry> architectureEntries,
            final String padding) {

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

    public static void processArchitectureOnly(final List<ArchitectureEntry> architectureEntries, final String padding) {

        final Path baseTargetPathForArch = buildBaseTargetPathForArch();

        architectureEntries.forEach(configEntry -> {
            final String folder          = configEntry.folder();
            final Path   targetDirectory = baseTargetPathForArch.resolve(folder);

            createDirectoriesSafely(targetDirectory);

            log.info("Created folder [{}]", padding.formatted(folder));
        });
    }

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
