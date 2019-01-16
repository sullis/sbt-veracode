# sbt-veracode

SBT plugin for Veracode

# Usage

project/plugins.sbt

```
resolvers += Resolver.bintrayIvyRepo("sullis", "sbt-plugins")

addSbtPlugin("io.github.sullis" % "sbt-veracode" % "0.0.8")
```

build.sbt

```
enablePlugins(VeracodePlugin)
```


# Unit testing

```

sbt scripted

```

# Related 
* https://mvnrepository.com/artifact/com.veracode.vosp.api.wrappers/vosp-api-wrappers-java
* https://github.com/calgaryscientific/veracode-gradle-plugin

