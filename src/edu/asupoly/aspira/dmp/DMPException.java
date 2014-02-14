package edu.asupoly.aspira.dmp;

@SuppressWarnings("serial")
public class DMPException extends Exception {
    public DMPException() {
    }

    public DMPException(String arg0) {
        super(arg0);
    }

    public DMPException(Throwable arg0) {
        super(arg0);
    }

    public DMPException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
