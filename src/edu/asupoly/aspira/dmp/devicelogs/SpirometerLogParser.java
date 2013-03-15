/*
 * ASPIRA Project
 * This program does parsing
 * of Spirometer log files
 */
package edu.asupoly.aspira.dmp.devicelogs;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import edu.asupoly.aspira.model.SpirometerReading;

public class SpirometerLogParser
{
    List<SpirometerReading> _readings= new ArrayList<SpirometerReading>();
    void parseLog(String filename)
    {
        try
        {
            InputSource source = new InputSource(new FileReader(filename));
            Document doc = null;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(source);
            System.out.println("Successfully Completed Parsing");
            populateReadings(doc);
            System.out.println("Printing Patient Reading -");
            printReadings();
        }
        catch (ParserConfigurationException ex) {
            Logger.getLogger(SpirometerLogParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(SpirometerLogParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SpirometerLogParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void populateReadings(Document doc)
    {
        NodeList nodeList = doc.getElementsByTagName("MeasureRec");
        int len = nodeList.getLength();
        for (int i = 0; i < len; i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String id = getFirstChildNodeValue(node, "ID");
                String mid = getFirstChildNodeValue(node, "MeasureID");
                String dt = getFirstChildNodeValue(node, "MeasureDate");
                String pef = getFirstChildNodeValue(node, "PEFValue");
                String fev = getFirstChildNodeValue(node, "FEV1Value");
                String err = getFirstChildNodeValue(node, "Error");
                String bvalue = getFirstChildNodeValue(node, "BestValue");
                SpirometerReading pr = new SpirometerReading(id, mid, dt, pef, fev, err, bvalue);
                _readings.add(pr);
            }
        }
    }
    
    void printReadings()
    {
        System.out.println("ID\tMeasureId\tMeasureDate\t\t\tPEFValue\tFEV1Value");
        for(SpirometerReading pr:_readings)
        {
            System.out.println(pr.getPid()+"\t" + pr.getMeasureID()+"\t\t"+pr.getMeasureDateTime()+"\t"+pr.getPEFValue()+"\t\t"+pr.getFEV1Value());
        }
    }
    
    String getFirstChildNodeValue(Node node, String name)
    {
        Element e = (Element)node;
        NodeList list = e.getElementsByTagName(name);
        String st =null;
        if(list.item(0).hasChildNodes())
        {
            st = list.item(0).getFirstChild().getNodeValue();
        }
        return st;
    }
    public static void main (String args[]) throws FileNotFoundException, TransformerConfigurationException {
        
        if(args.length < 1)
        {
            System.out.println("No log file passed as argument passed");
            return;
        }
        SpirometerLogParser _parser = new SpirometerLogParser();
        _parser.parseLog(args[0]);
    }
}
