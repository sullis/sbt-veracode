package io.github.sullis.sbt.veracode

import sbt._
import sbt.Keys.packagedArtifacts

object VeracodePlugin extends AutoPlugin {

  object autoImport {
    val veracodeAppName = settingKey[String]("Veracode app name")

    val veracodeArtifact = taskKey[File]("Artifact to upload to Veracode")

    val veracodeResolveAppId = taskKey[String]("veracodeResolveAppId")
    val veracodeSubmit = taskKey[Unit]("veracodeSubmit")
  }

  import autoImport._

  override lazy val projectSettings: Seq[Def.Setting[_]] = Seq(
    veracodeAppName := sbt.Keys.name.value,
    veracodeArtifact := identifyArtifact(packagedArtifacts.value)
  ) ++ allTasks

  private lazy val api: VeracodeApi = new VeracodeApiImpl(new VeracodeWrapperFactory(credentials))

  private def identifyArtifact(artifacts: Map[Artifact, File]): File = {
    artifacts.toList.map(_._2).filter(isValidJar(_)).head
  }

  private def isValidJar(file: File): Boolean = {
    file.getName.endsWith(".jar") &&
      !file.getName.endsWith("javadoc.jar") &&
      !file.getName.endsWith("sources.jar")
  }

  private val allTasks = Seq(
    veracodeResolveAppId := {
      System.out.println("veracodeResolveAppId")
      val appName = veracodeAppName.value
      val appId = api.fetchAppId(appName) getOrElse api.createApp(appName)

      System.out.println("Veracode appName: " + appName)
      System.out.println("Veracode appId: " + appId)

      appId
    },
    veracodeSubmit := {
      System.out.println("veracodeSubmit")
      val appId = veracodeResolveAppId.value
      val file = veracodeArtifact.value
      System.out.println("Veracode artifact file: " + file.getCanonicalPath)

      if (api.isScanRunning(appId)) {
        api.deleteBuild(appId)
      }
      api.uploadFile(appId, file)
      api.beginPreScan(appId)
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
