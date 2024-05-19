#!/bin/bash

# Tested in Ubuntu 22.04

# Update package list and install necessary packages
sudo apt update
sudo apt install git curl gnupg2 wget -y

# Install OpenJDK 8 and Maven
sudo apt-get install openjdk-8-jdk -y
wget https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz -P /tmp
sudo tar xzvf /tmp/apache-maven-3.9.6-bin.tar.gz -C /opt

# Add Maven environment variables to profile
sudo tee -a ~/.profile > /dev/null <<EOF

# Maven environment variables

export M2_HOME='/opt/apache-maven-3.9.6'
export PATH="\${M2_HOME}/bin:\${PATH}"
EOF

# Reload profile to apply Maven environment variables
source ~/.profile

# Install Node 14 (if needed)

#curl -s https://deb.nodesource.com/setup_14.x | sudo bash
#sudo apt install nodejs -y

# Install  TomCat
sudo mkdir /opt/tomcat/
sudo groupadd tomcat
sudo useradd -s /bin/false -g tomcat -d /opt/tomcat tomcat

# Download and extract Tomcat
curl -O https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.89/bin/apache-tomcat-9.0.89.tar.gz
sudo tar xzvf apache-tomcat-9.0.89.tar.gz -C /opt/tomcat/ --strip-component=1
rm -rf apache-tomcat-9.0.89.tar.gz

# Set Tomcat ownership and permissions
sudo chown -RH tomcat: /opt/tomcat/
sudo sh -c 'chmod +x /opt/tomcat/bin/*.sh'

# Create systemd service file for Tomcat
sudo tee /etc/systemd/system/tomcat.service > /dev/null <<EOF
[Unit]
Description=Apache Tomcat
After=network.target

[Service]
Type=forking
User=tomcat
Group=tomcat
Environment="JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64"
Environment="JAVA_OPTS=-Djava.security.egd=file:///dev/urandom -Djava.awt.headless=true"
Environment="CATALINA_BASE=/opt/tomcat"
Environment="CATALINA_HOME=/opt/tomcat"
Environment="CATALINA_PID=/opt/tomcat/temp/tomcat.pid"
Environment="CATALINA_OPTS=-Xms512M -Xmx1024M -server -XX:+UseParallelGC"
ExecStart=/opt/tomcat/bin/startup.sh
ExecStop=/opt/tomcat/bin/shutdown.sh

[Install]
WantedBy=multi-user.target
EOF

# Reload systemd daemon and start Tomcat service
sudo systemctl daemon-reload
sudo systemctl start tomcat


############### App preparation ######################

# Clone the application repository
git clone https://github.com/CapybaraDevOps/geocit.git

# Create .env file in script
#cat << EOF > ~/.env

#EOF

# Front-end (uncomment if need to buld frontend)
#cd ~/geocit/front-end
#
#npm install
#npm run dev

# Get the IP address of the VM and replace localhost with it in configuration files
app_prop=~/geocit/src/main/resources/application.properties
app_js=~/geocit/src/main/webapp/static/js/app.*.js
ip=$(ip a | grep -oE "\b192\.168\.([0-9]{1,3})\.([0-9]{1,3})\b" | head -n 1)
ip_db=$(echo "$ip" | awk -F'.' '{print $1"."$2"."$3"."$4+1}')
echo $ip
echo $ip_db

sed -i "s/localhost:8080/$ip:8080/g" $app_prop
sed -i "s/localhost:5432/$ip_db:5432/g" $app_prop
sed -i "s/localhost/$ip/g" $app_js

# Substitute environment variables in application.properties
while IFS='=' read -r key value; do
        key=$(echo "$key" | xargs)
        value=$(echo "$value" | xargs)

        sed -i "s|^\($key *= *\).*|\1$value|" "$app_prop"
done < ~/.env

rm -rf ~/.env

# Build the application
cd ~/geocit
mvn clean install
sudo mv target/citizen.war /opt/tomcat/webapps/

# Restart Tomcat service to deploy the application
sudo systemctl restart tomcat