/*
  General Scala attributes
 */
scalaVersion := "2.13.2"

/*
  General project attributes
 */
organization := "pme123"
name := "camundala-starter"
version := "0.0.1"
description := "A Starting Project that get you started with Camundala"
organizationHomepage := Some(url("https://github.com/pme123"))

/*
  Project dependencies > Camundala Services will fetch all required modules.
  For Camundala and Spring Boot
 */
libraryDependencies ++= Seq(
  "pme123" %% "camundala-cli" % "0.0.7"
)

/*
  Packaging plugin
 */

// enable the Java app packaging archetype and Ash script (for Alpine Linux, doesn't have Bash)
enablePlugins(JavaAppPackaging, DockerPlugin)

// set the main entrypoint to the application that is used in startup scripts
//mainClass in Compile := Some("pme123.camundala.starter.CamundalaStarterApp")

Compile / mainClass := Some("pme123.camundala.services.CamundaApp")
// the Docker image to base on (alpine is smaller than the debian based one (120 vs 650 MB)
dockerBaseImage := "openjdk:11-jre-slim"

// Expose the Camunda Host Port
dockerExposedPorts ++= Seq(8085)

// creates tag 'latest' as well when publishing
dockerUpdateLatest := true
