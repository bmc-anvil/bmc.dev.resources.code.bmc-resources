package bmc.dev.resources.code.bmcresources.maven;

import java.nio.file.Path;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.Constants.*;
import static bmc.dev.resources.code.bmcresources.io.IOUtilities.*;
import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileUtils.findPropertyIndex;
import static bmc.dev.resources.code.bmcresources.maven.MavenProjectInjector.GetMavenProject;

@Slf4j
public class MavenConfigFileWriter {

    private MavenConfigFileWriter() {}

    public static void writeMavenProperty(final String propertyKey, final String propertyValue) {

        final Path   mavenConfigPath           = GetMavenProject().getBasedir().toPath().resolve(".mvn", "maven.config");
        final String propertyWithPrefix        = MVN_PREFIX + propertyKey;
        final String updatedPropertyWithPrefix = propertyWithPrefix + "=" + propertyValue;

        createDirectory(mavenConfigPath.getParent());

        final List<String> lines = readAllLinesFromFile(mavenConfigPath);

        log.debug("lines before filtering: {}", lines);

        if (lines.isEmpty()) {
            lines.add(updatedPropertyWithPrefix);
        }
        else {
            findPropertyIndex.apply(lines, propertyWithPrefix)
                             .ifPresentOrElse(index -> lines.set(index, updatedPropertyWithPrefix), () -> lines.add(updatedPropertyWithPrefix));
        }

        log.debug("lines after filtering: {}", lines);

        writeAllLinesToFile(lines, mavenConfigPath);

        log.info("{}{}Updated [{}] with [{}]=true{}", COLOR_BOLD, COLOR_RED, mavenConfigPath, MVN_PREFIX + propertyKey, COLOR_RESET);
    }

}
