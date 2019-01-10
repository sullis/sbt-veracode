package io.github.sullis.sbt.veracode

import com.veracode.apiwrapper.AbstractAPIWrapper
import com.veracode.apiwrapper.wrappers.ResultsAPIWrapper
import com.veracode.apiwrapper.wrappers.SandboxAPIWrapper
import com.veracode.apiwrapper.wrappers.UploadAPIWrapper

class VeracodeWrapperFactory(credentials: ApiCredentials) {

  import CredentialsUtil.setCredentials

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
    wrapper.setUpCredentials(null, null)
    wrapper.setUpApiCredentials(credentials.apiId, credentials.apiKey)
    wrapper
  }

}