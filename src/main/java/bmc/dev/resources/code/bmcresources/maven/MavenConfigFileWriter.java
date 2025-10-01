package bmc.dev.resources.code.bmcresources.maven;

import java.nio.file.Path;
import java.util.List;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.Constants.MVN_PREFIX;
import static bmc.dev.resources.code.bmcresources.io.IOUtilities.*;
import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileUtils.findPropertyIndex;
import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.getMavenProject;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatBoldColor;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatColor;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.CYAN;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.RED;

@Slf4j
@UtilityClass
public class MavenConfigFileWriter {

    public static void writeMavenProperty(final String propertyKey, final String propertyValue) {

        final Path   mavenConfigPath           = getMavenProject().getBasedir().toPath().resolve(".mvn", "maven.config");
        final String propertyWithPrefix        = MVN_PREFIX + propertyKey;
        final String updatedPropertyWithPrefix = propertyWithPrefix + "=" + propertyValue;

        createDirectory(mavenConfigPath.getParent());

        final List<String> lines = readAllLinesFromFile(mavenConfigPath);

        log.debug(formatColor.apply(CYAN, "lines before filtering: [{}]"), lines);

        findPropertyIndex.apply(lines, propertyWithPrefix)
                         .ifPresentOrElse(index -> lines.set(index, updatedPropertyWithPrefix), () -> lines.add(updatedPropertyWithPrefix));

        log.debug(formatColor.apply(CYAN, "lines after filtering: [{}]"), lines);

        writeAllLinesToFile(lines, mavenConfigPath);

        log.info(formatBoldColor.apply(RED, "Updated [{}] with [{}=true]"), mavenConfigPath, MVN_PREFIX + propertyKey);
    }

}
