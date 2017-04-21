import java.net.URLClassLoader

name := """ayanerer"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
  .settings(
    javaOptions in Test += "-Dconfig.file=conf/test.conf"
  )

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  cache,
  ws,
  jdbc,
  evolutions,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "mysql" % "mysql-connector-java" % "5.1.41",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
  "io.spray" %%  "spray-json" % "1.3.3",
  "org.mockito" % "mockito-core" % "2.7.19" % Test,
  "org.scalikejdbc"      %% "scalikejdbc-play-initializer"   % "2.5.+",
  "org.scalikejdbc" %% "scalikejdbc-jsr310" % "2.5.+",
  "org.skinny-framework" %% "skinny-orm"                % "2.3.+",
  "com.adrianhurt" %% "play-bootstrap" % "1.1.1-P25-B3-SNAPSHOT" excludeAll(ExclusionRule(organization = "org.webjars")),
  "com.typesafe.akka" %% "akka-persistence" % "2.4.17",
  "org.iq80.leveldb"  % "leveldb"          % "0.7",
  "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",
  "com.github.seratch" %% "awscala" % "0.6.+",
  "com.scalapenos" %% "stamina-json" % "0.1.3"
)

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
