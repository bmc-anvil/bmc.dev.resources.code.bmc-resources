package bmc.dev.resources.code.bmcresources.maven;

import java.nio.file.Path;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import static bmc.dev.resources.code.bmcresources.Constants.*;
import static bmc.dev.resources.code.bmcresources.io.IOUtilities.*;
import static bmc.dev.resources.code.bmcresources.maven.MavenConfigFileUtils.findPropertyIndex;

public class MavenConfigFileWriter {


    public static void writeMavenProperty(final AbstractMojo mojo, final String propertyKey, final String propertyValue, final MavenProject mavenProject) {

        final Log    log                       = mojo.getLog();
        final Path   mavenConfigPath           = mavenProject.getBasedir().toPath().resolve(".mvn", "maven.config");
        final String propertyWithPrefix        = MVN_PREFIX + propertyKey;
        final String updatedPropertyWithPrefix = propertyWithPrefix + "=" + propertyValue;

        createDirectory(mojo, mavenConfigPath.getParent());

        final List<String> lines = readAllLinesFromFile(mojo, mavenConfigPath);

        log.debug("lines before filtering: " + lines);

        if (lines.isEmpty()) {
            lines.add(updatedPropertyWithPrefix);
        }
        else {
            findPropertyIndex.apply(lines, propertyWithPrefix)
                             .ifPresentOrElse(index -> lines.set(index, updatedPropertyWithPrefix),
                                                 () -> lines.add(updatedPropertyWithPrefix));
        }

        log.debug("lines after filtering: " + lines);

        writeAllLinesToFile(mojo, lines, mavenConfigPath);

        log.info("%s%sUpdated [%s] with [%s]=true%s".formatted(COLOR_BOLD, COLOR_RED, mavenConfigPath, MVN_PREFIX + propertyKey, COLOR_RESET));
    }

}
