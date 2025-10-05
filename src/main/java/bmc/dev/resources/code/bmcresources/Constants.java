package bmc.dev.resources.code.bmcresources;

import lombok.experimental.UtilityClass;

/**
 * Constant values used across the application.
 * This class centralizes the definition of various string literals and configuration-related constants.
 */
@UtilityClass
public class Constants {

    public static final String COMMENT_PREFIX             = "#";
    public static final String CONFIG_EXT                 = ".config";
    public static final String FOLDER_ARCH_MODELS         = "architecture_models";
    public static final String FOLDER_RESOURCES_CONFIG    = "resources_config";
    public static final String FILE_RESOURCES_UPSTREAM    = FOLDER_RESOURCES_CONFIG + "/bmc_resources_upstream.config";
    public static final String FILE_RESOURCES_USER        = FOLDER_RESOURCES_CONFIG + "/bmc_resources_user.config";
    public static final String FOLDER_TEMPLATES           = "readme_templates";
    public static final String MVN_PREFIX                 = "-D";
    public static final String PLUGIN_KEY                 = "bmc.dev.resources.code:bmc-resources";
    public static final String PROP_COMPLETED_ARCH        = "bmc.architecture.completed";
    public static final String PROP_COMPLETED_RESOURCE    = "bmc.resources.completed";
    public static final String PROP_CREATED_WITH_VERSION  = "bmc.resources.created.version";
    public static final String STRUCTURE_SEPARATOR        = ",";

}
