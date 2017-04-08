import java.net.URLClassLoader

name := """ayanerer"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
  .settings(
    registerTask("seed-image-download", "tasks.SeedImageTask", "image from google"),
    registerTask("daily-image-download", "tasks.DailyImageTask", "image from google"),
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
  "org.skinny-framework" %% "skinny-orm"                % "2.3.+",
  "com.adrianhurt" %% "play-bootstrap" % "1.1.1-P25-B3-SNAPSHOT" excludeAll(ExclusionRule(organization = "org.webjars"))
)

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"


def registerTask(name: String, taskClass: String, description: String) = {
  val sbtTask = (dependencyClasspath in Runtime) map { (deps) =>
    val depURLs = deps.map(_.data.toURI.toURL).toArray
    val classLoader = new URLClassLoader(depURLs, null)
    val task = classLoader.
                 loadClass(taskClass).
                 newInstance().
                 asInstanceOf[Runnable]
    task.run()
  }
  TaskKey[Unit](name, description) <<= sbtTask.dependsOn(compile in Compile)
}

