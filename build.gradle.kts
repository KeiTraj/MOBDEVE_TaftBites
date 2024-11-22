// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false // Use alias for android.application
    id("com.google.gms.google-services") version "4.4.2" apply false // Add Google Services plugin
    id("com.android.library") version "8.1.1" apply false // For library modules (if applicable)

}
