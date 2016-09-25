name := "spark-pruebas"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += "OSS Sonatype" at "https://repo1.maven.org/maven2/"
//resolvers += "es 2_11" at "https://mvnrepository.com/artifact/org.elasticsearch/elasticsearch-spark-20_2.11"



//libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "1.6.2" //% "provided"
libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "2.0.0" % "provided"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.3.4"
libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "1.1.1"
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-8" % "2.0.0" //% "provided"
//libraryDependencies += "org.elasticsearch" %% "elasticsearch-spark" % "2.4.0"
libraryDependencies += "org.elasticsearch" % "elasticsearch-spark-20_2.11" % "5.0.0-alpha5" % "provided"

//lazy val http4sVersion = "0.14.1"
//resolvers += Resolver.sonatypeRepo("snapshots")
//libraryDependencies += "com.ibm" %% "couchdb-scala" % "0.7.2"
/*libraryDependencies +=  "org.http4s" %% "http4s-dsl" % http4sVersion
libraryDependencies += "org.http4s" %% "http4s-blaze-server" % http4sVersion
libraryDependencies += "org.http4s" %% "http4s-blaze-client" % http4sVersion*/

assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*) => MergeStrategy.last
  case PathList("javax", "activation", xs @ _*) => MergeStrategy.last
  case PathList("org", "apache", xs @ _*) => MergeStrategy.last
  case PathList("com", "google", xs @ _*) => MergeStrategy.last
  case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
  case PathList("com", "codahale", xs @ _*) => MergeStrategy.last
  case PathList("com", "yammer", xs @ _*) => MergeStrategy.last
  case "about.html" => MergeStrategy.rename
  case "META-INF/ECLIPSEF.RSA" => MergeStrategy.last
  case "META-INF/mailcap" => MergeStrategy.last
  case "META-INF/mimetypes.default" => MergeStrategy.last
  case "plugin.properties" => MergeStrategy.last
  case "log4j.properties" => MergeStrategy.last
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}