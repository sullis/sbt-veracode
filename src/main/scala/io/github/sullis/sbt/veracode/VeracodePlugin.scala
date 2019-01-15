package io.github.sullis.sbt.veracode

import sbt._
import sbt.Keys.packagedArtifacts

object VeracodePlugin extends AutoPlugin {

  object autoImport {
    val veracodeAppName = settingKey[String]("Veracode app name")

    val veracodeResolveAppId = taskKey[String]("veracodeResolveAppId")
    val veracodeSubmit = taskKey[Unit]("veracodeSubmit")
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
    veracodeSubmit := {
      System.out.println("veracodeSubmit")
      val appId = veracodeResolveAppId.value
      val file = packagedArtifacts.value.toList.filter(_._2.getName.endsWith(".jar")).head._2
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
