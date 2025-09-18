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
import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.GetMavenProject;
import static bmc.dev.resources.code.bmcresources.utils.BMCConfigFileUtils.calculatePadding;
import static java.util.Optional.ofNullable;

@Slf4j
public class ArchDesignUtils {

    public static Function<String, String>           getArchStructure          = model -> FOLDER_ARCH_MODELS + "/" + model + "/" + model + CONFIG_EXT;
    public static BiFunction<String, String, String> getMainReadme             = (readme, model) -> ofNullable(readme).orElse(model + ".md");
    public static Function<String, String>           getReadmesSourceDirectory = model -> FOLDER_ARCH_MODELS + "/" + model + FOLDER_TEMPLATES + "/";

    private ArchDesignUtils() {}

    public static Path buildTargetPathForArch() {

        final MavenProject mavenProject = GetMavenProject();

        final String artifactId      = mavenProject.getArtifactId().replace("-", "");
        final String groupId         = mavenProject.getGroupId().replace(".", "/");
        final String sourceDirectory = mavenProject.getBuild().getSourceDirectory();

        return Path.of(sourceDirectory, groupId, artifactId);
    }

    public static void logConfiguration(final ArchitectureConfig config) {

        final MavenProject mavenProject = GetMavenProject();

        log.info("{}{}Architecture Structure configuration:{}", COLOR_BLUE, COLOR_BOLD, COLOR_RESET);
        log.info("{} --- skip: {}{}", COLOR_BLUE, config.isSkip(), COLOR_RESET);
        log.info("{} --- skipReadmes: {}{}", COLOR_BLUE, config.isSkipReadme(), COLOR_RESET);
        log.info("{} --- model: {}{}", COLOR_BLUE, config.getModel(), COLOR_RESET);
        log.info("{} --- mainReadme: {}{}", COLOR_BLUE, config.getMainReadme(), COLOR_RESET);
        log.info("{} --- artifactId: {}{}", COLOR_BLUE, mavenProject.getArtifactId(), COLOR_RESET);
        log.info("{} --- groupId: {}{}", COLOR_BLUE, mavenProject.getGroupId(), COLOR_RESET);
        log.info("{} --- mavenSourceDirectory: {}{}", COLOR_BLUE, mavenProject.getBuild().getSourceDirectory(), COLOR_RESET);
        log.info("{} --- archTargetDirectory: {}{}", COLOR_BLUE, buildTargetPathForArch(), COLOR_RESET);
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
