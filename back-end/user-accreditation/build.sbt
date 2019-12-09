lazy val `user-accreditation` = (project in file("."))
  .enablePlugins(PlayService, RoutesCompiler)
  .settings(
    name := "user-accreditation",
    version := "1.0",
    scalaVersion := "2.12.2",
    libraryDependencies := Seq(
      guice,
      logback,
      javaCore,
      akkaHttpServer,
      "org.projectlombok" % "lombok" % "1.18.10",
      "com.typesafe.akka" %% "akka-slf4j" % "2.5.26",
      "com.typesafe.akka" %% "akka-cluster-sharding" % "2.5.26",
      "com.typesafe.akka" %% "akka-stream-kafka" % "1.0.4",
      "org.testcontainers" % "kafka" % "1.12.3",
      "org.hamcrest" % "hamcrest" % "2.2" % Test,
      "com.typesafe.akka" %% "akka-testkit" % "2.5.26" % Test
    )
  )
