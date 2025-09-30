package bmc.dev.resources.code.bmcresources.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static bmc.dev.resources.code.bmcresources.Constants.COMMENT_PREFIX;
import static bmc.dev.resources.code.bmcresources.Constants.STRUCTURE_SEPARATOR;
import static bmc.dev.resources.code.bmcresources.utils.BMCConfigFileUtils.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Stream.of;
import static org.junit.jupiter.api.Assertions.*;

class BMCConfigFileUtilsTest {

    @Test
    void calculateLeftAlignedPadding_returnsExpectedOutput() {

        final int    paddingSize = 10;
        final String apply       = calculateLeftAlignedPadding.apply(paddingSize);
        final String expected    = "%-" + paddingSize + "s";

        assertEquals(expected, apply);
    }

    @ParameterizedTest
    @CsvSource(value = {"this/is/an:entry", "another:entry", "another./*thing:entry.4"}, delimiter = ':')
    void createMapEntry_withLeftAndRightHandSideValues_returnsAProperlySplitEntry(final String key, final String value) {

        final Entry<String, String> processedEntry = createMapEntry.apply(key + ":" + value);

        assertEquals(key, processedEntry.getKey());
        assertEquals(value, processedEntry.getValue());

        final Entry<String, String> tweakedEntryToBeTrimmed = createMapEntry.apply("    " + key + "   :    " + value + "    ");

        assertEquals(key, tweakedEntryToBeTrimmed.getKey());
        assertEquals(value, tweakedEntryToBeTrimmed.getValue());
    }

    @Test
    void createMapEntry_withOneOrBothSideValuesMissing_returnsAProperlySplitEntry() {

        final String commonValue                 = "this/is/an";
        final String lineMissingRightHandValue02 = "this/is/an:";
        final String lineMissingLeftHandValue    = ":this/is/an";

        final Entry<String, String> processedEntryRight01 = createMapEntry.apply(commonValue);
        final Entry<String, String> processedEntryRight02 = createMapEntry.apply(lineMissingRightHandValue02);
        final Entry<String, String> processedEntryLeft    = createMapEntry.apply(lineMissingLeftHandValue);

        assertEquals(commonValue, processedEntryRight01.getKey());
        assertEquals("", processedEntryRight01.getValue());

        assertEquals(commonValue, processedEntryRight02.getKey());
        assertEquals("", processedEntryRight02.getValue());

        // This is expected to happen but not expected to be useful at all
        assertEquals("", processedEntryLeft.getKey());
        assertEquals(commonValue, processedEntryLeft.getValue());
    }

    @Test
    void extractConfiguration_withValidData_returnsASortedLinkedHashMap() {

        final String configFile = "/clean_ddd_hexa_for_tests.config";
        final BufferedReader configFileReader =
                new BufferedReader(new InputStreamReader(requireNonNull(this.getClass().getResourceAsStream(configFile)), UTF_8));

        final Map<String, String> configMap = extractConfiguration.apply(configFileReader);

        assertEquals(5, configMap.size());
        assertInstanceOf(LinkedHashMap.class, configMap);

        // Verify the order is sorted and kept
        final var keys = configMap.keySet().stream().toList();
        assertEquals("adapters", keys.get(0));
        assertEquals("adapters/in", keys.get(1));
        assertEquals("adapters/in/rest", keys.get(2));
        assertEquals("adapters/in/rest/dtos", keys.get(3));
        assertEquals("adapters/in/rest/dtos/common", keys.get(4));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   ", "  " + COMMENT_PREFIX + "  ", "  " + STRUCTURE_SEPARATOR + "  ", COMMENT_PREFIX, STRUCTURE_SEPARATOR})
    void isLineProcessable_withInvalidValidInput_returnsFalse(final String input) {

        assertFalse(isLineProcessable.test(input));
    }

    @Test
    void isLineProcessable_withValidInput_returnsTrue() {

        final String commonValue = "this/is/an:/valid_input:";

        assertTrue(isLineProcessable.test(commonValue));
    }

    @Test
    void toLinkedHashMap_withEntries_returnsAHashmapWithAllEntries() {

        final Entry<String, String> entry01 = createMapEntry.apply("this/is/an:entry");
        final Entry<String, String> entry02 = createMapEntry.apply("this/is/another:entry");
        final Entry<String, String> entry03 = createMapEntry.apply("this/is/yet/another:entry");

        final Map<String, String> collectMap = of(entry01, entry02, entry03).collect(toLinkedHashMap);

        assertInstanceOf(LinkedHashMap.class, collectMap);
        assertEquals(3, collectMap.size());
    }

}
