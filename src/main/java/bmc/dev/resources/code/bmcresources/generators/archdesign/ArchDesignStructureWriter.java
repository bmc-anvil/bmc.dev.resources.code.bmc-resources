package bmc.dev.resources.code.bmcresources.generators.archdesign;

import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import bmc.dev.resources.code.bmcresources.config.ArchitectureConfig;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.Constants.*;
import static bmc.dev.resources.code.bmcresources.generators.archdesign.ArchDesignUtils.*;
import static bmc.dev.resources.code.bmcresources.io.ConfigFileReader.readConfigFile;
import static bmc.dev.resources.code.bmcresources.io.IOUtilities.createDirectory;
import static bmc.dev.resources.code.bmcresources.utils.StringUtils.isNullOrBlank;

@Slf4j
public class ArchDesignStructureWriter {

    private ArchDesignStructureWriter() {}

    public static void processArchitecture(final ArchitectureConfig config) {

        if (isNullOrBlank.test(config.getModel())) {
            log.warn("{}Architecture Structure is blank, skipping.{}", COLOR_RED, COLOR_RESET);
        }
        else {

            log.info("{}Architecture Structure creation started.{}", COLOR_YELLOW, COLOR_RESET);
            logConfiguration(config);

            final Path                                targetRootDir = buildTargetPathForArch();
            final Entry<Integer, Map<String, String>> structureMap  = readConfigFile(getArchStructure.apply(config.getModel())).orElseThrow();
            final BiConsumer<Path, String>            readmeAction  = resolveReadmeOp(config);
            final BiConsumer<String, String>          logAction     = resolveLogOp(config.isSkipReadme(), structureMap.getKey());

            structureMap.getValue().forEach((folder, readme) -> {
                final Path targetDirectory = targetRootDir.resolve(folder);

                createDirectory(targetDirectory);
                readmeAction.accept(targetDirectory, readme);
                logAction.accept(folder, readme);
            });

            readmeAction.accept(targetRootDir, getMainReadme.apply(config.getMainReadme(), config.getModel()));
        }

        log.info("{}Architecture Structure creation completed.{}", COLOR_YELLOW, COLOR_RESET);
    }

}
