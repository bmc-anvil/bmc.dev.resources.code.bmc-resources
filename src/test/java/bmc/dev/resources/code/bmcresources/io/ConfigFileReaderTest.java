package bmc.dev.resources.code.bmcresources.io;

import java.util.List;

import org.junit.jupiter.api.Test;

import bmc.dev.resources.code.bmcresources.config.ResourceEntry;

import static bmc.dev.resources.code.bmcresources.io.ConfigFileReader.readResourcesConfigFile;
import static org.junit.jupiter.api.Assertions.*;

class ConfigFileReaderTest {

    @Test
    void readResourcesConfigFile_withEmptyResourcesConfigFile_shouldReturnCorrectList() {

        final String configFile = "clean_ddd_hexa_empty_for_tests.config";

        final List<ResourceEntry> configEntries = readResourcesConfigFile(configFile);

        assertTrue(configEntries.isEmpty());
    }

    @Test
    void readResourcesConfigFile_withExistingResourcesConfigFile_withNonValidData_shouldReturnCorrectList() {

        final String configFile = "clean_ddd_hexa_non_processable_for_tests.config";

        final List<ResourceEntry> configEntries = readResourcesConfigFile(configFile);

        assertTrue(configEntries.isEmpty());

    }

    @Test
    void readResourcesConfigFile_withExistingResourcesConfigFile_withValidData_shouldReturnCorrectList() {

        final String configFile = "clean_ddd_hexa_for_tests.config";

        final List<ResourceEntry> configEntries = readResourcesConfigFile(configFile);

        assertFalse(configEntries.isEmpty());
        assertEquals(5, configEntries.size());
    }

    @Test
    void readResourcesConfigFile_withNonExistingResourcesConfigFile_shouldThrowException() {

        final String configFile = "i_do_not_exist.config";

        assertThrows(IllegalArgumentException.class, () -> readResourcesConfigFile(configFile));
    }

}
