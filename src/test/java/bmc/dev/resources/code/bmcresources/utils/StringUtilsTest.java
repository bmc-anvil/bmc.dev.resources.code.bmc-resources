package bmc.dev.resources.code.bmcresources.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StringUtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {"test", " test", "_test", "test _", "    a    "})
    void stringHasContent_returnsFalse(final String input) {

        assertFalse(StringUtils.isNullOrBlank.test(input));

    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   "})
    void stringIsNullOrBlank_returnsFalse(final String input) {

        assertTrue(StringUtils.isNullOrBlank.test(input));

    }

}
