/**
 * 
 */
package edu.asupoly.aspira.dmp.devicelogs;

/**
 * @author kevinagary
 *
 */
@SuppressWarnings("serial")
public class DeviceLogException extends Exception {

    /**
     * 
     */
    public DeviceLogException() {
        super();
    }

    /**
     * @param arg0
     */
    public DeviceLogException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     */
    public DeviceLogException(Throwable arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public DeviceLogException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
