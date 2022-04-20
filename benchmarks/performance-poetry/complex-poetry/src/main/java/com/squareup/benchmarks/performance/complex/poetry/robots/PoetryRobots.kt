package com.squareup.benchmarks.performance.complex.poetry.robots

import androidx.test.uiautomator.By
import androidx.test.uiautomator.BySelector
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until

val PoetryPackageSelector: BySelector =
  By.pkg("com.squareup.benchmarks.performance.complex.poetry").depth(0)
val LoadingDialogSelector: BySelector =
  By.res("com.squareup.benchmarks.performance.complex.poetry", "loading_dialog")
val RavenPoemSelector: BySelector = By.textContains("The Raven")
val NextSelector: BySelector = By.textStartsWith("next")
val PreviousSelector: BySelector = By.textEndsWith("previous")

fun UiDevice.waitForPoetry(timeout: Long = 3_000) {
  wait(Until.hasObject(PoetryPackageSelector), timeout)
}

fun UiDevice.waitForLoading(timeout: Long = 3_000) {
  waitFor(LoadingDialogSelector, timeout)
}

fun UiDevice.next(timeout: Long = 3_000) {
  waitForAndClick(NextSelector, timeout)
  waitForLoading(timeout)
}

fun UiDevice.previous(timeout: Long = 3_000) {
  waitForAndClick(PreviousSelector, timeout)
  waitForLoading(timeout)
}

fun UiDevice.openRavenAndNavigate() {
  waitForAndClick(RavenPoemSelector)
  waitForLoading()

  waitForAndClick(By.textStartsWith("Deep into that darkness peering"))
  waitForLoading()

  repeat(5) {
    next()
  }

  repeat(5) {
    previous()
  }

  this.pressBack()

  waitForLoading()

  waitFor(RavenPoemSelector)
}