package io.github.sullis.sbt.veracode

import sbt._

object VeracodePlugin extends AutoPlugin {
  private val defaultBusinessCriticality = "VL4"

  object autoImport {
    val veracodeArtifact = settingKey[String]("artifact on the local filesystem")
    val veracodeAppName = settingKey[String]("Veracode app name")
    val veracodeAppId = settingKey[String]("Veracode app id")

    val veracodeInitApi = taskKey[VeracodeApi]("veracodeInitApi")
    val veracodeResolveAppId = taskKey[String]("veracodeResolveAppId")
    val veracodeCreateBuild = taskKey[Unit]("veracodeCreateBuild")
    val veracodeUploadFile = taskKey[Unit]("veracodeUploadFile")
    val veracodeUploadFileSandbox = taskKey[Unit]("veracodeUploadFileSandbox")
  }

  import autoImport._

  override lazy val projectSettings: Seq[Def.Setting[_]] = Seq(
    veracodeAppName := sbt.Keys.name.value
  ) ++ allTasks

  private val allTasks = Seq(
    veracodeResolveAppId := {
      val wrapper = new VeracodeWrapperFactory(apiCredentials)
      wrapper.fetchAppId(veracodeAppName.value)
    },
    veracodeInitApi := {
      val api = new VeracodeApiImpl(new VeracodeWrapperFactory(apiCredentials), appId = veracodeResolveAppId.value)
      System.out.println("veracodeInitApi: " + api)
      api
    },
    veracodeCreateBuild := {
      val api = veracodeInitApi.value
      System.out.println("veracodeCreateBuild")
      val result = api.createBuild("build" + System.currentTimeMillis)
      System.out.println(result)
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

  private lazy val apiId: String = {
    Option(System.getenv("VERACODE_API_ID")).getOrElse(throw new RuntimeException("missing environment var: VERACODE_API_ID"))
  }

  private lazy val apiKey: String = {
    Option(System.getenv("VERACODE_API_KEY")).getOrElse(throw new RuntimeException("missing environment var: VERACODE_API_KEY"))
  }

  private lazy val apiCredentials = new ApiCredentials(apiId = apiId, apiKey = apiKey)

}

