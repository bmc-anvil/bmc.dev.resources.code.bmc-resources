package bmc.dev.resources.code.bmcresources.maven;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import lombok.experimental.UtilityClass;

import static java.lang.Boolean.FALSE;
import static java.lang.System.getProperty;
import static java.util.Optional.ofNullable;
import static java.util.OptionalInt.empty;
import static java.util.stream.IntStream.range;

/**
 * Class for handling Maven configuration files.
 */
@UtilityClass
public class MavenConfigFileUtils {

    /**
     * A {@link BiFunction} that determines the first occurrence of a given property, returning its index on the list.
     * <br>This function is useful when working with Maven configuration files to locate specific properties
     * in a list of configuration parameters.
     * <p>
     * If the property is found in the list, the {@code OptionalInt} will contain the index of the first matching string;
     * otherwise, an empty {@code OptionalInt} is returned.
     * <p>
     * Input:
     * - A list of strings to search within.
     * - A property to search for within the strings of the list.
     * <p>
     * Output:
     * - A non-empty {@code OptionalInt} containing the zero-based index of the first matching string
     * if the property is found, or an empty {@code OptionalInt} otherwise.
     */
    public static final BiFunction<List<String>, String, OptionalInt> findPropertyIndex = (list, property) ->
            ofNullable(list)
                    .map(strings ->
                                 range(0, strings.size())
                                         .filter(i -> strings.get(i).contains(property))
                                         .findFirst())
                    .orElse(empty());

    /**
     * {@link Predicate} to evaluate if a given Maven property is both set and has a value of {@code true}.
     * <p>
     * If the property exists AND its value is {@code true}, the predicate returns {@code true}; otherwise, it returns {@code false}.
     */
    public static final Predicate<String> isPropertySetAndTrue = property -> ofNullable(getProperty(property)).map(Boolean::valueOf).orElse(FALSE);

}
