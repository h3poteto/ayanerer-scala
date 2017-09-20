import java.net.URLClassLoader

name := """ayanerer"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
  .settings(
    javaOptions in Test += "-Dconfig.file=conf/test.conf"
  )

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  guice,
  jdbc,
  evolutions,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.1" % Test,
  "mysql" % "mysql-connector-java" % "5.1.43",
  "net.databinder.dispatch" %% "dispatch-core" % "0.13.+",
  "io.spray" %%  "spray-json" % "1.3.3",
  "org.mockito" % "mockito-core" % "2.8.47" % Test,
  "org.scalikejdbc"      %% "scalikejdbc-play-initializer"   % "2.6.+",
  "org.scalikejdbc" %% "scalikejdbc-jsr310" % "2.5.2",
  "org.skinny-framework" %% "skinny-orm"                % "2.4.+",
  "com.adrianhurt" %% "play-bootstrap" % "1.2-P26-B3",
  "org.webjars" % "bootstrap" % "3.3.7-1" exclude("org.webjars", "jquery"),
  "org.webjars" % "jquery" % "3.2.1",
  "org.webjars" % "font-awesome" % "4.7.0",
  "org.webjars" % "bootstrap-datepicker" % "1.4.0" exclude("org.webjars", "bootstrap"),
  "com.typesafe.akka" %% "akka-persistence" % "2.5.+",
  "org.iq80.leveldb"  % "leveldb"          % "0.9",
  "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",
  "com.github.seratch" %% "awscala" % "0.6.+",
  "com.scalapenos" %% "stamina-json" % "0.1.+"
)

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
