package bmc.dev.resources.code.bmcresources.generators.archdesign;

import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import bmc.dev.resources.code.bmcresources.config.ArchitectureConfig;

import static bmc.dev.resources.code.bmcresources.Constants.COLOR_RESET;
import static bmc.dev.resources.code.bmcresources.Constants.COLOR_YELLOW;
import static bmc.dev.resources.code.bmcresources.generators.archdesign.ArchDesignReadmeWriter.copyReadme;
import static bmc.dev.resources.code.bmcresources.generators.archdesign.ArchDesignUtils.*;
import static bmc.dev.resources.code.bmcresources.io.ConfigFileReader.readConfigFile;
import static bmc.dev.resources.code.bmcresources.io.ConfigFileUtilities.calculatePadding;
import static bmc.dev.resources.code.bmcresources.io.IOUtilities.createDirectory;
import static bmc.dev.resources.code.bmcresources.textutils.StringUtils.isNullOrBlank;

public class ArchDesignStructureWriter {

    public static void createArchitectureStructure(final AbstractMojo mojoClass, final MavenProject mavenProject, final ArchitectureConfig config) {

        final Log log = mojoClass.getLog();

        if (isNullOrBlank(config.getModel())) {
            log.warn("Architecture Structure is blank, skipping.");
        }
        else {

            log.info("%sArchitecture Structure creation started.%s".formatted(COLOR_YELLOW, COLOR_RESET));
            logConfiguration(mojoClass, config, mavenProject);

            final String                              readmesSourceDir = getReadmesSourceDirectory.apply(config.getModel());
            final Path                                targetRootDir    = buildTargetPathForArch(mavenProject);
            final Entry<Integer, Map<String, String>> structureMap     = readConfigFile(mojoClass, getArchStructure.apply(config.getModel())).orElseThrow();
            final String                              padding          = calculatePadding.apply(structureMap.getKey());

            structureMap.getValue().forEach((folder, readme) -> {
                final Path   targetDirectory = targetRootDir.resolve(folder);
                final String folderPadded    = padding.formatted(folder);

                createDirectory(mojoClass, targetDirectory);
                log.info("Created folder [%s] with doc [%s]".formatted(folderPadded, readme));
                copyReadme(mojoClass, targetDirectory, readmesSourceDir, config, readme);

            });

            copyReadme(mojoClass, targetRootDir, readmesSourceDir, config, getMainReadme.apply(config.getMainReadme(), config.getModel()));

            log.info("%sArchitecture Structure creation completed.%s".formatted(COLOR_YELLOW, COLOR_RESET));
        }
    }

}
