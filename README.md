
# ch-058, geocitizen

___build and deploy (ubuntu16, git2, maven3, tomcat9)___

1) `git clone https://github.com/nromanen/Ch-058.git; cd Ch-058`
1) in config file [`~/Ch-058/src/main/resources/application.properties`](https://git.io/vA4Sw)
	you might want to edit following properties
	 * [`front.url`](https://git.io/vARyB) - front url
	 * [`db.url`](https://git.io/vARyu) - db uri (__db must be created manually__)
	 * [`db.username`](https://git.io/vARyo) & [`db.password`](https://git.io/vARyK) - db credentials
1) `mvn install && mv target/citizen.war /usr/share/tomcat9/webapps/ && /usr/share/tomcat9/bin/startup.sh`
1) e.g. <http://localhost:8080/citizen/>

# 

if you want to make changes to frontend 
you have to cd to `~/Ch-058/front-end` dir and run `npm run dev` after successful execution you'll see url.
to generate the production build you have to
 - replace url with tomcat's url (e.g. `'http://localhost:8080/citizen'`) in [`~/Ch-058/front-end/src/main.js`](git.io/vA49U)
 - run `npm run build`, move all files from `~/Ch-058/front-end/dist` to `~/Ch-058/src/main/webapp`
 - in [`~/Ch-058/src/main/webapp/index.html`](https://git.io/vAR9l) put dots (ha-ha) on lines
    * after [`<link href=`](https://git.io/vARrw) 
    * after [`<script type=text/javascript src=`](https://git.io/vARr5)          
- then repeat 3rd step of `build and deploy`  

# 
    
[swagger](http://localhost:8080/citizen/swagger-ui.html)

[heroku](https://geocitizen.herokuapp.com)  
  
# Geocitizen

# geocit


## Setup

### 1. Environment Variables

First, create a `.env` file in the root directory of your project of your user and add the following environment variables:
```
# Postgres DB
db.url 			= jdbc:postgresql://localhost:5432/ss_demo_1
db.username 	= postgres
db.password 	= postgres
# Postgres liquibase
url 			= jdbc:postgresql://localhost:5432/ss_demo_1
username 		= postgres
password 		= postgres
# SMTP
email.username 	=
email.password 	=
# Google Maps
map.key 		=
```

### 2. Startup Script

Use the `startup.sh` script to build and start the application. This script performs the following tasks:

- Install OpenJDK 8 and Maven
- Install Postgres and configure it (if needed)
- Install Node 14 (if needed)
- Install Tomcat
- Clone the application repository
- Get the IP address of the VM and replace `localhost` with it in the configuration files
- Substitute environment variables in `application.properties`
- Build the application

To run the script, use the following command:
`bash startup.sh`

### Important Note
If you already have a Postgres database created, comment out all lines in the script under the section `# Install Postgres and configure (if needed)`. Additionally, ensure the .env file is updated with the proper values for your existing database.

### Usage
Once the setup is complete, the application will be up and running on your local host. You can access it using the IP address of your VM.
