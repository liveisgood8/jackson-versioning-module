package io.github.liveisgood8.jacksonversioning;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Describe version in semver format.
 */
public class Version implements Comparable<Version> {

    private static final Version EMPTY = new Version(-1, -1, -1);

    private final int major;

    private final int minor;

    private final int patch;

    private Version(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public static Version empty() {
        return EMPTY;
    }

    public static Version of(int major, int minor) {
        validateThatPositive(major);
        validateThatPositive(minor);

        return new Version(major, minor, -1);
    }

    public static Version of(int major, int minor, int patch) {
        validateThatPositive(major);
        validateThatPositive(minor);
        validateThatPositive(patch);

        return new Version(major, minor, patch);
    }

    private static void validateThatPositive(int part) {
        if (part < 0) {
            throw new IllegalArgumentException("Version part should be positive number");
        }
    }

    public static Version fromString(String version) {
        if (version == null || version.isBlank()) {
            throw new IllegalArgumentException("Version could not be null or empty");
        }

        if (!version.startsWith("v")) {
            throw new IllegalArgumentException("Version should start from 'v' symbol");
        }

        List<String> versionParts = Arrays.stream(version.substring(1).split("\\."))
                .filter(versionPart -> !versionPart.isBlank())
                .map(String::trim)
                .collect(Collectors.toList());

        if (versionParts.size() < 2) {
            throw new IllegalArgumentException("Version string must be in semver format (X.Y.Z)");
        }

        List<Integer> versionPartsInteger = versionParts.stream()
                .map(
                        versionPartString -> {
                            int versionPartInteger = Integer.parseInt(versionPartString);
                            if (versionPartInteger < 0) {
                                throw new IllegalArgumentException("Version part should be positive number: " + versionPartString);
                            }
                            return versionPartInteger;
                        }
                )
                .collect(Collectors.toList());

        int major = versionPartsInteger.get(0);
        int minor = versionPartsInteger.get(1);

        if (versionPartsInteger.size() > 2) {
            int patch = versionPartsInteger.get(2);
            return Version.of(major, minor, patch);
        } else {
            return Version.of(major, minor);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        Version otherVersion = (Version) other;
        return equals(otherVersion);
    }

    public boolean greaterThan(Version other) {
        return compareTo(other) > 0;
    }

    public boolean lessThan(Version other) {
        return compareTo(other) < 0;
    }

    public boolean equals(Version other) {
        return compareTo(other) == 0;
    }

    @Override
    public int compareTo(Version other) {
        int result = major - other.major;
        if (result == 0) {
            result = minor - other.minor;
            if (result == 0) {
                result = patch - other.patch;
            }
        }
        return result;
    }

    @JsonValue
    public String toString() {
        if (this == EMPTY) {
            return "<empty>";
        }

        if (patch >= 0) {
            return String.format("v%d.%d.%d", major, minor, patch);
        } else {
            return String.format("v%d.%d", major, minor);
        }
    }
}
