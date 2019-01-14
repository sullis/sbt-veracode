package io.github.sullis.sbt.veracode

import sbt._

object VeracodePlugin extends AutoPlugin {

  object autoImport {
    val veracodeArtifact = settingKey[String]("artifact on the local filesystem")
    val veracodeAppName = settingKey[String]("Veracode app name")

    val veracodeResolveAppId = taskKey[String]("veracodeResolveAppId")
    val veracodeUploadFile = taskKey[Unit]("veracodeUploadFile")
    val veracodeBeginScan = taskKey[Unit]("veracodeBeginScan")
  }

  import autoImport._

  override lazy val projectSettings: Seq[Def.Setting[_]] = Seq(
    veracodeAppName := sbt.Keys.name.value
  ) ++ allTasks

  private val api = new VeracodeApiImpl(new VeracodeWrapperFactory(apiCredentials))

  private val allTasks = Seq(
    veracodeResolveAppId := {
      System.out.println("veracodeResolveAppId")
      val appId = api.fetchAppId(veracodeAppName.value)
      System.out.println("Veracode appName: " + veracodeAppName.value)
      System.out.println("Veracode appId: " + appId)
      appId
    },
    veracodeUploadFile := {
      System.out.println("veracodeUploadFile")
      val file = new File(veracodeArtifact.value)
      if (!file.exists()) {
        throw new RuntimeException("File does not exist: " + veracodeArtifact.value)
      }
      api.uploadFile(veracodeResolveAppId.value, file)
    },
    veracodeBeginScan := {
      System.out.println("veracodeBeginScan")
      api.beginScan(veracodeResolveAppId.value)
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

