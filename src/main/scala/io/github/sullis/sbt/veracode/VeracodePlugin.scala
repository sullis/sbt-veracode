package io.github.sullis.sbt.veracode

import sbt._

object VeracodePlugin extends AutoPlugin {
  val veracodeApiKey = settingKey[String]("Veracode API key")
  val veracodeApiId = settingKey[String]("Veracode API id")
  val veracodeArtifact = settingKey[String]("artifact on the local filesystem")
  val veracodeAppName = settingKey[String]("artifact on the local filesystem")

  override lazy val projectSettings = Seq(
    veracodeAppName := sbt.Keys.name.value
  )
}
