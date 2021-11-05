package io.github.liveisgood8.jacksonversioning;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VersioningPropertyMetaCollection {

    private static final VersioningPropertyMetaCollection EMPTY = new VersioningPropertyMetaCollection();

    private final List<DefaultVersioningPropertyMeta> versioningPropertyMetaSet;

    private VersioningPropertyMetaCollection() {
        this.versioningPropertyMetaSet = null;
    }

    private VersioningPropertyMetaCollection(List<DefaultVersioningPropertyMeta> versioningPropertyMetaSet) {
        this.versioningPropertyMetaSet = versioningPropertyMetaSet;
    }

    public static VersioningPropertyMetaCollection of(List<DefaultVersioningPropertyMeta> versioningPropertyMetaSet) {
        return new VersioningPropertyMetaCollection(versioningPropertyMetaSet);
    }

    public static VersioningPropertyMetaCollection empty() {
        return EMPTY;
    }

    public Optional<VersionPropertyMeta> getForVersion(Version version) {
        if (versioningPropertyMetaSet == null) {
            // Available for any version
            return Optional.of(DefaultVersioningPropertyMeta.empty());
        }

        var propertyMetaSet = versioningPropertyMetaSet.stream()
                .filter(propertyMeta -> propertyMeta.isWithin(version))
                .collect(Collectors.toList());

        if (propertyMetaSet.size() > 1) {
            throw new IllegalArgumentException(
                    "For version: " + version + " founded multiple definitions: " + propertyMetaSet.stream()
                            .map(DefaultVersioningPropertyMeta::toString)
                            .collect(Collectors.joining(","))
            );
        }

        return propertyMetaSet.isEmpty()
                ? Optional.empty()
                : Optional.of(propertyMetaSet.iterator().next());
    }

    public boolean isWithin(Version version) {
        if (versioningPropertyMetaSet == null) {
            // Available for any version
            return true;
        }

        return versioningPropertyMetaSet.stream()
                .anyMatch(propertyMeta -> propertyMeta.isWithin(version));
    }
}
