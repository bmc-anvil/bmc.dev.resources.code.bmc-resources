package bmc.dev.resources.code.bmcresources.properties;

import org.apache.maven.plugins.annotations.Parameter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Configuration class for defining the architecture structure generation parameters.
 * <br>This class is used to set up the configuration for generating the folder structure and readme files for a given
 * architecture model.
 * <p>
 * The following parameters are available:
 * <pre>
 * - mainReadme: Specifies the name of the main README file for the architecture.
 * - model: Defines the architecture model to be used for structure creation.
 * - overwriteReadmes: Determines if existing readme files should be overwritten.
 * - skip: Enables skipping the entire architecture structure generation process.
 * - skipReadme: Allows skipping the generation of architecture readme files.
 * </pre>
 */
@Getter
@Setter
@NoArgsConstructor
public class ArchitectureConfig {

    /**
     * The name of the architecture's main README file, placed at the root level of the generated folder structure.
     */
    @Parameter
    private String  mainReadme;
    /**
     * The architecture's model to use for folder structure generation.
     */
    @Parameter
    private String  model;
    /**
     * Skip the architecture generation.
     */
    @Parameter
    private boolean skip       = false;
    /**
     * Skip the architecture readme files generation.
     */
    @Parameter
    private boolean skipReadme = false;

}
