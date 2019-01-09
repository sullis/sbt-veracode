package io.github.sullis.sbt.veracode

import com.veracode.apiwrapper.AbstractAPIWrapper
import com.veracode.apiwrapper.wrappers.ResultsAPIWrapper
import com.veracode.apiwrapper.wrappers.SandboxAPIWrapper
import com.veracode.apiwrapper.wrappers.UploadAPIWrapper

class VeracodeWrapperFactory(credentials: Either[UserCredentials, ApiCredentials]) {

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

case class UserCredentials(username: String, password: String)

case class ApiCredentials(key: String, id: String)

object CredentialsUtil {
  def setCredentials[W <: AbstractAPIWrapper](credentials: Either[UserCredentials, ApiCredentials], wrapper: W): W = {
    credentials match {
      case Left(uc) => wrapper.setUpCredentials(uc.username, uc.password)
      case Right(ac) => wrapper.setUpApiCredentials(ac.key, ac.id)
    }
    wrapper
  }

}