package bmc.dev.resources.code.bmcresources.utils;

import java.io.BufferedReader;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;

import static bmc.dev.resources.code.bmcresources.Constants.COMMENT_PREFIX;
import static bmc.dev.resources.code.bmcresources.Constants.STRUCTURE_SEPARATOR;
import static bmc.dev.resources.code.bmcresources.utils.StringUtils.isNullOrBlank;
import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.util.Map.Entry;
import static java.util.Map.Entry.comparingByKey;
import static java.util.Map.entry;
import static java.util.stream.Collectors.toMap;

public class BMCConfigFileUtils {

    public static final Function<Integer, String> calculatePadding = length -> "%-" + length + "s";

    public static final Function<String, Entry<String, String>> createMapEntry = line -> {
        final String[] lineParts = line.split(STRUCTURE_SEPARATOR);
        final String   folder    = lineParts[0].trim();
        final String   readme    = lineParts.length > 1 ? lineParts[1].trim() : "";
        return entry(folder, readme);
    };

    public static final Predicate<String> discardEmptyAndComment = input -> !isNullOrBlank.test(input) && !input.startsWith(COMMENT_PREFIX);

    public static final Function<Collection<String>, Integer> getMaxStringLengthFromCollection = strings -> strings.stream()
                                                                                                                   .map(String::length)
                                                                                                                   .max(Integer::compareTo)
                                                                                                                   .orElse(0);

    public static final Collector<Entry<String, String>, ?, LinkedHashMap<String, String>> toLinkedHashMap = toMap(Entry::getKey, Entry::getValue,
                                                                                                                   (existing, _) -> existing,
                                                                                                                   LinkedHashMap::new);

    public static final Function<BufferedReader, Map<String, String>> extractConfiguration = reader -> reader.lines()
                                                                                                             .filter(discardEmptyAndComment)
                                                                                                             .map(createMapEntry)
                                                                                                             .sorted(comparingByKey(CASE_INSENSITIVE_ORDER))
                                                                                                             .collect(toLinkedHashMap);

    private BMCConfigFileUtils() {}

}
