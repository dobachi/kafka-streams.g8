lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      organization := "$organization$",
      scalaVersion := "2.13.3"
    )
  ),
  name := "$name$",
  libraryDependencies := Seq(
    library.kafkaClients,
    library.kafkaStreams,
    library.log4jCore,
    library.typesafeConfig,
    library.kafkaTest,
    library.scalactic,
    library.scalaTest
  )
).settings(
  assemblyMergeStrategy in assembly := {
    case PathList("jackson-databind-2.10.2.jar") => MergeStrategy.last
    case PathList("jackson-annotations-2.10.2.jar") => MergeStrategy.last
    case PathList("jackson-core-2.10.2.jar") => MergeStrategy.last
    case PathList("jackson-datatype-jdk8-2.10.2.jar") => MergeStrategy.last
    case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
    case _ => MergeStrategy.first
  }
)

lazy val library = new {

  val version = new {
    val kafkaVersion   = "2.6.0"
    val scalaTest      = "3.2.3"
    val log4jCore      = "2.13.3"
    val typesafeConfig = "1.3.4"
  }

  val kafkaClients   = "org.apache.kafka"         % "kafka-clients"        % version.kafkaVersion
  val kafkaStreams   = "org.apache.kafka"         %% "kafka-streams-scala" % version.kafkaVersion
  val log4jCore      = "org.apache.logging.log4j" % "log4j-core"           % version.log4jCore
  val typesafeConfig = "com.typesafe"             % "config"               % version.typesafeConfig

  val kafkaTest = "org.apache.kafka" % "kafka-streams-test-utils" % version.kafkaVersion
  val scalactic = "org.scalactic"    %% "scalactic"               % version.scalaTest
  val scalaTest = "org.scalatest"    %% "scalatest"               % version.scalaTest % "test"
}
