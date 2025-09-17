package bmc.dev.resources.code.bmcresources.maven;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.BiFunction;

import static java.util.stream.IntStream.range;

public class MavenConfigFileUtils {

    public static BiFunction<List<String>, String, OptionalInt> findPropertyIndex = (list, property) -> range(0, list.size())
            .filter(i -> list.get(i).contains(property))
            .findFirst();

}
