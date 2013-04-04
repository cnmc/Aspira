package edu.asupoly.aspira.test;

import java.util.Properties;

import edu.asupoly.aspira.monitorservice.AirQualityMonitorTask;
import edu.asupoly.aspira.monitorservice.MonitoringService;

public final class MonitoringServiceTest {

    private MonitoringServiceTest() {
    }

    public static void main(String[] args) {
        java.util.Timer t = new java.util.Timer();
        Properties p = new Properties();
        p.setProperty("deviceid", "device_one");
        p.setProperty("patientid", "patient_one");
        p.setProperty("aqlogfile", "devicelogs/DylosLog.txt");
        try {
            System.out.println("Starting the monitoring service");
            MonitoringService theService = MonitoringService.getMonitoringService();
            /*
            AirQualityMonitorTask task = new AirQualityMonitorTask();
            if (task.init(p)) {
                System.out.println("Scheduling task");
                t.scheduleAtFixedRate(task, 1000L, 2000L);
            }
            */
            System.out.println("Started the monitoring service");
            //Thread.sleep(5000);
            System.out.println("Waited 5 seconds");
            Thread.sleep(90000);
            System.out.println("Waited 9 seconds");
        } catch (Throwable t2) {
            t2.printStackTrace();
        }
    }
}
