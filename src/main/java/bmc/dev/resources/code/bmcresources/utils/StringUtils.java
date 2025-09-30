package bmc.dev.resources.code.bmcresources.utils;

import java.util.function.Predicate;

import lombok.experimental.UtilityClass;

/**
 * General Utilities for manipulating Strings.
 */
@UtilityClass
public class StringUtils {

    /**
     * A {@link Predicate} that checks if a given string is either {@code null} or {@link String#isBlank()}.
     * <p>
     * A string is considered blank if it is empty or contains only whitespace characters.
     *
     * @see String#isBlank()
     *
     */
    public static final Predicate<String> isNullOrBlank = stringToEvaluate -> stringToEvaluate == null || stringToEvaluate.isBlank();

}
