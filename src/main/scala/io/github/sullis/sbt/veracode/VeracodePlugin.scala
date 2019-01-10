package io.github.sullis.sbt.veracode

import sbt._

object VeracodePlugin extends AutoPlugin {
  val veracodeApiId = settingKey[String]("Veracode API id")
  val veracodeApiKey = settingKey[String]("Veracode API key")
  val veracodeArtifact = settingKey[String]("artifact on the local filesystem")
  val veracodeAppName = settingKey[String]("application name")

  override lazy val projectSettings = Seq(
    veracodeAppName := sbt.Keys.name.value
  )
}
