[![Build Status](https://travis-ci.com/jembi/bsis.svg?token=FCDs4JFMycqVrLvQpU5A&branch=develop)](https://travis-ci.com/jembi/bsis) [![codecov.io](https://codecov.io/github/jembi/bsis/coverage.svg?branch=develop&token=hU3P8AQ1o0)](https://codecov.io/github/jembi/bsis/coverage.svg?branch=develop&token=hU3P8AQ1o0)

BSIS
====

BSIS (Blood Safety Information System) was a project started at Georgia Institute of Technology as part of the [Computing For Good (C4G)](http://www.cc.gatech.edu/about/advancing/c4g/) Program at Georgia Tech, under the name V2V (Vein to Vein). 
The project is now maintained by [Jembi Health Systems](http://www.jembi.org) in South Africa.

# Background

BSIS is a software to manage blood related information from the point of donation through testing, storage to its eventual usage in a hospital or a clinic. 
Handling of blood is a sensitive operation and record of movement of blood as it goes through the following chain is critical to the effective functioning of blood processing centers. 
This solution is primary targeted for deployment in the developing countries of Africa where much of the blood inventory tracking is done on paper.

![Lifecycle of Donated Blood] (https://raw.github.com/jembi/bsis/master/lifecycle_blood.png)

The software was developed at Georgia Tech as part of the C4G program in collaboration with Centers for Disease Control and Prevention (CDC), Safe Blood for Africa (SBFA) and the participating countries of Africa, and is now developed and maintained by Jembi Health Systems (JHS) in South Africa.

# [Video Introduction to BSIS] (http://www.youtube.com/watch?v=O_zIIXepPHc)

## Problems related to blood supply in Africa

* Unsafe blood can lead to disease transmissions
* Insufficient supply of Safe Blood in hospitals point-of-care
* Higher number of individuals living with HIV in Africa
* Shortage of blood
* Many countries of Africa depend on paper based systems to track the movement of blood through the blood processing chain

[This factsheet] (http://www.who.int/mediacentre/factsheets/fs279/en/index.html) from WHO discusses the problems related to blood safety and availability specifically the developing countries of Africa in greater detail.

## Challenges

 * Transitioning from a paper based system to a computer based system can take time and experience
 * Varying blood processing practices across different countries
 * Unreliable Internet connectivity
 * Developing intuitive and easy-to-use admin. Cannot depend on an onsite admin to configure databases.

## Solution

The challenges listed above have influenced the design of BSIS and the following design features have resulted.
 * Develop a browser based but locally deployable solution to avoid dependence on Internet
 * Smaller upgrade package size to allow 
 * Make features configurable so that blood processing centers can turn them on/off based on their current practice
 * A single installer which installs and configures all dependencies like Java, MySQL, Apache Tomcat etc. to make first time configuration simple.

Many other design decisions in the future will be influenced by the same parameters.

# Developer Documentation

## Technology

### Production Code

* Language: [Java](http://www.oracle.com/technetwork/java/javase/overview/index.html)
* Application Server (Servlet Container): [Apache Tomcat](http://tomcat.apache.org/)
* Application and Web Framework: [Spring](https://spring.io/projects/spring-framework)
* Object Relational Mapper: [Hibernate](http://hibernate.org/)
* Database Management: [Liquibase](https://www.liquibase.org/)

### Test Code

* [Spring Test](https://docs.spring.io/spring/docs/4.1.x/spring-framework-reference/htmlsingle/#testing)

### Tooling

* Build: [Maven](https://maven.apache.org/)
* Containerization: [Docker](https://www.docker.com/)
* Database: [MySQL CE](https://www.mysql.com/products/community/)

## Build, Run and Test

### Build

* With Maven: `mvn compile`
* With Docker Compose: `sudo docker-compose build bsis`

### Run

* With Docker Compose: `sudo docker-compose --build up`
* With Bash Script: `./start.sh` (runs docker-compose internally)

### Test

* With Maven: `mvn test`

## Conventions

### Code Style

  * Use 2 spaces instead of tabs to indent your code. 
  * Use the appropriate [codestyles](codestyles) template for your IDE. These codestyles are based on the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).
  * Only auto format the code you are working on.

### Git Commit Messages

Use the supplied Git commit message template to ensure that commit messages conform to the project standards. 
The first line of the commit message should contain a JIRA ticket reference and a short description of the commit (50 characters max).  
The following lines (72 characters max) should describe why the change is being made, what the problem was, and may contain external references if necessary.

In your BSIS Git repository folder, run the following command:

``` git config commit.template .git.template ```

This will set the git commit template for the local git repository only. 
If you'd like to set the template for all your Git repositories, then run the following command:

``` git config --global commit.template .git.template ```

## Getting Started

Starting work on a new code base even when it uses similar versions and tooling that you are used can be frustrating. 
Here are a few tips to help you get coding quickly, pain free!

### With Docker

1. Install the required tooling: [Maven](https://maven.apache.org/) and [Docker](https://www.docker.com/).
1. Change the host name part of the database url in [bsis.properties](src/main/resources/bsis.properties) from `localhost` to `mysql` the name of the docker container.
1. Start the application with the bash script `./start.sh` which will download the dependencies, start the application and create the database.
NB. Creating the database from scratch takes quite a long time (10s of minutes) so it might be easier to grab a database dump from another developer.
 
### Without Docker

1. This repository contains some Eclipse specific configuration. If you are comfortable using Eclipse then setting up your development environment will be relatively easier.
   You may use another IDE if you wish to.
    * [Install Eclipse Indigo 3.7 or Eclipse Juno 4.2](http://www.eclipse.org/downloads/).
    * Install [Apache Tomcat 7](http://tomcat.apache.org/) and [integrate with Eclipse](http://www.coreservlets.com/Apache-Tomcat-Tutorial/tomcat-7-with-eclipse.html).
    * Clone the repository into a local directory preferably housed in your eclipse workspace and checkout the develop branch.
    * The project dependencies are configured using [Maven](http://maven.apache.org/). Install Eclipse plugin [m2eclipse](http://maven.apache.org/eclipse-plugin.html) for easier integration of Maven with your development environment. In Eclipse, select Help>Install New Software and add the site http://download.eclipse.org/technology/m2e/releases to add the m2eclipse plugin.Setup m2eclipse so that it automatically downloads all the required dependencies, sources, javadocs.
    * Import the source code into Eclipse by using 'Import Existing project' option. The master branch contains the required eclipse project files (e.g. .project and .settings), so you should be able to start working right away. For other IDE's you will need to more work.
2. You will also need to setup a [MySQL CE](https://www.mysql.com/products/community/) database:
    *  Run the Maven build `mvn liquibase:update` which will call the liquibase scripts to create and initialise your database.
    * `src/main/resources/bsis.properties` contains your database connection information.
    * Note: Liquibase is executed on application startup, so this step is optional.
