package io.github.sullis.sbt.veracode

import java.io.File

import scala.xml.XML

trait VeracodeApi {
  def fetchAppId(appName: String): String
  def beginScan(appId: String): String
  def createBuild(appId: String, buildVersion: String): Either[VeracodeError, String]
  def getAppInfo(appId: String): String
  def uploadFile(appId: String, file: java.io.File): String
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


  override def beginScan(appId: String): String = {
    veracodeWrapperFactory.uploadApi.beginScan(appId, null, null)
  }
  override def createBuild(appId: String, buildVersion: String): Either[VeracodeError, String] =  {
    System.out.println("createBuild: appId=" + appId + " with buildVersion " + buildVersion)
    val responseXml = veracodeWrapperFactory.uploadApi.createBuild(appId, buildVersion)
    System.out.println("XML: " + responseXml)
    val error = VeracodeXmlUtil.findError(responseXml)
    error match {
      case Some(e) => Left(e)
      case _ => Right("buildId-FIXME")
    }
  }

  override def getAppInfo(appId: String): String =  {
    veracodeWrapperFactory.uploadApi.getAppInfo(appId)
  }

  override def uploadFile(appId: String, file: File): String = {
    val xmlResponse = veracodeWrapperFactory.uploadApi.uploadFile(appId, file.getCanonicalPath)
    System.out.println("uploadFile response: " + xmlResponse)
    xmlResponse
  }

}
