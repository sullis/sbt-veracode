organization := "io.github.sullis"

name := "sbt-veracode"

crossSbtVersions := List("0.13.18", "1.2.8")

enablePlugins(SbtPlugin, GitVersioning, GitBranchPrompt)

scriptedLaunchOpts := { scriptedLaunchOpts.value ++
  Seq("-Xmx1024M", "-XX:MaxPermSize=256M", "-Dplugin.version=" + version.value)
}

scriptedBufferLog := false

git.useGitDescribe := true

scalacOptions ++= List(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-encoding", "UTF-8"
)

publishMavenStyle := false

bintrayOrganization := Some("TODO")

bintrayPackageLabels := Seq("sbt", "veracode")

bintrayRepository := "sbt-plugin-releases"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

val awsSdkVersion = "2.3.0"

libraryDependencies ++= Seq(
  "com.veracode.vosp.api.wrappers" % "vosp-api-wrappers-java" % "18.12.5.5",
  "software.amazon.awssdk" % "kms" % awsSdkVersion,
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
)
