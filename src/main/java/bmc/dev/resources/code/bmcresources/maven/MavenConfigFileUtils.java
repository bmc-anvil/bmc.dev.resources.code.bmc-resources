package bmc.dev.resources.code.bmcresources.maven;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.BiFunction;

import lombok.experimental.UtilityClass;

import static java.util.Optional.ofNullable;
import static java.util.OptionalInt.empty;
import static java.util.stream.IntStream.range;

@UtilityClass
public class MavenConfigFileUtils {

    public static BiFunction<List<String>, String, OptionalInt> findPropertyIndex = (list, property) ->
            ofNullable(list)
                    .map(strings ->
                                 range(0, strings.size())
                                         .filter(i -> strings.get(i).contains(property))
                                         .findFirst())
                    .orElse(empty());

}
