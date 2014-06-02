# Puppet manifest
#
# Required modules:
# puppetlabs/mysql
# puppetlabs/java
# example42/tomcat
# maestrodev/maven
#

# Variables
$mysql_root_password = "root"
$mysql_bsis_user = "bsis"
$mysql_bsis_user_password = "bsis"
$mysql_bsis_database_name = "v2v_new"

# defaults for Exec
Exec {
	path => ["/bin", "/sbin", "/usr/bin", "/usr/sbin", "/usr/local/bin", "/usr/local/sbin"],
	user => "root",
}

# Make sure package index is updated
exec { "apt-get update":
    command => "apt-get update",
    user => "root",
}

# Install required packages
Package { ensure => "installed" }
package { "git": }

#Install MySQL server
class { 'mysql::server':
	root_password => $mysql_root_password
}

#Install Java
class { "java": }

#Install tomcat
class { "tomcat": }

# Install Maven
class { "maven::maven":
	version => "3.0.3", 
}

