package bmc.dev.resources.code.bmcresources.generators.archdesign;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import bmc.dev.resources.code.bmcresources.config.ArchitectureConfig;
import bmc.dev.resources.code.bmcresources.config.ArchitectureEntry;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.generators.archdesign.ArchDesignUtils.*;
import static bmc.dev.resources.code.bmcresources.io.ConfigFileReader.readArchitectureModelFile;
import static bmc.dev.resources.code.bmcresources.io.IOUtilities.createDirectory;
import static bmc.dev.resources.code.bmcresources.utils.BMCConfigFileUtils.getMaxStringLength;
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

            final Path                       baseTargetPathForArch = buildBaseTargetPathForArch();
            final List<ArchitectureEntry>    configEntries         = readArchitectureModelFile(getArchStructure.apply(config.getModel()));
            final BiConsumer<String, String> readmeAction          = resolveReadmeOp(config, baseTargetPathForArch);
            final Integer                    maxFolderLength       = getMaxStringLength.apply(configEntries.stream().map(ArchitectureEntry::folder).toList());
            final BiConsumer<String, String> logAction             = resolveLogOp(config.isSkipReadme(), maxFolderLength);

            configEntries.forEach(configEntry -> {
                final String           folder          = configEntry.folder();
                final Optional<String> readme          = configEntry.readme();
                final Path             targetDirectory = baseTargetPathForArch.resolve(folder);

                createDirectory(targetDirectory);

                readme.filter(isNullOrBlank)
                      .ifPresentOrElse(
                              readmeFile -> readmeAction.accept(folder, readmeFile),
                              () -> log.debug(formatColor.apply(CYAN, "Creation is either disabled or [{}] is null or blank."), readme));

                logAction.accept(folder, readme.orElse("none configured"));
            });

            readmeAction.accept("", getMainReadme.apply(config.getMainReadme(), config.getModel()));
        }

        log.info(formatColor.apply(YELLOW, "Architecture Structure creation completed."));
    }

}
