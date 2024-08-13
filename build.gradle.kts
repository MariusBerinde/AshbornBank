// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
     // per rooms
    id("androidx.room")  version "2.6.1" apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
    // per datastore
  //  id("androidx.datastore")  version "2.6.1" apply false
}