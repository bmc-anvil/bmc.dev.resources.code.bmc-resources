package bmc.dev.resources.code.bmcresources.io;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import bmc.dev.resources.code.bmcresources.config.ResourceEntry;

import static bmc.dev.resources.code.bmcresources.io.ConfigFileReader.extractConfigFileEntries;
import static bmc.dev.resources.code.bmcresources.utils.BMCConfigFileUtils.extractResources;
import static org.junit.jupiter.api.Assertions.*;

class ConfigFileReaderTest {

    @Test
    void readConfigFileAndExtractContent_withEmptyResourcesConfigFile_shouldReturnCorrectList() {

        final String configFile = "clean_ddd_hexa_empty_for_tests.config";

        assertTrue(extractConfigFileEntries(configFile, extractResources).isEmpty());
    }

    @Test
    void readConfigFileAndExtractContent_withExistingResourcesConfigFile_withNonValidData_shouldReturnCorrectList() {

        final String configFile = "clean_ddd_hexa_non_processable_for_tests.config";

        assertTrue(extractConfigFileEntries(configFile, extractResources).isEmpty());
    }

    @Test
    void readConfigFileAndExtractContent_withExistingResourcesConfigFile_withValidData_shouldReturnCorrectList() {

        final String configFile = "clean_ddd_hexa_for_tests.config";

        final Optional<List<ResourceEntry>> configEntries = extractConfigFileEntries(configFile, extractResources);

        assertFalse(configEntries.isEmpty());
        assertEquals(5, configEntries.get().size());
    }

    @Test
    void readConfigFileAndExtractContent_withNonExistingResourcesConfigFile_shouldThrowException() {

        final String configFile = "i_do_not_exist.config";

        assertThrows(IllegalArgumentException.class, () -> extractConfigFileEntries(configFile, extractResources));
    }

}
