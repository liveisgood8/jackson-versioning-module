package com.nexus.jacksonversioning;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class VersioningPropertyMetaCollection {

    private static final VersioningPropertyMetaCollection EMPTY = new VersioningPropertyMetaCollection();

    private final Set<DefaultVersioningPropertyMeta> versioningPropertyMetaSet;

    public static VersioningPropertyMetaCollection of(Set<DefaultVersioningPropertyMeta> versioningPropertyMetaSet) {
        return new VersioningPropertyMetaCollection(versioningPropertyMetaSet);
    }

    public static VersioningPropertyMetaCollection empty() {
        return EMPTY;
    }

    private VersioningPropertyMetaCollection() {
        this.versioningPropertyMetaSet = null;
    }

    private VersioningPropertyMetaCollection(Set<DefaultVersioningPropertyMeta> versioningPropertyMetaSet) {
        this.versioningPropertyMetaSet = versioningPropertyMetaSet;
    }

    public Optional<VersionPropertyMeta> getForVersion(Version version) {
        if (versioningPropertyMetaSet == null) {
            // Available for any version
            return Optional.of(DefaultVersioningPropertyMeta.empty());
        }

        var propertyMetaSet = versioningPropertyMetaSet.stream()
                .filter(propertyMeta -> propertyMeta.isWithin(version))
                .collect(Collectors.toSet());

        if (propertyMetaSet.size() > 1) {
            throw new IllegalArgumentException("Multiple json version definitions found for version: " + version);
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
