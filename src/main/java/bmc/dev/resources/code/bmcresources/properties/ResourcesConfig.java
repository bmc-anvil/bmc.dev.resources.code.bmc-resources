package bmc.dev.resources.code.bmcresources.properties;

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
     * Skip the resources' execution.
     */
    @Parameter
    private boolean skip                   = false;

}
