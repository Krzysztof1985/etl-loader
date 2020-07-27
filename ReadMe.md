### I Introduction

Example ETL (Extract, Transform, Load) project which is using
MongoDB and aggregation framework to calculate variety statistics.


### II Basic usage

Due to some time limitations, below are some assumptions:
* uploaded CSV file must contains unique entries
* database will not allow a save duplicated (indexes) entries.
* sometimes spring-data-mongodb libraries (spring-boot dependencies + other libraries) are not working well (indexes generation using annotations) 
the alternative settings has been made on ***```mongoTemplate```*** level


There are some additional ***```converters```*** because MongoDB has some limitations especially with **Long**, **Integer** types,
but the most painful limit is in case ***ZonedDateTime*** and time calculations.


User might via ```swagger-ui``` performs variety calculations.
Available on [applicationURL:8080/swagger-ui.html](applicationURL:8080/swagger-ui.html).
For example : ```localhost:8080/swagger-ui.html```

First of all please delete old data, and then upload fresh one.

Based on fresh one input, you should be able to execute some filters.


#### III Performing calculations
To perform some calculation user must provide as a request body valid filter.
Filter contains multiple fields such as:
* date ranges
* metrics which should be displayed
* grouping dimension
* data sources

Complete request might looks like follows:
```json
{
 "from": "01/01/19",
  "to": "12/30/20",
  "campaigns": [
    "Carfinder",
    "PLA"
  ],
  "metrics": {
    "clicks": true,
    "impressions": true
  },
  "dataSource": [
    "Google Ads",
    "Twitter Ads"
  ],
  "groupDimension": "dataSource"
}
```
##### Date Range filters

All request to perform calculations will be described part by part

```json
"from" : 11/23/20,
"to" :  12/31/20
```
All dates must match to pattern dd/dd/dd (digit), where this pattern
is interpreted as **dd/MM/yy**

On backend side all dates are saved as a ZonedDateTime with UTC ZoneId.

##### Campaigns

In this filter we must provide for which campaigns we would like to calculate statistics.
```json
  "campaigns": [
    "Carfinder",
    "PLA"
  ]
```
If user would not calculate some statistics for some campaign, please just not provide it.

##### Metrics
There are 2 types of metrics available for user:
* clicks
* impressions (views, displays)
User must provide a which metrics would like to see based on ***true/false*** properties in the metrics JSON.

##### Data Sources
Data sources are information from whom given data come from.
User might specify from which provider/source would like to use, to perform calculations.

##### Grouping Dimensions

Grouping Dimension is a property which can take only 2 possible inputs:
* dataSource (case sensitive)
* campaign

This is a "key" property to group aggregated data.

Based on valid proper request user is able to perform multiple calculations.

#### IV LOCAL DEVELOPMENT AND SETUP

##### 1. Install specific MongoDB version
For development purposes we suggest to use Linux OS, especially to further work with docker, and bash scripts.

It's recommended to have all mongo packages in a version at least 4.0.0
To do this on Linux machines please execute following command:
```bash
 sudo apt-get install -y mongodb-org=4.2.0 mongodb-org-server=4.2.0 mongodb-org-shell=4.2.0 mongodb-org-mongos=4.2.0 mongodb-org-tools=4.2.0
```
Sometimes all packages could be not found in given version so please decrease all version until all will be match, but at least in version 4.0.0

##### 2. Install Robo 3T (former Robo Mongo) - UI client for MongoDB

