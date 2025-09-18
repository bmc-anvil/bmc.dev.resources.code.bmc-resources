package bmc.dev.resources.code.bmcresources.config;

import org.apache.maven.plugins.annotations.Parameter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralConfig {

    /**
     * Skip the plugin execution altogether.
     */
    @Parameter
    private boolean skip = false;

}
