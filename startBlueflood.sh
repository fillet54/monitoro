
java -Dblueflood.config=file:///`(pwd)`/conf/blueflood.conf \
        -Dlog4j.configuration=file:///`(pwd)`/conf/blueflood-log4j.properties \
        -Xms1G \
        -Xmx1G \
        -Dcom.sun.management.jmxremote.authenticate=false \
        -Dcom.sun.management.jmxremote.ssl=false \
        -Djava.rmi.server.hostname=localhost \
        -Dcom.sun.management.jmxremote.port=9180 \
        -classpath lib/blueflood/blueflood-all/target/blueflood-all-2.0.0-SNAPSHOT-jar-with-dependencies.jar com.rackspacecloud.blueflood.service.BluefloodServiceStarter 


