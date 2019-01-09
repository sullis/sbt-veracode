package io.github.sullis.sbt.veracode

trait VeracodeApi {
  def beginPreScan: String
  def beginPreScanSandbox: String
  def createBuild(buildVersion: String): String
  def getAppInfo: String
}

class VeracodeApiImpl(veracodeWrapperFactory: VeracodeWrapperFactory, appId: String, sandboxId: String)
  extends VeracodeApi {
  override def beginPreScan: String = ???

  override def beginPreScanSandbox: String = ???

  override def createBuild(buildVersion: String): String = ???

  override def getAppInfo: String = ???
}

