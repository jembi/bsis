[![Build Status](https://secure.travis-ci.org/jembi/bsis.png)](http://travis-ci.org/jembi/bsis)

BSIS
====

BSIS (Blood Safety Information System) was a project started at Georgia Institute of Technology as part of the [Computing For Good (C4G)](http://www.cc.gatech.edu/about/advancing/c4g/) Program at Georgia Tech, under the name V2V (Vein to Vein). The project is now maintained by [Jembi Health Systems](http://www.jembi.org) in South Africa.

# Background
BSIS is a software to manage blood related information from the point of donation through testing, storage to its eventual usage in a hospital or a clinic. Handling of blood is a sensitive operation and record of movement of blood as it goes through the following chain is critical to the effective functioning of blood processing centers. This solution is primary targeted for deployment in the developing countries of Africa where much of the blood inventory tracking is done on paper.

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


Development Environment
-----------------------
1. This repository contains some Eclipse specific configuration. If you are comfortable using Eclipse then setting up your development environment will be relatively easier.
   You may use another IDE if you wish to.
    * Download and unpack Eclipse Indigo 3.7 or Eclipse Juno 4.2 from http://www.eclipse.org/downloads/.
    * Install Apache Tomcat 7 and integrate with Eclipse (you may follow http://www.coreservlets.com/Apache-Tomcat-Tutorial/tomcat-7-with-eclipse.html).
    * Fork and clone the master branch into a local directory preferrably housed in your eclipse workspace.
    * The project dependencies are configured using [Maven] (http://maven.apache.org/). Install Eclipse plugin [m2eclipse] (http://maven.apache.org/eclipse-plugin.html) for easier integration of Maven with your development environment. In Eclipse, select Help>Install New Software and add the site http://download.eclipse.org/technology/m2e/releases to add the m2eclipse plugin.Setup m2eclipse so that it automatically downloads all the required dependencies, sources, javadocs.
    * Import the source code into Eclipse by using 'Import Existing project' option. The master branch contains the required eclipse project files (e.g. .project and .settings), so you should be able to start working right away. For other IDE's you will need to more work.
    * If there are issues with Maven dependencies, try run > mvn dependency:tree in the Workspace folder of the BSIS project (see http://stackoverflow.com/questions/4262186/missing-maven-dependencies-in-eclipse-project).
    * Sometimes we get NoClassDefFound errors for ContextLoaderListener and some other classes. To fix this error right click Project, select properties, then Deployment Assembly and then add Maven Dependencies to WEB-INF/lib. This should fix the errors.
      http://stackoverflow.com/questions/6083501/maven-dependencies-not-visible-in-web-inf-lib
      Note that the Maven Dependencies are removed from Deployment Assembly and need to readded when Maven update project is run.
    * On your local system `BSIS/build.properties` will get modified depending on where Apache Tomcat is installed. May be making build.properties an untracked file would be a good idea.
2. You will also need to setup a MySQL database and specify this configuration in the following files:
    * `BSIS/src/database.properties` should contain your database connection information. Please note this should be an untracked file.
    * `BSIS/war/WEB-INF/classes/META-INF/persistence.xml` should contain your database connection information
    * `BSIS/war/BSIS.properties` Should contain the path of your MySQL binaries and database connection information, used to create backup programmatically from inside the webapp
    * You may use the script `BSIS/resetDatabase.sh` to setup your database intially. Note this script will work only a linux system. If you are developing on Windows then you may have to write a new batch script. The file `ddl_mysql.sql` is autogenerated using the class `BSIS/src/datagenerator/SchemaGenerator`.

Important Coding Conventions
----------------------------
  * Use spaces instead of tabs for formatting. You may configure your IDE to use spaces instead of tabs for all files Java, XML, HTML, JS.
    To setup Eclipse to do this for you by default you may read [this] (http://stackoverflow.com/questions/3460994/inserting-spaces-instead-of-tabs-in-all-files).
    This will ensure uniform rendering of files across editors.
  * Use 2 spaces to indent your code. Java code tends to have long variable names and hence it is easier to read code with smaller indentation.
