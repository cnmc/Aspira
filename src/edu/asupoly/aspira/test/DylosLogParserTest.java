package edu.asupoly.aspira.test;

import java.util.List;
import edu.asupoly.aspira.dmp.devicelogs.DylosLogParser;
import edu.asupoly.aspira.model.ParticleReading;

public final class DylosLogParserTest {
    public static void main(String[] args) {
	if (args.length ==0) {
	    System.out.println("USAGE: java edu.asupoly.aspira.test.DylosLogParserTest file {file, ...]");
	    System.exit(0);
	}
	for (int i = 0; i< args.length; i++) {
	    System.out.println("************ Processing logfile " + args[i]);
	    try {
		List<ParticleReading> readings = DylosLogParser.parseLog(args[i]);
		System.out.println(readings.size() + " particle readings found");
		for (ParticleReading pr : readings) {
		    System.out.println(pr.toString());
		}
	    } catch (Throwable t) {
		t.printStackTrace();
		System.out.println("Skipping rest of file " + args[i]);
	    }
	}
    }
}
