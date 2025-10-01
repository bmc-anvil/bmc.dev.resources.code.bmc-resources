package bmc.dev.resources.code.bmcresources.utils;

import java.io.BufferedReader;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;

import lombok.experimental.UtilityClass;

import static bmc.dev.resources.code.bmcresources.Constants.COMMENT_PREFIX;
import static bmc.dev.resources.code.bmcresources.Constants.STRUCTURE_SEPARATOR;
import static bmc.dev.resources.code.bmcresources.utils.StringUtils.isNullOrBlank;
import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.util.Map.Entry;
import static java.util.Map.Entry.comparingByKey;
import static java.util.Map.entry;
import static java.util.stream.Collectors.toMap;

/**
 * Utility class providing functionality for processing and manipulating BMC resources configuration files.
 */
@UtilityClass
public class BMCConfigFileUtils {

    /**
     * A {@link Function} that generates a string format specifier for left-aligned padding
     * based on the given width.
     * <p>
     * The input integer represents the desired width of the formatted string, and the resulting
     * format specifier will be a left-aligned string placeholder (e.g., "%-10s" for width 10).
     */
    public static final Function<Integer, String> calculateLeftAlignedPadding = length -> "%-" + length + "s";

    /**
     * A {@link Function} that takes a {@link String} and splits it into an {@link Entry} of type {@link String}, {@link String}.
     * <p>
     * The separator can be used to parse, for example, folder names and their corresponding readme or content.
     * If no content is specified, the value portion of the {@link Entry} will default to an empty string.
     * <p>
     * The function trims any leading or trailing whitespace from both parts of the split input string.
     *
     * @see Entry
     * @see Map#entry(Object, Object)
     */
    public static final Function<String, Entry<String, String>> createMapEntry = line -> {
        final String[] lineParts = line.split(STRUCTURE_SEPARATOR);
        final String   key       = lineParts[0].trim();
        final String   value     = lineParts.length > 1 ? lineParts[1].trim() : "";
        return entry(key, value);
    };

    /**
     * A {@link Function} that computes the maximum string length from a given {@link Collection} of strings.
     */
    public static final Function<Collection<String>, Integer> getMaxStringLengthFromCollection =
            strings -> strings.stream().map(String::length).max(Integer::compareTo).orElse(0);

    /**
     * A {@link Predicate} used to filter out from a collection of strings:
     * <br>- null / blank as evaluated by {@link StringUtils#isNullOrBlank}.
     * <br>- comment lines
     * <br>- non parseable lines (i.e. starting with a separator, etc...).
     * <p>
     * This predicate returns {@code true} for strings that are neither empty/blank nor comments, etc.
     */
    public static final Predicate<String> isLineProcessable =
            input -> !(isNullOrBlank.test(input) || input.trim().startsWith(COMMENT_PREFIX) || input.trim().startsWith(STRUCTURE_SEPARATOR));

    /**
     * A {@link Collector} that accumulates {@link Map.Entry} elements into a {@link LinkedHashMap}.
     * <p>
     * The collector ensures that the entries are collected in the order they are encountered.
     * <p>
     * If duplicate keys are encountered while collecting, the existing key-value pair is retained, and the duplicate is ignored.
     * <p>
     * This collector is particularly useful for preserving the insertion order of key-value pairs while converting a collection
     * of {@link Map.Entry} elements into a {@link LinkedHashMap}.
     */
    public static final Collector<Entry<String, String>, ?, LinkedHashMap<String, String>> toLinkedHashMap =
            toMap(Entry::getKey, Entry::getValue, (existing, _) -> existing, LinkedHashMap::new);

    /**
     * A {@link Function} that processes a {@link BufferedReader} to extract configuration data.
     * <p>
     * The input is expected to be read line by line, with each line potentially representing a key-value pair.
     * <p>
     * The steps in the transformation are as follows:
     * <br> - Lines that are empty or start with a comment marker are filtered out.
     * <br> - Remaining lines are mapped into entries of a {@link Map} using {@code createMapEntry}, which splits each line into key-value pairs.
     * <br> - The resulting map entries are sorted by their keys in a case-insensitive order.
     * <br> - The sorted entries are collected into a {@link LinkedHashMap} to maintain the ordering.
     * <p>
     * The final result is a sorted, ordered map of configuration key-value pairs.
     */
    public static final Function<BufferedReader, Map<String, String>> extractConfiguration = reader -> reader.lines()
                                                                                                             .filter(isLineProcessable)
                                                                                                             .map(createMapEntry)
                                                                                                             .sorted(comparingByKey(CASE_INSENSITIVE_ORDER))
                                                                                                             .collect(toLinkedHashMap);

}
