package io.github.sullis.sbt.veracode

import sbt._

object VeracodePlugin extends AutoPlugin {
  object autoImport {
    val veracodeApiId = settingKey[String]("Veracode API id")
    val veracodeApiKey = settingKey[String]("Veracode API key")
    val veracodeArtifact = settingKey[String]("artifact on the local filesystem")
    val veracodeAppName = settingKey[String]("application name")

    val veracodeInitApi = taskKey[VeracodeApi]("veracodeInitApi")
    val veracodeCreateBuild = taskKey[Unit]("veracodeCreateBuild")
    val veracodeUploadFile = taskKey[Unit]("veracodeUploadFile")
    val veracodeUploadFileSandbox = taskKey[Unit]("veracodeUploadFileSandbox")
  }

  import autoImport._

  override lazy val projectSettings: Seq[Def.Setting[_]] = Seq(
    veracodeAppName := sbt.Keys.name.value,
  ) ++ allTasks

  private val allTasks = Seq(
    veracodeInitApi := {
      val apiCredentials = new ApiCredentials(veracodeApiId.value, veracodeApiKey.value)
      val api = new VeracodeApiImpl(new VeracodeWrapperFactory(apiCredentials), "appId1234", "sandboxId1234")
      System.out.println("veracodeInitApi: " + api)
      api
    },
    veracodeCreateBuild := {
      val veracodeApi = veracodeInitApi.value
      System.out.println("veracodeCreateBuild")
    },

    veracodeUploadFile := {
      val veracodeApi = veracodeInitApi.value
      System.out.println("veracodeUploadFile")
    },

    veracodeUploadFileSandbox := {
      val veracodeApi = veracodeInitApi.value
      System.out.println("veracodeUploadFileSandbox")
    }
  )

}
