package bmc.dev.resources.code.bmc_resources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.maven.plugin.logging.Log;

import static bmc.dev.resources.code.bmc_resources.ArchDesignReadmeWriter.copyReadmeToModelDirectory;
import static java.nio.file.Paths.get;
import static java.util.Optional.ofNullable;

public class ArchDesignStructureWriter {

    public static void createArchitectureStructure(final Log log, final Class<? extends ArchitectureGeneratorMojo> mojoClass, final String targetRootDirectory,
            final Entry<Integer, Map<String, String>> maxFolderLengthAndModelMap, final String architectureMainReadme, final String architectureModel,
            final boolean architectureOverwriteReadmes) {

        final String architectureReadmeFile = ofNullable(architectureMainReadme).orElse(architectureModel + ".md");
        final String padding                = "%-" + maxFolderLengthAndModelMap.getKey() + "s";

        maxFolderLengthAndModelMap.getValue().forEach((folder, readme) -> {
            final Path   targetDirectory = get(targetRootDirectory, folder);
            final String folderPadded    = padding.formatted(folder == null ? "" : folder);

            createDirectory(targetDirectory);
            log.info("Created folder [%s] with doc [%s]".formatted(folderPadded, readme));
            ofNullable(readme).ifPresent(
                    readmeFile -> copyReadmeToModelDirectory(log, mojoClass, targetDirectory, architectureModel, readmeFile, architectureOverwriteReadmes));

        });

        copyReadmeToModelDirectory(log, mojoClass, get(targetRootDirectory), architectureModel, architectureReadmeFile, architectureOverwriteReadmes);
    }

    private static void createDirectory(final Path targetDirectory) {

        try {
            Files.createDirectories(targetDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
