package io.github.sullis.sbt.veracode

import sbt._
import sbt.Keys._

object VeracodePlugin extends AutoPlugin {
  val apiKey = settingKey[String]("Veracode API key")
  val apiId = settingKey[String]("Veracode API id")
  val artifact = settingKey[String]("artifact on the local filesystem")
}
