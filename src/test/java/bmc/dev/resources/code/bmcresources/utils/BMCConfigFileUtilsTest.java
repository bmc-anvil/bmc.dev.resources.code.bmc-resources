package bmc.dev.resources.code.bmcresources.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import bmc.dev.resources.code.bmcresources.config.ArchitectureEntry;
import bmc.dev.resources.code.bmcresources.config.ResourceEntry;

import static bmc.dev.resources.code.bmcresources.Constants.*;
import static bmc.dev.resources.code.bmcresources.utils.BMCConfigFileUtils.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
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
    @CsvSource(value = {"this/is/an,entry.md", "another,entry.md", "another./*thing,entry.md"}, delimiter = ',')
    void createArchitectureEntry_withFullValues_returnsProperEntry(final String folder, final String readme) {

        final String baseLine = folder + "," + readme;

        final ArchitectureEntry processedEntry = createArchitectureEntry.apply(baseLine);

        assertEquals(folder, processedEntry.folder());
        //noinspection OptionalGetWithoutIsPresent
        assertEquals(readme, processedEntry.readme().get());

        final String tweakedBaseLine = "    " + folder + "   ,    " + readme;

        final ArchitectureEntry tweakedEntryToBeTrimmed = createArchitectureEntry.apply(tweakedBaseLine);

        assertEquals(folder, tweakedEntryToBeTrimmed.folder());
        //noinspection OptionalGetWithoutIsPresent
        assertEquals(readme, tweakedEntryToBeTrimmed.readme().get());
    }

    @Test
    void createArchitectureEntry_withMissingValues_returnsProperEntry() {

        final String commonValue               = "this/is/an";
        final String lineMissingRightHandValue = "this/is/an,";
        final String lineMissingLeftHandValue  = ",readme.md";

        final ArchitectureEntry processedEntry01 = createArchitectureEntry.apply(commonValue);
        final ArchitectureEntry processedEntry02 = createArchitectureEntry.apply(lineMissingRightHandValue);
        final ArchitectureEntry processedEntry03 = createArchitectureEntry.apply(lineMissingLeftHandValue);

        assertEquals(commonValue, processedEntry01.folder());
        assertEquals(empty(), processedEntry01.readme());

        assertEquals(commonValue, processedEntry02.folder());
        assertEquals(empty(), processedEntry02.readme());

        // This can happen, and it will be caught as an error but not here as I'll limit the splitter "just to split".
        assertEquals("", processedEntry03.folder());
        //noinspection OptionalGetWithoutIsPresent
        assertEquals("readme.md", processedEntry03.readme().get());

    }

    @ParameterizedTest
    @CsvSource(value = {"this/is/an,entry,x", "another,entry,", "another./*thing,entry.4,"}, delimiter = ',')
    void createResourceEntry_withFullValues_returnsProperEntry(final String source, final String target, final String permission) {

        final String baseLine = source + "," + target;
        final String fullLine = permission != null ? baseLine + STRUCTURE_SEPARATOR + permission : baseLine;

        final ResourceEntry processedEntry = createResourceEntry.apply(fullLine);

        assertEquals(source, processedEntry.source());
        assertEquals(target, processedEntry.target());
        //noinspection OptionalGetWithoutIsPresent
        ofNullable(permission).ifPresentOrElse(
                perm -> assertEquals(perm, processedEntry.permission().get()),
                () -> assertTrue(processedEntry.permission().isEmpty()));

        final String tweakedBaseLine = "    " + source + "   ,    " + target + "    ";
        final String tweakedFullLine = permission != null ? tweakedBaseLine + STRUCTURE_SEPARATOR + permission : tweakedBaseLine;

        final ResourceEntry tweakedEntryToBeTrimmed = createResourceEntry.apply(tweakedFullLine);

        assertEquals(source, tweakedEntryToBeTrimmed.source());
        assertEquals(target, tweakedEntryToBeTrimmed.target());
        //noinspection OptionalGetWithoutIsPresent
        ofNullable(permission).ifPresentOrElse(
                perm -> assertEquals(perm, processedEntry.permission().get()),
                () -> assertTrue(processedEntry.permission().isEmpty()));
    }

    @Test
    void createResourceEntry_withMissingValues_returnsProperEntry() {

        final String commonValue               = "this/is/an";
        final String lineMissingRightHandValue = "this/is/an,";
        final String lineMissingLeftHandValue  = ",this/is/an";

        final ResourceEntry processedEntryRight01 = createResourceEntry.apply(commonValue);
        final ResourceEntry processedEntryRight02 = createResourceEntry.apply(lineMissingRightHandValue);
        final ResourceEntry processedEntryLeft    = createResourceEntry.apply(lineMissingLeftHandValue);

        assertEquals(commonValue, processedEntryRight01.source());
        assertEquals("", processedEntryRight01.target());
        assertEquals(empty(), processedEntryRight01.permission());

        assertEquals(commonValue, processedEntryRight02.source());
        assertEquals("", processedEntryRight02.target());
        assertEquals(empty(), processedEntryRight01.permission());

        // This is expected to happen but not expected to be useful at all
        assertEquals("", processedEntryLeft.source());
        assertEquals(commonValue, processedEntryLeft.target());
        assertEquals(empty(), processedEntryRight01.permission());
    }

    @Test
    void extractArchitectureModel_withValidData_returnsASortedList() {

        final String                  configFile       = "clean_ddd_hexa_for_tests.config";
        final BufferedReader          configFileReader =
                new BufferedReader(new InputStreamReader(requireNonNull(this.getClass().getResourceAsStream("/" + configFile)), UTF_8));
        final List<ArchitectureEntry> configEntries    = extractArchitectureModel.apply(configFileReader);

        assertEquals(5, configEntries.size());
        assertInstanceOf(List.class, configEntries);

        // Verify the order is sorted and kept
        final var sources = configEntries.stream().map(ArchitectureEntry::folder).toList();
        assertEquals("adapters", sources.get(0));
        assertEquals("adapters/in", sources.get(1));
        assertEquals("adapters/in/rest", sources.get(2));
        assertEquals("adapters/in/rest/dtos", sources.get(3));
        assertEquals("adapters/in/rest/dtos/common", sources.get(4));
    }

    @Test
    void extractResources_withValidData_returnsASortedList() {

        final String              configFile       = "bmc_resources_upstream_for_tests.config";
        final BufferedReader      configFileReader =
                new BufferedReader(new InputStreamReader(requireNonNull(this.getClass().getResourceAsStream("/" + configFile)), UTF_8));
        final List<ResourceEntry> configEntries    = extractResources.apply(configFileReader);

        assertEquals(3, configEntries.size());
        assertInstanceOf(List.class, configEntries);

        // Verify the order is sorted and kept
        final var sources = configEntries.stream().map(ResourceEntry::source).toList();
        assertEquals(".editorconfig", sources.get(0));
        assertEquals(".mvn/wrapper/", sources.get(1));
        assertEquals("mvnw", sources.get(2));
    }

    @Test
    void getMaxStringLengthFromCollection_withData_returnsLengthOfLongestString() {

        final String       longestString = "thisWillBeTheLongestString";
        final List<String> stringList    = List.of("this", "is", "a", "list", "of", "strings", longestString);
        final Set<String>  stringSet     = Set.of("this", "is", "a", "list", "of", "strings", longestString);

        assertEquals(longestString.length(), getMaxStringLength.apply(stringList));
        assertEquals(longestString.length(), getMaxStringLength.apply(stringSet));
    }

    @Test
    void getMaxStringLengthFromCollection_withNoData_returnsZero() {

        final List<String> stringList = new ArrayList<>();
        final Set<String>  stringSet  = new HashSet<>();

        assertEquals(0, getMaxStringLength.apply(stringList));
        assertEquals(0, getMaxStringLength.apply(stringSet));

    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   ", "  " + COMMENT_PREFIX + "  ", "  " + STRUCTURE_SEPARATOR + "  ", COMMENT_PREFIX, STRUCTURE_SEPARATOR})
    void isLineProcessable_withInvalidValidInput_returnsFalse(final String input) {

        assertFalse(isLineProcessable.test(input));
    }

    @Test
    void isLineProcessable_withValidInput_returnsTrue() {

        final String commonValue = "this/is/an,/valid_input,";

        assertTrue(isLineProcessable.test(commonValue));
    }

}
