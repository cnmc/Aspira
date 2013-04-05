package edu.asupoly.aspira.test;

import java.util.Properties;

import edu.asupoly.aspira.monitorservice.MonitoringService;

public final class MonitoringServiceTest {

    private MonitoringServiceTest() {
    }

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("USAGE: java edu.asupoly.aspira.monitorservice.MonitoringServiceTest <device id> <patient id> <AQ logfile> <delay>");
        }
        Properties p = new Properties();
        p.setProperty("deviceid", args[0]);   // e.g. "device_one"
        p.setProperty("patientid", args[1]);  // e.g. "patient_one"
        p.setProperty("aqlogfile", args[2]);  // e.g. "devicelogs/DylosLog.txt");
        try {
            System.out.println("Starting the monitoring service");
            MonitoringService theService = MonitoringService.getMonitoringService();
            System.out.println("Started the monitoring service");
            Thread.sleep(Integer.parseInt(args[3]));
            System.out.println("Waited " + args[3] + " seconds");
            theService.shutdownService();
        } catch (Throwable t2) {
            t2.printStackTrace();
        }
    }
}
