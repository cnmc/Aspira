/**
 * 
 */
package edu.asupoly.aspira.model;

/**
 * @author kevinagary
 *
 */
@SuppressWarnings("serial")
public class AspiraModelException extends Exception {

    /**
     * 
     */
    public AspiraModelException() {
        super();
    }

    /**
     * @param arg0
     */
    public AspiraModelException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     */
    public AspiraModelException(Throwable arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public AspiraModelException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
