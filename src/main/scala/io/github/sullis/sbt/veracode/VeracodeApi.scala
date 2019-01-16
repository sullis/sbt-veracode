package io.github.sullis.sbt.veracode

import java.io.File
import scala.xml.XML

trait VeracodeApi {
  def fetchAppId(appName: String): Option[String]
  def beginPreScan(appId: String): Either[VeracodeError, String]
  def deleteBuild(appId: String): Either[VeracodeError, String]
  def createBuild(appId: String, buildVersion: String): Either[VeracodeError, String]
  def getAppInfo(appId: String): String
  def uploadFile(appId: String, file: java.io.File): Either[VeracodeError, String]
  def isScanRunning(appId: String): Boolean
  def getScanStatus(appId: String): String
  def createApp(appName: String): String
}

class VeracodeApiImpl(veracodeWrapperFactory: VeracodeWrapperFactory)
  extends VeracodeApi {

  def getScanStatus(appId: String): String = {
    val responseString = veracodeWrapperFactory.uploadApi.getBuildInfo(appId)
    val xml = scala.xml.XML.loadString(responseString)
    val status = xml \\ "buildinfo" \\ "build" \\ "analysis_unit" \@ "status"
    status
  }

  override def isScanRunning(appId: String): Boolean = {
    val status = getScanStatus(appId)
    val result = !status.equals("Results Ready")
    result
  }

  override def createApp(appName: String): String = {
    val response = veracodeWrapperFactory.uploadApi.createApp(appName, "Very High")
    val xml = XML.loadString(response)
    xml \\ "application" \@ "app_id"
  }

  override def fetchAppId(appName: String): Option[String] = {
    val responseString = veracodeWrapperFactory.uploadApi.getAppList
    val xml = XML.loadString(responseString)
    val apps = (xml \\ "applist" \\ "app")
    val app = apps.filter(a => {
      val appNameAttributeValue = a \@ "app_name"
      appNameAttributeValue.equals(appName)
    })
    val rawAppId = app \@ "app_id"
    rawAppId.trim match {
      case "" => None
      case _ => Option(rawAppId.trim)
    }
  }


  override def beginPreScan(appId: String): Either[VeracodeError, String] = {
    val xml = veracodeWrapperFactory.uploadApi.beginPreScan(appId, null, "true", "true")
    checkForErrors(xml)
  }

  override def createBuild(appId: String, buildVersion: String): Either[VeracodeError, String] =  {
    val xml = veracodeWrapperFactory.uploadApi.createBuild(appId, buildVersion)
    checkForErrors(xml)
  }

  override def getAppInfo(appId: String): String =  {
    veracodeWrapperFactory.uploadApi.getAppInfo(appId)
  }

  override def uploadFile(appId: String, file: File): Either[VeracodeError, String] = {
    val filePath = file.getCanonicalPath
    if (!file.exists) {
      throw new RuntimeException(s"uploadFile: [${filePath} does not exist")
    }
    if (!file.canRead) {
      throw new RuntimeException(s"uploadFile: cannot read file [${filePath}]")
    }
    val xml = veracodeWrapperFactory.uploadApi.uploadFile(appId, filePath)
    checkForErrors(xml)
  }

  override def deleteBuild(appId: String): Either[VeracodeError, String] = {
    val responseString = veracodeWrapperFactory.uploadApi.deleteBuild(appId)
    checkForErrors(responseString)
  }

  private def checkForErrors(xmlResponse: String): Either[VeracodeError, String] = {
    val error = VeracodeXmlUtil.findError(xmlResponse)
    error match {
      case Some(e) => {
        System.err.println("Veracode API error: " + e + " in XML: " + xmlResponse)
        Left(e)
      }
      case _ => Right(xmlResponse)
    }

  }

}
