package edu.asupoly.aspira.test;

import java.io.FileNotFoundException;
import javax.xml.transform.TransformerConfigurationException;
import edu.asupoly.aspira.dmp.devicelogs.SpirometerXMLLogParser;

public class SpirometerXMLLogParserTest {

    private SpirometerXMLLogParserTest() {
    }
   public static void main (String args[]) throws FileNotFoundException, TransformerConfigurationException {
        
        if(args.length < 1)
        {
            System.out.println("No log file passed as argument passed");
            return;
        }
        SpirometerXMLLogParser _parser = new SpirometerXMLLogParser();
        _parser.parseLog(args[0]);
    }
}
