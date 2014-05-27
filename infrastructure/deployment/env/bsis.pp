# Puppet manifest
#
# Required modules:
# puppetlabs/mysql
# puppetlabs/java
# example42/tomcat
# maestrodev/maven
#

# Variables
# MySQL info should match the values in src/database.properties
$mysql_root_password = "root"
$mysql_bsis_user = "bsis"
$mysql_bsis_user_password = "bsis"
$mysql_bsis_database_name = "v2v_new"

# defaults for Exec
Exec {
	path => ["/bin", "/sbin", "/usr/bin", "/usr/sbin", "/usr/local/bin", "/usr/local/sbin"],
	user => "root",
}

# Make sure package index is updated (when referenced by require)
#exec { "apt-get update":
#    command => "apt-get update",
#    user => "root",
#}

# Install required packages
Package { ensure => "installed" }
#package { "git-core": ensure => latest }
package { "git":}


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
	version => "3.0.3", # version to install
}

# Remove previous deploys
exec { "clean-database":
	command => "echo drop database $mysql_bsis_database_name | mysql -uroot -p$mysql_root_password",
	returns => [0, 1],
	require	=> Class["mysql::server"]
}

# Create bsis demo database
mysql::db { $mysql_bsis_database_name:
	user 			=> $mysql_bsis_user,
	password		=> $mysql_bsis_user_password,
	host			=> "localhost",
	grant			=> ["all"],
	require  		=> Exec["clean-database"]
}

#Create directory to store codebase
file {
"/var/jembi":
	ensure => "directory",
	owner => "vagrant";
}

#Clone git repository
exec { "clone-repo":
    command => "git clone http://github.com/jembi/bsis.git",
    cwd => "/var/jembi",
    unless => "test -d /var/bsis/bsis/.git",
    subscribe => [
        Package['git-core'],
        File['/var/jembi']
    ],
}

#Change to develop branch
exec { "checkout-develop":
    command => "git checkout develop",
    cwd => "/var/jembi/bsis",
    unless => "test -d /var/bsis/bsis/.git",
}

#
exec { "Vumi setup":
    command => "python setup.py develop",
    cwd => "/var/praekelt/vumi/",
    user => "root",
    subscribe => [
        Exec['Clone git repository']
    ],
    refreshonly => true
}


# Build using maven
exec { "mvn-build":
	cwd => "var/jembi/bsis",
	command => "mvn clean install",
	returns => [0, 1],
	require	=> Class["maven::maven"]
}

# Deploy to tomcat
exec { "deploy":
	cwd => "var/jembi/bsis",
	command => "sudo cp target/bsis.war /var/lib/tomcat7/webapps/",
	returns => [0, 1],
	require	=> Exec["mvn-build"]
}

