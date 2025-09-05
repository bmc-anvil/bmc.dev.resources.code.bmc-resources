package bmc.dev.resources.code.bmc_resources;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.maven.plugin.logging.Log;

import static bmc.dev.resources.code.bmc_resources.Constants.FOLDER_MODELS;
import static bmc.dev.resources.code.bmc_resources.Constants.FOLDER_TEMPLATES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ArchDesignReadmeWriter {

    public static void copyReadmeToModelDirectory(final Log log, final Class<? extends ArchitectureGeneratorMojo> mojoClass, final Path targetDirectory,
            final String architectureModel, final String readmeFile, final boolean architectureOverwriteReadmes) {

        if (!architectureOverwriteReadmes && Files.exists(targetDirectory.resolve(readmeFile))) {
            log.warn("Skipping copying [%s], as it exists and [%s] is set to false".formatted(readmeFile, "architecture.overwriteReadmes"));
        } else {

            final String readmeTemplateResourceFile = "/" + FOLDER_MODELS + "/" + architectureModel + "/" + FOLDER_TEMPLATES + "/" + readmeFile;

            try (final InputStream readmeFileStream = mojoClass.getResourceAsStream(readmeTemplateResourceFile)) {
                if (readmeFileStream == null) {
                    log.warn("Could not find readme template for [%s]".formatted(readmeFileStream));
                } else {
                    Files.copy(readmeFileStream, targetDirectory.resolve(readmeFile), REPLACE_EXISTING);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
