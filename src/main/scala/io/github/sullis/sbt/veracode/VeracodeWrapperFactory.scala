package io.github.sullis.sbt.veracode

import com.veracode.apiwrapper.AbstractAPIWrapper
import com.veracode.apiwrapper.wrappers.ResultsAPIWrapper
import com.veracode.apiwrapper.wrappers.SandboxAPIWrapper
import com.veracode.apiwrapper.wrappers.UploadAPIWrapper

class VeracodeWrapperFactory(credentials: Either[ApiCredentials, UserCredentials]) {

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
  require(apiId.length > 0, "apiId")
  require(apiKey.length > 0, "apiKey")
}

case class UserCredentials(username: String, password: String) {
  require(username.length > 0, "username")
  require(password.length > 0, "password")
}

object CredentialsUtil {
  def setCredentials[W <: AbstractAPIWrapper](credentials: Either[ApiCredentials, UserCredentials], wrapper: W): W = {
    credentials match {
      case Left(api) => setCredentials(api, wrapper)
      case Right(user) => setCredentials(user, wrapper)
    }
    wrapper
  }

  def setCredentials[W <: AbstractAPIWrapper](credentials: ApiCredentials, wrapper: W): W = {
    wrapper.setUpApiCredentials(credentials.apiId, credentials.apiKey)
    wrapper
  }

  def setCredentials[W <: AbstractAPIWrapper](credentials: UserCredentials, wrapper: W): W = {
    wrapper.setUpCredentials(credentials.username, credentials.password)
    wrapper
  }
}