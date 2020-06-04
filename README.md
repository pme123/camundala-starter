# camundala-starter
An simple example that you can clone to start your own Camundala Project.

The idea is to use it to get your first BPMN Project done with Camundala step by step.

At the moment you have to publish first [Camundala](https://github.com/pme123/camundala):

    mill __.publishLocal

# How-To
This chapter describes the most important parts working with Camundala.
>Be aware that this is done on a Mac.
# Constraints
To go through the next steps smoothly you should have the following installed:
* brew
* docker
* git client
* sbt
* Intellij with Scala Plugin
# Start a new Project
Clone this project:

    cd ~/your-projects-path
    git clone https://github.com/pme123/camundala-starter.git

Adjust project:

    # remove Git config
    rm -rf camundala-starter/.git
    # replace all camundala-starter with your project name
    brew install gnu-sed
    grep -rl camundala-starter camundala-starter | xargs gsed -i'' 's/camundala-starter/my-project-name/g'
    # rename project folder
    mv camundala-starter my-project-name 
    
Open Project in your favourite IDE, like Intellij or VS Code.

# Development
## Start Environment
Start the Camunda CLI (`CamundalaStarterApp`), directly in _Intellij_.

At the moment `sbt run` does not work - see [Stackoverflow](https://stackoverflow.com/questions/62131981/scala-script-engine-is-not-found-when-run-in-test-using-mill-or-sbt)

This will start the Camundala CLI, which starts also a **Camunda Spring App** 
and an extra HTTP Endpoint for **Camundala** specific services.

> If Camunda cannot connect to the database - no worries - we have to start the DB-Docker image first.

Start the DB-Docker
    
    docker up
This starts the database in a Docker Image (`docker/docker-compose-dev.yml`).

Now you can restart the App

    app restart

### Help    
To get the description for the CLI (Command Line Interface):
* base commands: `--help`
* sub commands, for example: `docker --help`  
> The logs are printed directly in the console - so do not get confused.   

### DeployId
The `deployId` refers to the Id of the Deployment.
A lot of CLI commands use this Id to do stuff on a configured Deployment.

This allows us for example to deploy our BPMN to a Remote Server.
 
This project comes with 2 predefined Deployments:
 * `default`: Deploys to the Camunda started with _CamundalaStarterApp_. 
   If you do not define a `deployId`, Camundala takes this one.
 * `remote`: Uses to the Camunda started with `docker-compose`.

# BPMN Workflow  
## BPMN Diagram 
The starter gives you a starting BPMN. 
Just open `src/main/resources/bpmn/StarterProcess.bpmn` in the Camunda Modeler.
> A base concept is that the model contains only the business flow.
>Everything else is done with _Scala Case Classes_.

The link between the Model and the classes are the Process Id and the Ids of the Flow elements.

#### Deploy BPMN from the Modeler
You can deploy changes directly in the Modeler.
In the Modeler Deploy Diagram:
* REST Endpoint: http://localhost:8888`</deployId>`
* Authentication: HTTP Basic
  * kermit
  * kermit

![image](https://user-images.githubusercontent.com/3437927/83785454-9d6f3080-a691-11ea-98db-2ded96b9e245.png)  
  
> It indicates if the Connection is successful.

## BPMN Model
Let's have a look at the model which describes the Model of the BPMN.

When starting the StarterApp you define the BPMN Script, that contains 
all your BPMNs and Deploys:
`StandardApp.layer(classOf[CamundalaStarterApp], StaticFile("bpmnModels.sc", "bpmn"))` 

### Generating the Model
After you created the BPMN in the Modeler, it is time to add the logic.
To simplify this process you can use `generate` in the CLI. 
This creates the Case Classes for the configured BPMNs:

    Bpmn("StarterProcess.bpmn",
       StaticFile("StarterProcess.bpmn", "bpmn"),
       List(
          BpmnProcess("SwapiProcess",
                userTasks = List(UserTask("ShowResultTask")),
                serviceTasks = List(ServiceTask("CallSwapiServiceTask")),
    ...

The BPMN Models must be defined in `bpmnModels.sc` like:

    Deploys.standard(Seq(Bpmn("StarterProcess.bpmn",
                           StaticFile("StarterProcess.bpmn", "bpmn"),
                           List(...))))

### Deploy from the CLI  
When changing your model you have to update the app.
Use `app update` - this will compile your model and register in the app.

With `deploy create <deployId>` you can deploy your adjusted Model-Script.
It will print warnings if there is something missing in your model:

    - Warnings Some(StarterProcess.bpmn):
     - ValidateWarning(There is NOT a StartEvent with id 'ShowStarWarsPlanetsStartEvent' in Scala.)

## Working Remote
It is possible to deploy the BPMN Diagram and the Scala Model to a Remote Installation.

However when changing something in your Scala Code, the following steps are required:

#### Publish Docker Image
In the _SBT-Console_: `sbt docker:publishLocal`

In the CLI: `docker up remote`

This starts your local docker at `8085` - 
use `docker:publish` with an according Repo if you have a _real_ remote Server.

Now you can deploy your BPMNs to the remote Server, like: `deploy create remote`

>Be aware that users are only created locally with `createUsers`. 
>For now you have to create them in the Cockpit (which is what you normally want).

# Background
Why are the Models Scala Script?
* The advantage of this is that you don't have to restart Camunda, to get Changes in your model.
  However if you make changes in your Scala code, you have to restart Camunda, resp. build a new Docker Image.
  
  >You can also move parts to Scala if they are stable and speed up the update process.

Why _SBT_ and not _Mill_?
* The assembly of Mill did not satisfy the Spring Boot requirements.
