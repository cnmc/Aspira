package edu.asupoly.aspira;

public class AspiraException extends Exception {

    private static final long serialVersionUID = 3948497336833442128L;

    public AspiraException() {
        super();
    }

    public AspiraException(String arg0) {
        super(arg0);

    }

    public AspiraException(Throwable arg0) {
        super(arg0);
    }

    public AspiraException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
