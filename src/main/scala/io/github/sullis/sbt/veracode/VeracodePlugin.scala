package io.github.sullis.sbt.veracode

import sbt._

object VeracodePlugin extends AutoPlugin {
  object autoImport {
    val veracodeApiId = settingKey[Option[String]]("Veracode API id")
    val veracodeApiKey = settingKey[Option[String]]("Veracode API key")
    val veracodeArtifact = settingKey[String]("artifact on the local filesystem")
    val veracodeAppName = settingKey[String]("application name")

    val veracodeResolveApiCredentials = taskKey[ApiCredentials]("veracodeResolveApiCredentials")
    val veracodeInitApi = taskKey[VeracodeApi]("veracodeInitApi")
    val veracodeCreateBuild = taskKey[Unit]("veracodeCreateBuild")
    val veracodeUploadFile = taskKey[Unit]("veracodeUploadFile")
    val veracodeUploadFileSandbox = taskKey[Unit]("veracodeUploadFileSandbox")
  }

  import autoImport._

  override lazy val projectSettings: Seq[Def.Setting[_]] = Seq(
    veracodeAppName := sbt.Keys.name.value,
    veracodeApiId := None,
    veracodeApiKey := None
  ) ++ allTasks

  private val allTasks = Seq(
    veracodeResolveApiCredentials := {
      val apiId = veracodeApiId.value.getOrElse {
        Option(System.getenv("VERACODE_API_ID")).getOrElse(throw new RuntimeException("missing Veracode API id"))
      }
      val apiKey = veracodeApiKey.value.getOrElse {
        Option(System.getenv("VERACODE_API_KEY")).getOrElse(throw new RuntimeException("missing Veracode API key"))
      }
      val creds = new ApiCredentials(apiId = apiId, apiKey = apiKey)
      System.out.println("veracodeResolveApiCredentials: " + creds)
      creds
    },
    veracodeInitApi := {
      val api = new VeracodeApiImpl(new VeracodeWrapperFactory(veracodeResolveApiCredentials.value), "appId1234", "sandboxId1234")
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
