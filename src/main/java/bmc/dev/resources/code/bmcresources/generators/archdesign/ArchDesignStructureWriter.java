package bmc.dev.resources.code.bmcresources.generators.archdesign;

import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import bmc.dev.resources.code.bmcresources.properties.ArchitectureConfig;

import static bmc.dev.resources.code.bmcresources.Constants.*;
import static bmc.dev.resources.code.bmcresources.generators.archdesign.ArchDesignUtils.*;
import static bmc.dev.resources.code.bmcresources.io.ConfigFileReader.readConfigFile;
import static bmc.dev.resources.code.bmcresources.io.IOUtilities.createDirectory;
import static bmc.dev.resources.code.bmcresources.utils.StringUtils.isNullOrBlank;

public class ArchDesignStructureWriter {

    public static void processArchitecture(final AbstractMojo mojo, final MavenProject mavenProject, final ArchitectureConfig config) {

        final Log log = mojo.getLog();

        if (isNullOrBlank(config.getModel())) {
            log.warn("%sArchitecture Structure is blank, skipping.%s".formatted(COLOR_RED, COLOR_RESET));
        }
        else {

            log.info("%sArchitecture Structure creation started.%s".formatted(COLOR_YELLOW, COLOR_RESET));
            logConfiguration(mojo, config, mavenProject);

            final Path                                targetRootDir = buildTargetPathForArch(mavenProject);
            final Entry<Integer, Map<String, String>> structureMap  = readConfigFile(mojo, getArchStructure.apply(config.getModel())).orElseThrow();
            final BiConsumer<Path, String>            readmeAction  = resolveReadmeOp(mojo, config);
            final BiConsumer<String, String>          logAction     = resolveLogOp( mojo, config.isSkipReadme(), structureMap.getKey());

            structureMap.getValue().forEach((folder, readme) -> {
                final Path targetDirectory = targetRootDir.resolve(folder);

                createDirectory(mojo, targetDirectory);
                readmeAction.accept(targetDirectory, readme);
                logAction.accept(folder, readme);
            });

            readmeAction.accept(targetRootDir, getMainReadme.apply(config.getMainReadme(), config.getModel()));
        }

        log.info("%sArchitecture Structure creation completed.%s".formatted(COLOR_YELLOW, COLOR_RESET));
    }

}
