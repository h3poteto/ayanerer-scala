import java.net.URLClassLoader
name := """ayanerer"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
  .settings(
    registerTask("google-image-download", "tasks.GoogleImageTask", "image from google")
  )

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "mysql" % "mysql-connector-java" % "5.1.41"
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
