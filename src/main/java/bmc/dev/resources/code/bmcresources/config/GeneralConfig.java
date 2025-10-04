package bmc.dev.resources.code.bmcresources.config;

import org.apache.maven.plugins.annotations.Parameter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Configuration class for defining general parameters related to plugin execution.
 */
@Getter
@Setter
@NoArgsConstructor
public class GeneralConfig {

    /**
     * Skip the plugin execution altogether.
     */
    @Parameter
    private boolean skip = false;

}
