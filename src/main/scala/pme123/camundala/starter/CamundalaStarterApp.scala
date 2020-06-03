package pme123.camundala.starter

import eu.timepit.refined.auto._
import org.springframework.boot.autoconfigure.SpringBootApplication
import pme123.camundala.app.appRunner.AppRunner
import pme123.camundala.cli.{ProjectInfo, StandardCliApp}
import pme123.camundala.model.bpmn.{Bpmn, Sensitive, StaticFile, Url, User}
import pme123.camundala.model.deploy.{CamundaEndpoint, Deploy, Deploys, DockerConfig}
import pme123.camundala.services.StandardApp
import pme123.camundala.services.StandardApp.StandardAppDeps
import zio.ZLayer

@SpringBootApplication
class CamundalaStarterApp

object CamundalaStarterApp extends StandardCliApp {

  protected val ident: String = "camunda-starter"
  protected val title = "Twitter Camundala Demo App"

  protected def projectInfo: ProjectInfo = ProjectInfo(
    ident,
    "pme123",
    "0.0.1",
    "https://github.com/pme123/camundala-starter",
    "MIT"
  )

  protected def appRunnerLayer: ZLayer[StandardAppDeps, Nothing, AppRunner] =
    StandardApp.layer(classOf[CamundalaStarterApp], StaticFile("bpmnModels.sc", "."))

}
object deploys {

  val camundaUrl: Url = "http://localhost:8085/rest"
  val camundaDevUrl: Url = "http://localhost:8088/rest"

  def restApi(endpoint: Url): CamundaEndpoint = CamundaEndpoint(endpoint, "kermit", Sensitive("kermit"))

  def standard(bpmns: Seq[Bpmn], additionalUsers: Seq[User] = Seq.empty): Deploys =
    Deploys(Set(
      Deploy("default", bpmns, DockerConfig(dockerDir = "docker",
        composeFiles = Seq("docker-compose-dev")),
        camundaEndpoint = restApi(camundaDevUrl),
        additionalUsers = additionalUsers
      ),
      Deploy("remote", bpmns,
        DockerConfig(dockerDir = "docker",
          composeFiles = Seq("docker-compose"),
          maybeReadyUrl = Some(camundaUrl),
          projectName = "camunda-remote"
        ),
        camundaEndpoint = restApi(camundaDevUrl),
        additionalUsers = additionalUsers
      )
    ))
}
