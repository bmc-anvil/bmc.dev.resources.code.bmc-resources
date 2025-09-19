package bmc.dev.resources.code.bmcresources.generators.archdesign;

import java.nio.file.Path;

import lombok.extern.slf4j.Slf4j;

import static bmc.dev.resources.code.bmcresources.io.IOUtilities.copySingleResource;
import static bmc.dev.resources.code.bmcresources.utils.LogFormattingUtils.formatCyan;
import static bmc.dev.resources.code.bmcresources.utils.StringUtils.isNullOrBlank;

@Slf4j
public class ArchDesignReadmeWriter {

    private ArchDesignReadmeWriter() {}

    public static void copyReadme(final Path targetPath, final String sourceDir, final String readme) {

        if (isNullOrBlank.test(readme)) {
            log.debug(formatCyan("Creation is either disabled or [{}] is null or blank."), readme);
        }
        else {
            copySingleResource(sourceDir, targetPath, readme);
        }
    }

}
