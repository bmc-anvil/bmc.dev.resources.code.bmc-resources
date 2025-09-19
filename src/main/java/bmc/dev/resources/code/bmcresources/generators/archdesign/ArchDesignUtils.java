package bmc.dev.resources.code.bmcresources.generators.archdesign;

import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.maven.project.MavenProject;

import bmc.dev.resources.code.bmcresources.config.ArchitectureConfig;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.Constants.*;
import static bmc.dev.resources.code.bmcresources.generators.archdesign.ArchDesignReadmeWriter.copyReadme;
import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.getMavenProject;
import static bmc.dev.resources.code.bmcresources.utils.BMCConfigFileUtils.calculatePadding;
import static bmc.dev.resources.code.bmcresources.utils.LogFormattingUtils.formatBlue;
import static bmc.dev.resources.code.bmcresources.utils.LogFormattingUtils.formatBlueBold;
import static java.util.Optional.ofNullable;

@Slf4j
public class ArchDesignUtils {

    public static Function<String, String>           getArchStructure          = model -> FOLDER_ARCH_MODELS + "/" + model + "/" + model + CONFIG_EXT;
    public static BiFunction<String, String, String> getMainReadme             = (readme, model) -> ofNullable(readme).orElse(model + ".md");
    public static Function<String, String>           getReadmesSourceDirectory = model -> FOLDER_ARCH_MODELS + "/" + model + FOLDER_TEMPLATES + "/";

    private ArchDesignUtils() {}

    public static Path buildTargetPathForArch() {

        final MavenProject mavenProject = getMavenProject();

        final String artifactId      = mavenProject.getArtifactId().replace("-", "");
        final String groupId         = mavenProject.getGroupId().replace(".", "/");
        final String sourceDirectory = mavenProject.getBuild().getSourceDirectory();

        return Path.of(sourceDirectory, groupId, artifactId);
    }

    public static void logConfiguration(final ArchitectureConfig config) {

        final MavenProject mavenProject = getMavenProject();

        log.info(formatBlueBold("Architecture Structure configuration:"));
        log.info(formatBlue(" --- skip: [{}]"), config.isSkip());
        log.info(formatBlue(" --- skipReadmes: [{}]"), config.isSkipReadme());
        log.info(formatBlue(" --- model: [{}]"), config.getModel());
        log.info(formatBlue(" --- mainReadme: [{}]"), config.getMainReadme());
        log.info(formatBlue(" --- artifactId: [{}]"), mavenProject.getArtifactId());
        log.info(formatBlue(" --- groupId: [{}]"), mavenProject.getGroupId());
        log.info(formatBlue(" --- mavenSourceDirectory: [{}]"), mavenProject.getBuild().getSourceDirectory());
        log.info(formatBlue(" --- archTargetDirectory: [{}]"), buildTargetPathForArch());
        log.info("");
    }

    public static BiConsumer<String, String> resolveLogOp(final boolean skip, final Integer maxFolderNameLength) {

        final String padding = calculatePadding.apply(maxFolderNameLength);

        return skip ? (folder, _) -> log.info("Created folder [{}]", padding.formatted(folder))
                    : (folder, readme) -> log.info("Created folder [{}] with readme [{}]", padding.formatted(folder), readme);
    }

    public static BiConsumer<Path, String> resolveReadmeOp(final ArchitectureConfig config) {

        final String readmesSourceDir = getReadmesSourceDirectory.apply(config.getModel());

        return config.isSkipReadme() ? (_, _) -> {/* no-op */}
                                     : (targetPath, readmeFile) -> copyReadme(targetPath, readmesSourceDir, readmeFile);
    }

}
