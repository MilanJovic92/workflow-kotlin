plugins {
  `kotlin-jvm`
  id("com.google.devtools.ksp")
  id("com.vanniktech.maven.publish")
}

apply(from = rootProject.file(".buildscript/configure-maven-publish.gradle"))

dependencies {
  compileOnly(libs.jetbrains.annotations)
  compileOnly(libs.squareup.moshi.codegen)

  ksp(libs.squareup.moshi.codegen)

  api(libs.kotlin.jdk8)
  api(libs.kotlinx.coroutines.core)

  implementation(libs.squareup.moshi.adapters)
  implementation(libs.squareup.moshi)

  testImplementation(libs.kotlin.test.jdk)
}
