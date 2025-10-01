package bmc.dev.resources.code.bmcresources.io;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigFileReaderTest {

    @Test
    void readConfigFile_withEmptyConfigFile_shouldReturnCorrectMap() {

        final String configFile = "clean_ddd_hexa_empty_for_tests.config";

        final Optional<Map.Entry<Integer, Map<String, String>>> structureMap = ConfigFileReader.readConfigFile(configFile);

        assertTrue(structureMap.isEmpty());
    }

    @Test
    void readConfigFile_withExistingConfigFile_withNonValidData_shouldReturnCorrectMap() {

        final String configFile = "clean_ddd_hexa_non_processable_for_tests.config";

        final Optional<Map.Entry<Integer, Map<String, String>>> structureMap = ConfigFileReader.readConfigFile(configFile);

        assertTrue(structureMap.isEmpty());

    }

    @Test
    void readConfigFile_withExistingConfigFile_withValidData_shouldReturnCorrectMap() {

        final String configFile          = "clean_ddd_hexa_for_tests.config";
        final String biggestStringInFile = "adapters/in/rest/dtos/common";

        final Optional<Map.Entry<Integer, Map<String, String>>> structureMap = ConfigFileReader.readConfigFile(configFile);

        assertTrue(structureMap.isPresent());
        assertEquals(5, structureMap.get().getValue().size());
        assertEquals(biggestStringInFile.length(), structureMap.get().getKey());
    }

    @Test
    void readConfigFile_withNonExistingConfigFile_shouldThrowException() {

        final String configFile = "i_do_not_exist.config";

        assertThrows(IllegalArgumentException.class, () -> ConfigFileReader.readConfigFile(configFile));
    }

}
