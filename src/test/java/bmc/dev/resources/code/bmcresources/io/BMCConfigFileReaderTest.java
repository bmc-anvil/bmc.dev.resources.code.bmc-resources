package bmc.dev.resources.code.bmcresources.io;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import bmc.dev.resources.code.bmcresources.config.ResourceEntry;

import static bmc.dev.resources.code.bmcresources.io.BMCConfigFileReader.extractConfigFileEntries;
import static bmc.dev.resources.code.bmcresources.utils.BMCConfigFileUtils.extractResources;
import static org.junit.jupiter.api.Assertions.*;

class BMCConfigFileReaderTest {

    @Test
    void extractConfigFileEntries_withEmptyResourcesConfigFile_shouldReturnCorrectList() {

        final String configFile = "clean_ddd_hexa_empty_for_tests.config";

        assertTrue(extractConfigFileEntries(configFile, extractResources).isEmpty());
    }

    @Test
    void extractConfigFileEntries_withExistingResourcesConfigFile_withNonValidData_shouldReturnCorrectList() {

        final String configFile = "clean_ddd_hexa_non_processable_for_tests.config";

        assertTrue(extractConfigFileEntries(configFile, extractResources).isEmpty());
    }

    @Test
    void extractConfigFileEntries_withExistingResourcesConfigFile_withValidData_shouldReturnCorrectList() {

        final String configFile = "clean_ddd_hexa_for_tests.config";

        final Optional<List<ResourceEntry>> configEntries = extractConfigFileEntries(configFile, extractResources);

        assertFalse(configEntries.isEmpty());
        assertEquals(5, configEntries.get().size());
    }

    @Test
    void extractConfigFileEntries_withNonExistingResourcesConfigFile_shouldThrowException() {

        final String configFile = "i_do_not_exist.config";

        assertThrows(IllegalArgumentException.class, () -> extractConfigFileEntries(configFile, extractResources));
    }

    @Test
    void extractConfigFileEntries_withNullExtractor_shouldThrowException() {

        final String configFile = "clean_ddd_hexa_for_tests.config";
        assertThrows(IllegalArgumentException.class, () -> extractConfigFileEntries(configFile, null));
    }

}
