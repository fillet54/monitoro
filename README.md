#Super Monitor
##Intro
JMX Monitor which presents data using Cubism.js horizon chart.
 
##Requirements
- Java7
- Cassandra
- Blueflood
- Maven

##Setup
### Folder Structure
The scripts provided in the root directory of this project expect a certain directory layout as follows

- root
	- conf
	- lib
		- blueflood
		- cassandra
	- src
		- .... 

### Cassandra
1. Download and extract Cassandra to a directory of your choice.
2. Make sure that the following environment variables are set.
	- `JAVA_HOME` _(Java7 JRE location)_
	- `CASSANDRA_HOME` _(To the directory you extracted the files to in step 1)_
3. Modify your Cassandra config file `%CASSANDRA_HOME%\cassandra\conf\cassandra.yaml` as you like. I kept the defaults which will setup a storage location at `%CASSANDRA_HOME%\cassandra\data`
4. Open a cmd and run the `%CASSANDRA_HOME%\bin\cassandra.bat` file.
5. (Optional) You can verify that Cassandra is running, by trying to connect to the server.  To do this run the `%CASSANDRA_HOME%\bin\cassandra-cli.bat` file.

### Maven
1. Download and extract maven to directory of choice.
2. Make sure that the following environment variables are set.
	- `JAVA_HOME` _(JRE location)_
	- `%JAVA_HOME%\bin` set in `PATH`
	- `M2_HOME` _(To the directory you extracted the files to in step 1)_
	- `M2` _(%M2\_HOME%\bin)_
	- `%M2%` set in `PATH`

###Blueflood
1. Clone the Blueflood git repository. `git clone git@github.com:rackerlabs/blueflood.git`
2. Make sure that the following environment variables are set.
 	- `JAVA_HOME` _(Java7 JRE location)_
 	- `BLUEFLOOD_HOME` _(To the directory you cloned to in step 1)_
3. (Windows only) As of 9/12/2014 there are a few source modifications that need to be made to get the tests to pass on windows.
	- Edit `%BLUEFLOOD_HOME%\blueflood-kafka\pom.xml` and add an extra `/` at the end of `file://` so that it looks like this `<kafka.config>file:///${basedir}...`
	- Edit `%BLUEFLOOD_HOME%\blueflood-core\src\test\java\com\rackspacecloud\blueflood\service\ConfigurationTest.java` and add an extra `/` at the end of `file://` near line 51.  
4. Create the __Uber jar__ by running `mvn package -P all-modules` in an __Administrator__ cmd window.
5. Create config files and a startup file.
6. With `Cassandra` running create the `DATA` tablespace by opening up a cmd window and executing `%CASSANDRA_HOME%\bin\cassandra-cli.bat -f %BLUEFLOOD_HOME%\src\cassandra\clie\load.script -h 127.0.0.1 -p 9160`
7. Edit `conf/blueflood.conf` if you wish 

###Webbit
This project currently uses the `0.4.16-SNAPSHOT` version of webbit. Since `SNAPSHOTS` are not held in maven central we will need to install it to our local maven repo.

1. Clone the Webbit git repostiory `git clone git@github.com:webbit/webbit.git`
2. `cd webbit`
3. Run `mvn install`


###Super-MQMonitor
1. Run `maven package` to create the __UBER JAR__.

##Running
Must be done in order.

1. Start Cassandra by executing `startCassandra.bat`
2. Start Blueflood by executing `startBlueflood.bat`
3. Start Super-MQMonitor by executing `startMonitor`
4. Open up web browser and point to `http://localhost:8080`

## TODO
1. Add configuration options

