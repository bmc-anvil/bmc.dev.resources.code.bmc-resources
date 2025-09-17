package bmc.dev.resources.code.bmcresources.generators.archdesign;

import java.nio.file.Path;

import org.apache.maven.plugin.AbstractMojo;

import bmc.dev.resources.code.bmcresources.properties.ArchitectureConfig;

import static bmc.dev.resources.code.bmcresources.Constants.COLOR_GREEN;
import static bmc.dev.resources.code.bmcresources.Constants.COLOR_RESET;
import static bmc.dev.resources.code.bmcresources.io.IOUtilities.copySingleResource;
import static bmc.dev.resources.code.bmcresources.utils.StringUtils.isNullOrBlank;

public class ArchDesignReadmeWriter {

    public static void copyReadme(final AbstractMojo mojo, final Path targetPath, final String sourceDir, final ArchitectureConfig config,
            final String readme) {

        if (config.isSkipReadme() || isNullOrBlank(readme)) {
            mojo.getLog().debug(("%sCreation is either disabled or [%s] is null or blank.%s").formatted(COLOR_GREEN, readme, COLOR_RESET));
        }
        else {
            copySingleResource(mojo, sourceDir, targetPath, readme);
        }
    }

}
