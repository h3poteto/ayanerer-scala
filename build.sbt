import java.net.URLClassLoader

name := """ayanerer"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
  .settings(
    registerTask("google-image-download", "tasks.GoogleImageTask", "image from google")
  )

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "mysql" % "mysql-connector-java" % "5.1.41",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
  "io.spray" %%  "spray-json" % "1.3.3",
  "com.typesafe.play" %% "play-slick" % "2.0.2",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.2",
  "org.mockito" % "mockito-core" % "2.7.19" % Test
)

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

