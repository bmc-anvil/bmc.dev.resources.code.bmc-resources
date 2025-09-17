package bmc.dev.resources.code.bmcresources.generators.archdesign;

import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import bmc.dev.resources.code.bmcresources.properties.ArchitectureConfig;

import static bmc.dev.resources.code.bmcresources.Constants.*;
import static bmc.dev.resources.code.bmcresources.utils.BMCConfigFileUtils.calculatePadding;
import static bmc.dev.resources.code.bmcresources.generators.archdesign.ArchDesignReadmeWriter.copyReadme;
import static java.util.Optional.ofNullable;

public class ArchDesignUtils {

    public static Function<String, String>           getArchStructure          = model -> FOLDER_ARCH_MODELS + "/" + model + "/" + model + CONFIG_EXT;
    public static BiFunction<String, String, String> getMainReadme             = (readme, model) -> ofNullable(readme).orElse(model + ".md");
    public static Function<String, String>           getReadmesSourceDirectory = model -> FOLDER_ARCH_MODELS + "/" + model + FOLDER_TEMPLATES + "/";

    public static Path buildTargetPathForArch(final MavenProject mavenProject) {

        final String artifactId      = mavenProject.getArtifactId().replace("-", "");
        final String groupId         = mavenProject.getGroupId().replace(".", "/");
        final String sourceDirectory = mavenProject.getBuild().getSourceDirectory();

        return Path.of(sourceDirectory, groupId, artifactId);
    }

    public static void logConfiguration(final AbstractMojo mojo, final ArchitectureConfig config, final MavenProject mavenProject) {

        final Log log = mojo.getLog();

        log.info("%s%sArchitecture Structure configuration:%s".formatted(COLOR_BLUE, COLOR_BOLD, COLOR_RESET));
        log.info("%s --- skip: %s%s".formatted(COLOR_BLUE, config.isSkip(), COLOR_RESET));
        log.info("%s --- skipReadmes: %s%s".formatted(COLOR_BLUE, config.isSkipReadme(), COLOR_RESET));
        log.info("%s --- model: %s%s".formatted(COLOR_BLUE, config.getModel(), COLOR_RESET));
        log.info("%s --- mainReadme: %s%s".formatted(COLOR_BLUE, config.getMainReadme(), COLOR_RESET));
        log.info("%s --- artifactId: %s%s".formatted(COLOR_BLUE, mavenProject.getArtifactId(), COLOR_RESET));
        log.info("%s --- groupId: %s%s".formatted(COLOR_BLUE, mavenProject.getGroupId(), COLOR_RESET));
        log.info("%s --- mavenSourceDirectory: %s%s".formatted(COLOR_BLUE, mavenProject.getBuild().getSourceDirectory(), COLOR_RESET));
        log.info("%s --- archTargetDirectory: %s%s".formatted(COLOR_BLUE, buildTargetPathForArch(mavenProject), COLOR_RESET));
        log.info("");
    }

    public static BiConsumer<String, String> resolveLogOp(final AbstractMojo mojo, final boolean skip, final Integer maxFolderNameLength) {

        final String padding = calculatePadding.apply(maxFolderNameLength);

        return skip ? (folder, _) -> mojo.getLog().info("Created folder [%s]".formatted(padding.formatted(folder)))
                    : (folder, readme) -> mojo.getLog().info("Created folder [%s] with readme [%s]".formatted(padding.formatted(folder), readme));
    }

    public static BiConsumer<Path, String> resolveReadmeOp(final AbstractMojo mojo, final ArchitectureConfig config) {

        final String readmesSourceDir = getReadmesSourceDirectory.apply(config.getModel());

        return config.isSkipReadme() ? (_, _) -> {/* no-op */}
                                     : (targetPath, readmeFile) -> copyReadme(mojo, targetPath, readmesSourceDir, config, readmeFile);
    }

}
