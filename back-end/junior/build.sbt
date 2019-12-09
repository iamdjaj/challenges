lazy val `yieldstreet-challenges` = (project in file("."))
  .settings(
    name := "yieldstreet-challenges",
    libraryDependencies ++= Seq(
      "junit" % "junit" % "4.12" % Test,
      "org.hamcrest" % "hamcrest" % "2.1" % Test,
      "org.mockito" % "mockito-core" % "2.27.0" % Test,
      "com.novocode" % "junit-interface" % "0.11" % Test
    )
  )