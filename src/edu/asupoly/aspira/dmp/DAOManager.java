/**
 * 
 */
package edu.asupoly.aspira.dmp;

/**
 * @author kevinagary
 *
 */
public class DAOManager {

    private static AspiraDAO __dao = null;
            
    /**
     * 
     */
    private DAOManager() {
        // need to read a prop file that describes what DAOImpl we are using and its params
        __dao = null;  // XXX 
    }

    public static DAOManager getDAOManager() throws DMPException {
        // do we need to hand out a ref to the Singleton?
        return null;
    }
    
    public static AspiraDAO getDAO() throws DMPException {
        if (__dao == null) {
            __dao = null; // XXX
        }
        return __dao;
    }
}
