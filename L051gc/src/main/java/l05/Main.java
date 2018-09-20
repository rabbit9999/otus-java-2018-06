package l05;

public class Main {
    public static void main(String ... args){
        /*
        -agentlib:jdwp=transport=dt_socket,address=14000,server=y,suspend=n
        -Xms512m
        -Xmx512m
        -XX:MaxMetaspaceSize=256m
        -verbose:gc
        -Dcom.sun.management.jmxremote.port=15000
        -Dcom.sun.management.jmxremote.authenticate=false
        -Dcom.sun.management.jmxremote.ssl=false
        -XX:+HeapDumpOnOutOfMemoryError
        -XX:-UseParallelGC
         */
        MyGCMonitor.installGCMonitoring();
        Stand stand = new Stand();
        stand.start();
    }
}
