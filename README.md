V2V
===

C4G Vein-to-Vein (V2V) is a system to monitor and analyze data from blood collection, testing, and utilization.

Vein-to-Vein (V2V) is a project started at Georgia Institute of Technology as part of the [Computing For Good (C4G)](http://www.cc.gatech.edu/about/advancing/c4g/) Program at Georgia Tech.

# Background
V2V is a software to manage blood related information from the point of donation through testing, storage to its eventual usage in a hospital or a clinic. Handling of blood is a sensitive operation and record of movement of blood as it goes through the following chain is critical to the effective functioning of blood processing centers. This solution is primary targeted for deployment in the developing countries of Africa where much of the blood inventory tracking is done on paper.

![Lifecycle of Donated Blood] (https://raw.github.com/C4G/V2V/master/lifecycle_blood.png)

The software is being developed at Georgia Tech as part of the C4G program in collaboration with Centers for Disease Control and Prevention (CDC), Safe Blood for Africa (SBFA) and the participating countries of Africa.

# [Video Introduction to V2V] (http://www.youtube.com/watch?v=O_zIIXepPHc)

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
The challenges listed above have influenced the design of V2V and the following design features have resulted.
 * Develop a browser based but locally deployable solution to avoid dependence on Internet
 * Smaller upgrade package size to allow 
 * Make features configurable so that blood processing centers can turn them on/off based on their current practice
 * A single installer which installs and configures all dependencies like Java, MySQL, Apache Tomcat etc. to make first time configuration simple.

Many other design decisions in the future will be influenced by the same parameters.


Development Environment
-----------------------
1. This repository contains some Eclipse specific configuration. If you are comfortable using Eclipse then setting up your development environment will be relatively easier.
   You may use another IDE if you wish to.
   a. Download and unpack Eclipse Indigo 3.7 or Eclipse Juno 4.2 from http://www.eclipse.org/downloads/.
   b. Install Apache Tomcat 7 and integrate with Eclipse (you may follow http://www.coreservlets.com/Apache-Tomcat-Tutorial/tomcat-7-with-eclipse.html).
   c. Fork and clone the master branch into a local directory preferrably housed in your eclipse workspace.
   d. The project dependencies are configured using Maven. Install Eclipse plugin m2eclipse for easier integration of Maven with your development environment. Setup m2eclipse so that it automatically downloads all the required dependencies, sources, javadocs.
   e. Import 'Existing project' into Eclipse. The master branch contains the required eclipse project files (e.g. .project and .settings), so you should be able to start working right away. For other IDE's you will need to more work.
2. You will also need to setup a MySQL database and specify this configuration in the following files:
   1. V2V/src/database.properties
   2. V2V/war/WEB-INF/classes/META-INF/persistence.xml
   (c) V2V/war/v2v.properties (Should contain the path of your MySQL binaries and database connection information, used to create backup programmatically from inside the webapp)
