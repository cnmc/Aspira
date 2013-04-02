/**
 * 
 */
package edu.asupoly.aspira.monitorservice;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.HashMap;

import edu.asupoly.aspira.dmp.DMPException;

/**
 * @author kevinagary
 *
 */
public final class MonitoringService {
    private static final int DEFAULT_MAX_TIMER_TASKS = 10;
    private static final int DEFAULT_INTERVAL = 60;  // in seconds
    private static final String PROPERTY_FILENAME = "monitoringservice.properties";
    private static final String MAX_TIMERTASKS_KEY = "MaxTimerTasks";
    private static final String DEFAULT_INTERVAL_KEY = "DefaultTaskInterval";
    private static final String TASK_KEY_PREFIX = "MonitorTask";
    private static final String TASKINTERVAL_KEY_PREFIX = "TaskInterval";
    
    private Timer __timer;
    private HashMap<String, TimerTask> __tasks;
    private Properties __props;

    private static MonitoringService __theMonitoringService = null;

    public static MonitoringService getMonitoringService() {
        if (__theMonitoringService == null) {
            try {
                __theMonitoringService = new MonitoringService();
            } catch (Throwable t) {
                // XXX log here
                __theMonitoringService = null;
            }
        }
        return __theMonitoringService;
    }
    
    /**
     * 
     */
    private MonitoringService() throws DMPException {
        __tasks = new HashMap<String, TimerTask>();
        int defaultInterval = DEFAULT_INTERVAL; // all intervals in seconds
        int maxTimerTasks   = DEFAULT_MAX_TIMER_TASKS;
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(PROPERTY_FILENAME));
            __props.load(isr);
            defaultInterval = Integer.parseInt(__props.getProperty(DEFAULT_INTERVAL_KEY));
            maxTimerTasks = Integer.parseInt(__props.getProperty(MAX_TIMERTASKS_KEY));
        } catch (NumberFormatException nfe) {
            // XXX log a bad prop format but use defaults and continue
            defaultInterval = DEFAULT_INTERVAL;
            maxTimerTasks   = DEFAULT_MAX_TIMER_TASKS;
        } catch (NullPointerException npe) {
            // XXX log an unassigned required prop but use defaults and continue
            defaultInterval = DEFAULT_INTERVAL;
            maxTimerTasks   = DEFAULT_MAX_TIMER_TASKS;
        }
        catch (IOException ie) {
            // XXX log a problem with reading the properties file as a whole, gotta bail
            throw new DMPException(ie);
        }
        catch (Throwable t1) {
            // XXX log something else happened
            throw new DMPException(t1);
        }

        // for each TimerTask indicated in the property, schedule it
        int i = 1;
        int interval = defaultInterval;
        String intervalProp = null;
        String taskClassName = __props.getProperty(TASK_KEY_PREFIX+i);
        while (i <= maxTimerTasks && taskClassName != null) {
            try {
                intervalProp = __props.getProperty(TASKINTERVAL_KEY_PREFIX+i);
                if (intervalProp != null) {
                    try {
                        interval = Integer.parseInt(intervalProp);
                    } catch (NumberFormatException nfe) {
                        interval = defaultInterval;
                        // XXX log the incorrect interval
                    }
                } else { // no interval specified, use default
                    interval = defaultInterval;
                }
                // let's create a TimerTask of that class and start it
                Class<?> taskClass = Class.forName(taskClassName);
                AspiraTimerTask nextTask = (AspiraTimerTask)taskClass.newInstance();
                if (nextTask.init(__props)) {
                    __tasks.put(TASK_KEY_PREFIX+i, nextTask);
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
            taskClassName = __props.getProperty(TASK_KEY_PREFIX+i);
        }
    }


}
