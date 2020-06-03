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
## Create the BPMN    

# Background
Why _SBT_ and not _Mill_?
* The assembly of Mill did not satisfy the Spring Boot requirements.
