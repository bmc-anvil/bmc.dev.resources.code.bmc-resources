package bmc.dev.resources.code.bmcresources.config;

import java.util.Optional;

/**
 * Record representing a single architecture model entry.
 *
 * @param folder folder to create
 * @param readme {@link Optional} accompanying readme file to the folder to create.
 */
public record ArchitectureEntry(
        String folder,
        Optional<String> readme
) {}
