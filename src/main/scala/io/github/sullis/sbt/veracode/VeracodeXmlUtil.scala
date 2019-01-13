package io.github.sullis.sbt.veracode

import scala.xml.XML

object VeracodeXmlUtil {
  def findError(s: String): Option[VeracodeError] = {
    val xml = XML.loadString(s)
    val nodes = (xml \\ "error")
    nodes.headOption.map(_.text).map(VeracodeError(_))
  }
}
