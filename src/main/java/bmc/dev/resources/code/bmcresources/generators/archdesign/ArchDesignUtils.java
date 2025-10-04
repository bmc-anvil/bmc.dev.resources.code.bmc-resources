package bmc.dev.resources.code.bmcresources.generators.archdesign;

import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.maven.project.MavenProject;

import bmc.dev.resources.code.bmcresources.config.ArchitectureConfig;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.Constants.*;
import static bmc.dev.resources.code.bmcresources.io.IOUtilities.copyResourceSingle;
import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.getMavenProject;
import static bmc.dev.resources.code.bmcresources.utils.BMCConfigFileUtils.calculateLeftAlignedPadding;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatBoldColor;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatColor;
import static bmc.dev.resources.code.bmcresources.utils.StringUtils.isNullOrBlank;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.BLUE;
import static java.util.Optional.ofNullable;

@Slf4j
@UtilityClass
public class ArchDesignUtils {

    public static Function<String, String>           getArchStructure          = model -> FOLDER_ARCH_MODELS + "/" + model + "/" + model + CONFIG_EXT;
    public static BiFunction<String, String, String> getMainReadme             = (readme, model) -> ofNullable(readme).orElse(model + ".md");
    public static Function<String, String>           getReadmesSourceDirectory = model -> FOLDER_ARCH_MODELS + "/" + model + "/" + FOLDER_TEMPLATES + "/";

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

    public static BiConsumer<String, String> resolveLogOp(final boolean skip, final Integer maxFolderNameLength) {

        final String padding = calculateLeftAlignedPadding.apply(maxFolderNameLength);

        return skip ? (folder, _) -> log.info("Created folder [{}]", padding.formatted(folder))
                    : (folder, readme) -> log.info("Created folder [{}] with readme [{}]", padding.formatted(folder), readme);
    }

    public static BiConsumer<String, String> resolveReadmeOp(final ArchitectureConfig config, final Path baseTargetPathForArch) {

        final String readmesSource = getReadmesSourceDirectory.apply(config.getModel());

        return config.isSkipReadme() ? (_, _) -> {/* no-op */}
                                     : (targetFolder, readmeFile) ->
                       copyResourceSingle(baseTargetPathForArch, readmesSource + readmeFile,
                                          isNullOrBlank.test(targetFolder) ? readmeFile : targetFolder + "/" + readmeFile);
    }

}
