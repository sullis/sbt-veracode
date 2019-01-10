package io.github.sullis.sbt.veracode

import sbt._

object VeracodePlugin extends AutoPlugin {
  val veracodeApiId = settingKey[String]("Veracode API id")
  val veracodeApiKey = settingKey[String]("Veracode API key")
  val veracodeArtifact = settingKey[String]("artifact on the local filesystem")
  val veracodeAppName = settingKey[String]("application name")

  val createBuild = taskKey[Unit]("createBuild")
  val uploadFile = taskKey[Unit]("uploadFile")
  val uploadFileSandbox = taskKey[Unit]("uploadFileSandbox")

  override lazy val projectSettings = Seq(
    veracodeAppName := sbt.Keys.name.value
  )

  createBuild := {
    System.out.println("createBuild")
  }

  uploadFile := {
    System.out.println("uploadFile")
  }

  uploadFileSandbox := {
    System.out.println("uploadFileSandbox")
  }
}
