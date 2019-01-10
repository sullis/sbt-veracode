package io.github.sullis.sbt.veracode

import sbt._

object VeracodePlugin extends AutoPlugin {
  val veracodeApiId = settingKey[String]("Veracode API id")
  val veracodeApiKey = settingKey[String]("Veracode API key")
  val veracodeArtifact = settingKey[String]("artifact on the local filesystem")
  val veracodeAppName = settingKey[String]("application name")

  val veracodeCreateBuild = taskKey[Unit]("createBuild")
  val veracodeUploadFile = taskKey[Unit]("uploadFile")
  val veracodeUploadFileSandbox = taskKey[Unit]("uploadFileSandbox")

  override lazy val projectSettings: Seq[Setting[_]] = Seq(
    veracodeAppName := sbt.Keys.name.value,
  ) ++ allTasks

  private val allTasks = Seq(
    veracodeCreateBuild := {
      System.out.println("veracodeCreateBuild")
    },

    veracodeUploadFile := {
      System.out.println("veracodeUploadFile")
    },

    veracodeUploadFileSandbox := {
      System.out.println("veracodeUploadFileSandbox")
    }
  )
}
