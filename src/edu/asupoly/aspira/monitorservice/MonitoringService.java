/**
 * 
 */
package edu.asupoly.aspira.monitorservice;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.HashMap;

/**
 * @author kevinagary
 *
 */
public final class MonitoringService {
    private static final int MAX_TIMER_TASKS = 100;
    private static final int DEFAULT_INTERVAL = 60;  // in seconds
    private Timer __timer;
    private HashMap<String, TimerTask> __tasks;
    
    /**
     * 
     */
    public MonitoringService(Properties props) {
        __tasks = new HashMap<String, TimerTask>();
        
        // for each TimerTask indicated in the property, schedule it
        int i = 1;
        int interval = DEFAULT_INTERVAL; // all intervals in seconds
        String intervalProp = null;
        String taskClassName = props.getProperty("MonitorTask"+i);
        while (i < MAX_TIMER_TASKS && taskClassName != null) {
            try {
                intervalProp = props.getProperty("TaskScheduleInterval"+i);
                if (intervalProp != null) {
                    try {
                        interval = Integer.parseInt(intervalProp);
                    } catch (NumberFormatException nfe) {
                        interval = DEFAULT_INTERVAL;
                        // XXX log the incorrect interval
                    }
                } else { // no interval specified, use default
                    interval = DEFAULT_INTERVAL;
                }
                // let's create a TimerTask of that class and start it
                Class<?> taskClass = Class.forName(taskClassName);
                AspiraTimerTask nextTask = (AspiraTimerTask)taskClass.newInstance();
                if (nextTask.init(props)) {
                    __tasks.put("MonitorService"+i, nextTask);
                    // fire up the task
                    __timer.schedule(nextTask, 0, interval*1000); // repeat task in seconds
                } else {
                    // XXX need to log that we were not able to init the task and so could not start it
                }
            } catch (Throwable t) {
                // something prevented us from creating the task, skip it
                // XXX need to log it here
            }
            i++;
            taskClassName = props.getProperty("MonitorTask"+i);
        }
    }

}
