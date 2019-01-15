organization := "io.github.sullis"

name := "sbt-veracode"

description := "SBT plugin for Veracode code analyzer"

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

bintrayOrganization in bintray := None

bintrayPackageLabels := Seq("sbt", "veracode")

bintrayRepository := "sbt-plugins"

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.html"))

val awsSdkVersion = "2.3.1"

libraryDependencies ++= Seq(
  "com.veracode.vosp.api.wrappers" % "vosp-api-wrappers-java" % "18.12.5.5",
  "software.amazon.awssdk" % "kms" % awsSdkVersion,
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
)
