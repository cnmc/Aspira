package edu.asupoly.aspira;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class GlobalHelper {
    public static String stackToString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString(); // stack trace as a string
    }
    
    private GlobalHelper() {}
}
