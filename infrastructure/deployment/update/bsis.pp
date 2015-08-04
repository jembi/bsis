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
$mysql_bsis_database_name = "bsis"
$git_branch = "develop"

# defaults for Exec
Exec {
	path => ["/bin", "/sbin", "/usr/bin", "/usr/sbin", "/usr/local/bin", "/usr/local/sbin"],
	user => "root",
}

# Remove database
#exec { "clean-database":
#	command => "echo drop database $mysql_bsis_database_name | mysql -uroot -p$mysql_root_password",
#	timeout	=>	3600,
#	returns => [0, 1],
#}

# Create bsis demo database
exec { "create-database":
	command => "echo create database if not exists $mysql_bsis_database_name | mysql -uroot -p$mysql_root_password",
	returns => [0, 1],
#	require	=> Exec["clean-database"],
}

# Build using maven
exec { "mvn-build":
	cwd => "/git/bsis",
	command => "mvn clean install",
	timeout	=>	3600,
	returns => [0, 1],
}

# Deploy to tomcat
exec { "deploy":
	command => "sudo cp /git/bsis/target/bsis.war /var/lib/tomcat6/webapps/",
	returns => [0, 1],
	require	=> Exec["mvn-build"],
}

#Restart tomcat
exec { "restart-tomcat":
	command => "sudo /etc/init.d/tomcat6 restart",
	returns => [0, 1],
	require	=> Exec["deploy"],
}
