package pme123.camundala.starter

import pme123.camundala.camunda.delegate.RestServiceDelegate.RestServiceTempl
import pme123.camundala.camunda.service.restService.Request
import pme123.camundala.camunda.service.restService.RequestPath.Path
import pme123.camundala.model.bpmn.{BpmnNodeId, PathElem, ServiceTask}

case class SwapiService(category: PathElem) {

  def asServiceTask(id: BpmnNodeId): ServiceTask =
  RestServiceTempl(
    Request(
      bpmns.swapiHost,
      path = Path(category),
      responseVariable = "swapiResult"
    )
  ).asServiceTask(id)
}
