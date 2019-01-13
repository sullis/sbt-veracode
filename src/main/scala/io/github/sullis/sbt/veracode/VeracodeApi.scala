package io.github.sullis.sbt.veracode

import java.io.File

trait VeracodeApi {
  def beginScan: String
  def createBuild(buildVersion: String): Either[VeracodeError, String]
  def getAppInfo: String
  def uploadFile(file: java.io.File): String
}

class VeracodeApiImpl(veracodeWrapperFactory: VeracodeWrapperFactory, appId: String)
  extends VeracodeApi {
  override def beginScan: String = {
    veracodeWrapperFactory.uploadApi.beginScan(appId, null, null)
  }
  override def createBuild(buildVersion: String): Either[VeracodeError, String] =  {
    System.out.println("createBuild: appId=" + appId + " with buildVersion " + buildVersion)
    val responseXml = veracodeWrapperFactory.uploadApi.createBuild(appId, buildVersion)
    System.out.println("XML: " + responseXml)
    val error = VeracodeXmlUtil.findError(responseXml)
    error match {
      case Some(e) => Left(e)
      case _ => Right("buildId-FIXME")
    }
  }

  override def getAppInfo: String =  {
    veracodeWrapperFactory.uploadApi.getAppInfo(appId)
  }

  override def uploadFile(file: File): String = {
    val xmlResponse = veracodeWrapperFactory.uploadApi.uploadFile(appId, file.getCanonicalPath)
    System.out.println("uploadFile response: " + xmlResponse)
    xmlResponse
  }

}
