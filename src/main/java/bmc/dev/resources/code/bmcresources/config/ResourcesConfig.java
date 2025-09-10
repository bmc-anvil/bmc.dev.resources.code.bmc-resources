package bmc.dev.resources.code.bmcresources.config;

import org.apache.maven.plugins.annotations.Parameter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Configuration class for defining parameters related to resource management during the plugin execution.
 */
@Getter
@Setter
@NoArgsConstructor
public class ResourcesConfig {

    /**
     * Forces overwriting every bmc-managed resource with template defaults.
     */
    @Parameter
    private boolean overwriteUserResources = false;
    /**
     * Skip the plugin execution altogether.
     */
    @Parameter
    private boolean skip                   = false;

}
