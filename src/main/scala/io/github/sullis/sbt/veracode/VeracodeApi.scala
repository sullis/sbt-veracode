package io.github.sullis.sbt.veracode

import java.io.File

trait VeracodeApi {
  def beginPreScan: String
  def beginPreScanSandbox: String
  def createBuild(buildVersion: String): String
  def getAppInfo: String
  def uploadFile(file: java.io.File): String
  def uploadFileSandbox(file: java.io.File): String
}

class VeracodeApiImpl(veracodeWrapperFactory: VeracodeWrapperFactory, appId: String, sandboxId: String)
  extends VeracodeApi {
  override def beginPreScan: String = {
    veracodeWrapperFactory.uploadApi.beginPreScan(appId)
  }

  override def beginPreScanSandbox: String =  {
    veracodeWrapperFactory.uploadApi.beginPreScan(appId, sandboxId)
  }

  override def createBuild(buildVersion: String): String =  {
    veracodeWrapperFactory.uploadApi.createBuild(appId, buildVersion)
  }

  override def getAppInfo: String =  {
    veracodeWrapperFactory.uploadApi.getAppInfo(appId)
  }

  override def uploadFile(file: File): String = {
    veracodeWrapperFactory.uploadApi.uploadFile(appId, file.getCanonicalPath)
  }

  override def uploadFileSandbox(file: File): String = {
    veracodeWrapperFactory.uploadApi.uploadFile(appId, file.getCanonicalPath, sandboxId)
  }
}

