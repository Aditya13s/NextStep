# ProGuard / R8 rules for the :app module
# ---------------------------------------------------------
# R8 (the shrinker/obfuscator used in release builds) reads
# this file AFTER applying the default Android rules from
# proguard-android-optimize.txt.
#
# Add rules here ONLY when R8 incorrectly removes or renames
# a class that is accessed via reflection (e.g. Room entities,
# Gson models, or classes referenced only in XML/manifests).
#
# Hilt, Room, and Navigation all supply their own consumer
# ProGuard rules via their AAR manifests, so you typically
# don't need extra rules for those libraries.
# ---------------------------------------------------------

# Keep Room entity class names (Room uses reflection to map
# column names to field names at runtime)
-keep class com.nextstep.core.data.local.entity.** { *; }

# Keep Hilt-generated component classes
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
