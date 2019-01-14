package io.github.sullis.sbt.veracode

import java.io.File

import scala.xml.XML

trait VeracodeApi {
  def fetchAppId(appName: String): String
  def beginPreScan(appId: String): Either[VeracodeError, String]
  def createBuild(appId: String, buildVersion: String): Either[VeracodeError, String]
  def getAppInfo(appId: String): String
  def uploadFile(appId: String, file: java.io.File): Either[VeracodeError, String]
}

class VeracodeApiImpl(veracodeWrapperFactory: VeracodeWrapperFactory)
  extends VeracodeApi {

  override def fetchAppId(appName: String): String = {
    val responseString = veracodeWrapperFactory.uploadApi.getAppList
    val xml = XML.loadString(responseString)
    val apps = (xml \\ "applist" \\ "app")
    val app = apps.filter(a => {
      val appNameAttributeValue = a \@ "app_name"
      appNameAttributeValue.equals(appName)
    })
    app \@ "app_id"
  }


  override def beginPreScan(appId: String): Either[VeracodeError, String] = {
    val xml = veracodeWrapperFactory.uploadApi.beginPreScan(appId, null, "true", "true")
    checkForErrors(xml)
  }

  override def createBuild(appId: String, buildVersion: String): Either[VeracodeError, String] =  {
    System.out.println("createBuild: appId=" + appId + " with buildVersion " + buildVersion)
    val xml = veracodeWrapperFactory.uploadApi.createBuild(appId, buildVersion)
    checkForErrors(xml)
  }

  override def getAppInfo(appId: String): String =  {
    veracodeWrapperFactory.uploadApi.getAppInfo(appId)
  }

  override def uploadFile(appId: String, file: File): Either[VeracodeError, String] = {
    val filePath = file.getCanonicalPath
    System.out.println(s"uploadFile: filePath[${filePath}] appId[$appId]")
    if (!file.exists) {
      throw new RuntimeException(s"uploadFile: [${filePath} does not exist")
    }
    if (!file.canRead) {
      throw new RuntimeException(s"uploadFile: cannot read file [${filePath}]")
    }
    val xml = veracodeWrapperFactory.uploadApi.uploadFile(appId, filePath)
    checkForErrors(xml)
  }

  private def checkForErrors(xmlResponse: String): Either[VeracodeError, String] = {
    System.out.println("XML response: " + xmlResponse)
    val error = VeracodeXmlUtil.findError(xmlResponse)
    error match {
      case Some(e) => {
        System.err.println("Veracode API error: " + e)
        Left(e)
      }
      case _ => Right(xmlResponse)
    }

  }
}
