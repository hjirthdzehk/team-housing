resolvers ++= Seq(
  "sonatype releases" at "http://oss.sonatype.org/content/repositories/releases",
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
)
libraryDependencies ++= Seq("com.h2database" % "h2" % "1.4.191",
                            "postgresql" % "postgresql" % "9.1-901.jdbc4")

addSbtPlugin("org.scalikejdbc"   % "scalikejdbc-mapper-generator" % "2.4.2")
addSbtPlugin("com.typesafe.play" % "sbt-plugin"                   % "2.5.3")
addSbtPlugin("com.typesafe.sbt"  % "sbt-coffeescript"             % "1.0.0")
addSbtPlugin("com.timushev.sbt"  % "sbt-updates"                  % "0.1.10")
