package bmc.dev.resources.code.bmcresources.config;

import java.util.Optional;

/**
 * Record representing a single resource entry.
 *
 * @param source     source file or folder to read from
 * @param target     target file or folder to write to
 * @param permission permission in POSIX 1-Letter code. Currently only processing "x", executable.
 */
public record ResourceEntry(
        String source,
        String target,
        Optional<String> permission
) {}
