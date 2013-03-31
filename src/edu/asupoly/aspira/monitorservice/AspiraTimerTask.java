/**
 * 
 */
package edu.asupoly.aspira.monitorservice;

import java.util.Properties;
import java.util.TimerTask;

/**
 * @author kevinagary
 *
 */
public abstract class AspiraTimerTask extends TimerTask {

    protected boolean _isInitialized;
    
    /**
     * 
     */
    protected AspiraTimerTask() {
        super();
        _isInitialized = false;
    }

    // Init should read whatever properties it expects in the subclass
    public abstract boolean init(Properties props);
    
    /* (non-Javadoc)
     * @see java.util.TimerTask#run()
     */
    @Override
    public void run() {
        return;
    }

}
