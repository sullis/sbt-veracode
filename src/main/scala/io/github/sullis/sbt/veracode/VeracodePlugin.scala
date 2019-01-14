package io.github.sullis.sbt.veracode

import sbt._

object VeracodePlugin extends AutoPlugin {

  object autoImport {
    val veracodeArtifact = settingKey[String]("artifact on the local filesystem")
    val veracodeAppName = settingKey[String]("Veracode app name")

    val veracodeResolveAppId = taskKey[String]("veracodeResolveAppId")
    val veracodeUploadAndScan = taskKey[Unit]("veracodeUploadAndScan")
  }

  import autoImport._

  override lazy val projectSettings: Seq[Def.Setting[_]] = Seq(
    veracodeAppName := sbt.Keys.name.value
  ) ++ allTasks

  private val api = new VeracodeApiImpl(new VeracodeWrapperFactory(credentials))

  private val allTasks = Seq(
    veracodeResolveAppId := {
      System.out.println("veracodeResolveAppId")
      val appId = api.fetchAppId(veracodeAppName.value)
      System.out.println("Veracode appName: " + veracodeAppName.value)
      System.out.println("Veracode appId: " + appId)
      appId
    },
    veracodeUploadAndScan := {
      System.out.println("veracodeUploadFile")
      val file = new File(veracodeArtifact.value)
      api.uploadFile(veracodeResolveAppId.value, file)
      api.beginScan(veracodeResolveAppId.value)
    }
  )

  private lazy val apiCredentials: Option[ApiCredentials] = {
    Option(System.getenv("VERACODE_API_ID")).map(apiId => ApiCredentials(apiId, System.getenv("VERACODE_API_KEY")))
  }

  private lazy val userCredentials: Option[UserCredentials] = {
    Option(System.getenv("VERACODE_USERNAME")).map(username => UserCredentials(username, System.getenv("VERACODE_PASSWORD")))
  }


  private lazy val credentials: Either[ApiCredentials, UserCredentials] = {
    apiCredentials.toLeft(userCredentials.getOrElse(throw new RuntimeException("missing Veracode credentials")))
  }

}
