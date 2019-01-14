
enablePlugins(VeracodePlugin)
name := "svc-mq-consumer"

version := "1.0." + System.currentTimeMillis

scalaVersion := "2.12.8"

veracodeArtifact := s"target/scala-2.12/${name.value}_2.12-${version.value}.jar"