For convenience it's good to install Robo 3T.
You should download the latest install file from official page:
[https://robomongo.org/download](https://robomongo.org/download)


##### 4. Installation Mongo Compass
 
 Another useful tool is Mongo Compass, you may download it from here:
 
 [Mongo Compass](https://www.mongodb.com/download-center/compass)
 
 Please download community edition, only 1 thing what is required to provide some data to download.
 Alternatively this could be downloaded by 1 person and shared via some flash USB disk.

##### 5. SETUP REPLICA SET
 
 To start working with replica set, it's required to do some `manual ` steps.
 First of all please check that ```mongod``` process is not running.
 If necessary please stop/kill this process. Then follow the points which should be executed.
###### 1. Edit file with sudo ***```/etc/mongod.conf```***, add/uncomment line about replica set.
 
 ```
...
replication:
   replSetName: "rs0"
...
 ```
You should provide a name for replica set - in this case **```rs0```**.
Save the file and proceed to the next point.

###### 2. Generate proper folder structure for example:
 
 ```bash
mkdir -p ~/data/db{0,1,2}
```
 
###### 3. Run following statements - each command should be run in separate command line terminal:
 
```
mongod --port 27017 --dbpath ~/data/db0 --replSet rs0
mongod --port 27018 --dbpath ~/data/db1 --replSet rs0
mongod --port 27019 --dbpath ~/data/db2 --replSet rs0
```

###### 4. Initialize ReplicaSet

Please login in another terminal to Mongo using command:
***```mongo```***
Then execute following commands:
```
rs.initiate()
rs.isMaster() 
rs.add( { host: "localhost:27018", priority: 0, votes: 0 } )
rs.add( { host: "localhost:27019", priority: 0, votes: 0 } )
```

To check the Replica set you may execute : **```rs.status()```**.


Alternatively you may execute the script:
```bash
#!/usr/bin/env bash

sudo mkdir -p ~/data/db{0,1,2}

sudo mongod --port 27017 --dbpath ~/data/db0 --replSet rs0 &
sudo mongod --port 27018 --dbpath ~/data/db1 --replSet rs0 &
sudo mongod --port 27019 --dbpath ~/data/db2 --replSet rs0
```

Above snippet might be copied to some file and execute this as a bash script - please make sure that you don't have
Windows system line endings (CRLF) otherwise bash script will be failed during execution.


###### 5. Update application property file with correct url's to mongo instances:
Example content **```application.properties```** file 
```
spring.data.mongodb.uri=mongodb://localhost:27017,localhost:27018,localhost:27019
spring.data.mongodb.database=csv_loader
spring.jackson.deserialization..fail-on-unknown-properties=false
```

That's it! You should be able to start Replica Set in Java application now.
#### IV. DOCKERIZING

##### 1. Dockerize Java application

Dockerization for Java Application is simple, there is only one thing that developer must remember
that required plugins for Spring Boot are enabled to make jar file executable.
Additionally there is worth to mention that following command is required
```ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","server.jar"]```

The security config is required for **docker** to enable overriding and using docker environments
as properties entries in *application.properties*

To build docker image you should execute example following command :

```cmd
docker build -t somerepo:500/myrepo/etlloader:2.2.2 .
```

where :
* ```com.aha.portal:5000``` is repository, and port number is required
* ```etlloader``` is image name
* ```1.0.5``` example image tag

then the user should push the image to repository using command:
```cmd
docker push somerepo:500/myrepo/etlloader:2.2.2
```
If docker will require to login first then please execute following command:
```docker login -u USER -p PASS``` where:
* ```USER``` - user (please check ~/m2/settings.xml to get the USER value)
* ```PASS``` - password (the same like above)

If login to docker repo is fine, please execute once again command ```docker push ...``` 


##### 2. Docker compose - multi build for DB's and AppStore image

Whole environment requires to have installed ***docker-compose*** packages.
Then please check the folder **${project.dir}/dockers** which contains all required
components and mounted volumes.

**1. Copy dockers directory**
    
The best way it will be copy whole folder dockers and adjust just one line inside.
The copy is good idea due to complain and errors which might appear due to disk space limit.
Sometimes ReplicaSet complains about this and is unable to start/setup correctly.


**2. Adjust docker image**

After copy operation please open the **```docker-compose.yaml```** file and 
in below section values from **somerepo:500/myrepo/etlloader:XXXX** to required docker image version of AppStore 
image and save it

```yaml
server-portal:
    image: somerepo:500/myrepo/etlloader:XXXX
    container_name: etl-loader
    restart: always
    depends_on:
```

Sometimes if you changed something you might rebuild images using command:
```cmd
docker-compose up --rebuild
```

In ```docker-compose.yaml``` there is a dedicated docker container named **setupReplicaSet**
which must start, connect once together all 3 mongo* dockers into *replicaSet* named **rs0**
Container **setupReplicaSet** will exit, so please don't bother this.

After finished work with ETL-Loader + MongoDB replica set you might delete/stop the whole process,
to do this please execute one of possible options:

* ```docker-compose down``` - this will completely stop & remove all docker containers which were take a part in whole setup
but they will not delete MongoDB mounted db data.

* ```docker-compose stop``` - this will stop but not remove all docker containers which were take a part in whole setup.
If user executes this command then to run again, it must up all containers using command
```docker-compose start```


This will setup everything what you required to dockerize and orchestration everything (ETL-Loader + MongoDB's as ReplicaSet)

##### Hints for docker-compose:

######1) If you would like change the name of the docker containers for mongo replicas in
docker-compose file from:
```yaml
"mongo1", "mongo2", "mongo3"
```
to some other names, then you must update the same names in 3 places:
* environment variables below: **``` spring.data.mongodb.uri: "mongo1:27017,mongo2:27017,mongo3:27017"```**
* in the file **``./replica-master/replicaSet.js``**
* in the file **``./replica-master/setup.sh``**


######2) Please don't change port mappings for replicas, they always must map from docker port 27017 to host unique not busy port.
So it's always looks the same, for example:

```yaml
//mongo1
    ports:
      - 27017:27017
//mongo2
    ports:
      - 27018:27017
    ports:
//mongo3
      - 27019:27017
```

### HINT
Please be careful using ***```docker-compose```*** it creates a virtual network that containers are able to see each other.
This might fail or working incorrect when you are using some VPN.
In such case please disconnect the VPN, then start ```docker-compose up``` and connect to the VPN.
This is due to changes in IP table entries performed by the Docker.

ENJOY!
