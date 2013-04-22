package edu.asupoly.aspira.test;

import edu.asupoly.aspira.monitorservice.MonitoringService;

public final class MonitoringServiceTest {

    private MonitoringServiceTest() {
    }

    public static void main(String[] args) {
        try {
            //Class.forName("edu.asupoly.aspira.GlobalHelper");
            System.out.println("Starting the monitoring service");
            MonitoringService theService = MonitoringService.getMonitoringService();
            System.out.println("Started the monitoring service");
            char c = 'a';
            do {
                long t1 = System.currentTimeMillis();
                c = (char) System.in.read();
                long t2 = System.currentTimeMillis();
                System.out.println("Seconds elapsed: " + (t2-t1)/1000L);
            } while (c != 'q');
            System.out.println("Shutting down the monitoring service");
            theService.shutdownService();
            System.out.println("Shut down the monitoring service");
            System.exit(0);
        } catch (Throwable t2) {
            t2.printStackTrace();
            System.exit(-1);
        } finally {
            System.out.println("Need to exit gracefully!");
        }
    }
}
