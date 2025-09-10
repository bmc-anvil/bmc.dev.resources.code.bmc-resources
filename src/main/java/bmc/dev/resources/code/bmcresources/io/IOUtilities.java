package bmc.dev.resources.code.bmcresources.io;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.maven.plugin.AbstractMojo;

import static java.nio.file.Files.createDirectories;

public class IOUtilities {

    public static void createDirectory(final AbstractMojo mojoClass, final Path targetDirectory) {

        try {
            createDirectories(targetDirectory);
        }
        catch (final IOException e) {
            mojoClass.getLog().error("could not create directory [%s]".formatted(targetDirectory), e);
        }
    }

}
