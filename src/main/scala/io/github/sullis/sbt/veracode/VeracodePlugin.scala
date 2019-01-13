package io.github.sullis.sbt.veracode

import sbt._

object VeracodePlugin extends AutoPlugin {

  object autoImport {
    val veracodeArtifact = settingKey[String]("artifact on the local filesystem")
    val veracodeAppName = settingKey[String]("Veracode app name")

    val veracodeInitApi = taskKey[VeracodeApi]("veracodeInitApi")
    val veracodeResolveAppId = taskKey[String]("veracodeResolveAppId")
    val veracodeUploadFile = taskKey[Unit]("veracodeUploadFile")
    val veracodeBeginScan = taskKey[Unit]("veracodeBeginScan")
  }

  import autoImport._

  override lazy val projectSettings: Seq[Def.Setting[_]] = Seq(
    veracodeAppName := sbt.Keys.name.value
  ) ++ allTasks

  private val allTasks = Seq(
    veracodeResolveAppId := {
      veracodeInitApi.value.fetchAppId(veracodeAppName.value)
    },
    veracodeInitApi := {
      val api = new VeracodeApiImpl(new VeracodeWrapperFactory(apiCredentials))
      api
    },
    veracodeUploadFile := {
      val file = new File(veracodeArtifact.value)
      if (!file.exists()) {
        throw new RuntimeException("File does not exist: " + veracodeArtifact.value)
      }
      val veracodeApi = veracodeInitApi.value
      veracodeApi.uploadFile(veracodeResolveAppId.value, file)
    },
    veracodeBeginScan := {
      val veracodeApi = veracodeInitApi.value
      veracodeApi.beginScan(veracodeResolveAppId.value)
      System.out.println("veracodeBeginScan")
    },

  )

  private lazy val apiId: String = {
    Option(System.getenv("VERACODE_API_ID")).getOrElse(throw new RuntimeException("missing environment var: VERACODE_API_ID"))
  }

  private lazy val apiKey: String = {
    Option(System.getenv("VERACODE_API_KEY")).getOrElse(throw new RuntimeException("missing environment var: VERACODE_API_KEY"))
  }

  private lazy val apiCredentials = new ApiCredentials(apiId = apiId, apiKey = apiKey)

}

