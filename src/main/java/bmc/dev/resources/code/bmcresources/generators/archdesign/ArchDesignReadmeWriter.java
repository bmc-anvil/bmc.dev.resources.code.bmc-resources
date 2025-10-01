package bmc.dev.resources.code.bmcresources.generators.archdesign;

import java.nio.file.Path;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.io.IOUtilities.copySingleResource;
import static bmc.dev.resources.code.bmcresources.utils.LogFormatUtils.formatColor;
import static bmc.dev.resources.code.bmcresources.utils.StringUtils.isNullOrBlank;
import static bmc.dev.resources.code.bmcresources.utils.TerminalColors.CYAN;

/**
 * Class containing methods to write readmes to disk.
 *
 */
@Slf4j
@UtilityClass
public class ArchDesignReadmeWriter {

    /**
     * Copies the specified readme file from the source directory to the target path.
     * <p>
     * If the readme file name is null or empty, the method logs a debug message and does not proceed with copying.
     *
     * @param targetPath the destination path where the readme file should be copied
     * @param sourceDir  the directory containing the source readme file
     * @param readme     the name of the readme file to be copied
     */
    public static void copyReadme(final Path targetPath, final String sourceDir, final String readme) {

        if (isNullOrBlank.test(readme)) {
            log.debug(formatColor.apply(CYAN, "Creation is either disabled or [{}] is null or blank."), readme);
        }
        else {
            copySingleResource(sourceDir, targetPath, readme);
        }
    }

}
