package bmc.dev.resources.code.bmcresources.generators.archdesign;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;

import bmc.dev.resources.code.bmcresources.config.ArchitectureConfig;

import static bmc.dev.resources.code.bmcresources.Constants.COLOR_GREEN;
import static bmc.dev.resources.code.bmcresources.Constants.COLOR_RESET;
import static bmc.dev.resources.code.bmcresources.textutils.StringUtils.isNullOrBlank;
import static java.nio.file.Files.copy;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ArchDesignReadmeWriter {

    public static void copyReadme(final AbstractMojo mojoClass, final Path targetDirectory, final String readmeSourceDirectory, final ArchitectureConfig config,
            final String readme) {

        final Log log = mojoClass.getLog();

        if (config.isSkipReadme() || isNullOrBlank(readme)) {
            log.debug("%sSkipping readme creation for readme [%s]. Creation is either disabled or readme is null or blank.%s"
                              .formatted(COLOR_GREEN, readme, COLOR_RESET));
        }
        else {

            final String fullReadmeResourcePath = readmeSourceDirectory + readme;

            try (final InputStream readmeFileStream = mojoClass.getClass().getResourceAsStream(fullReadmeResourcePath)) {
                if (readmeFileStream == null) {
                    log.warn("Could not find readme template for [%s]".formatted(fullReadmeResourcePath));
                }
                else {
                    copy(readmeFileStream, targetDirectory.resolve(readme), REPLACE_EXISTING);
                }
            }
            catch (final IOException ioe) {
                log.error("Error copying architecture readme: [%s]".formatted(readme), ioe);
            }
        }
    }

}
