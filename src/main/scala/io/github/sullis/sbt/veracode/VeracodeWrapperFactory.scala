package io.github.sullis.sbt.veracode

import com.veracode.apiwrapper.AbstractAPIWrapper
import com.veracode.apiwrapper.wrappers.ResultsAPIWrapper
import com.veracode.apiwrapper.wrappers.SandboxAPIWrapper
import com.veracode.apiwrapper.wrappers.UploadAPIWrapper
import scala.xml.XML

class VeracodeWrapperFactory(credentials: ApiCredentials) {

  import CredentialsUtil.setCredentials

  def fetchAppId(appName: String): String = {
    val responseString = this.uploadApi.getAppList
    val xml = XML.loadString(responseString)
    val apps = (xml \\ "applist" \\ "app")
    val app = apps.filter(a => {
      val appNameAttributeValue = a \@ "app_name"
      appNameAttributeValue.equals(appName)
    })
    app \@ "app_id"
  }

  def uploadApi(): UploadAPIWrapper = {
    setCredentials(credentials, new UploadAPIWrapper())
  }

  def resultsApi(): ResultsAPIWrapper = {
    setCredentials(credentials, new ResultsAPIWrapper())
  }

  def sandboxApi(): SandboxAPIWrapper = {
    setCredentials(credentials, new SandboxAPIWrapper())
  }
}

case class ApiCredentials(apiId: String, apiKey: String) {
  require(apiId != null, "apiId")
  require(apiKey != null, "apiKey")
}

object CredentialsUtil {
  def setCredentials[W <: AbstractAPIWrapper](credentials: ApiCredentials, wrapper: W): W = {
    wrapper.setUpApiCredentials(credentials.apiId, credentials.apiKey)
    wrapper
  }

}