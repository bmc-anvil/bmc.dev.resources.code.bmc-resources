package bmc.dev.resources.code.bmcresources.generators.archdesign;

import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import bmc.dev.resources.code.bmcresources.config.ArchitectureConfig;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.generators.archdesign.ArchDesignUtils.*;
import static bmc.dev.resources.code.bmcresources.io.ConfigFileReader.readConfigFile;
import static bmc.dev.resources.code.bmcresources.io.IOUtilities.createDirectory;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatBoldColor;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatColor;
import static bmc.dev.resources.code.bmcresources.utils.StringUtils.isNullOrBlank;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.CYAN;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.YELLOW;

@Slf4j
@UtilityClass
public class ArchDesignStructureWriter {

    public static void processArchitecture(final ArchitectureConfig config) {

        if (isNullOrBlank.test(config.getModel())) {
            log.warn(formatBoldColor.apply(YELLOW, ("Architecture Structure is blank, skipping.")));
        } else {

            log.info(formatColor.apply(YELLOW, ("Architecture Structure creation started.")));
            logArchitectureConfiguration(config);

            final Path                                baseTargetPathForArch = buildBaseTargetPathForArch();
            final Entry<Integer, Map<String, String>> structureMap          = readConfigFile(getArchStructure.apply(config.getModel())).orElseThrow();
            final BiConsumer<String, String>          readmeAction          = resolveReadmeOp(config, baseTargetPathForArch);
            final BiConsumer<String, String>          logAction             = resolveLogOp(config.isSkipReadme(), structureMap.getKey());

            structureMap.getValue().forEach((folder, readme) -> {
                final Path targetDirectory = baseTargetPathForArch.resolve(folder);

                createDirectory(targetDirectory);

                if (isNullOrBlank.test(readme)) {
                    log.debug(formatColor.apply(CYAN, "Creation is either disabled or [{}] is null or blank."), readme);
                } else {
                    readmeAction.accept(folder, readme);
                }

                logAction.accept(folder, readme);
            });

            readmeAction.accept("", getMainReadme.apply(config.getMainReadme(), config.getModel()));
        }

        log.info(formatColor.apply(YELLOW, "Architecture Structure creation completed."));
    }

}
