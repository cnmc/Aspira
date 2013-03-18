/*
 * ASPIRA Project
 * This program does parsing
 * of Spirometer log files
 */
package edu.asupoly.aspira.dmp.devicelogs;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import edu.asupoly.aspira.model.SpirometerReading;

/*
 * KGDJ: We still need to parse the Patient element
 */
public class SpirometerXMLLogParser
{
    List<SpirometerReading> _readings= new ArrayList<SpirometerReading>();
    public boolean parseLog(String filename)
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
            Logger.getLogger(SpirometerXMLLogParser.class.getName()).log(Level.SEVERE, "Successfully Completed Parsing");
            populateReadings(doc);
            //Logger.getLogger(SpirometerXMLLogParser.class.getName()).log(Level.SEVERE, "Printing Patient Reading -");
            //printReadings();
            return true;
        }
        catch (Throwable t) {
            Logger.getLogger(SpirometerXMLLogParser.class.getName()).log(Level.SEVERE, null, t);
        }
        return false;
    }
    
    private void populateReadings(Document doc) throws DeviceLogException
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
    
    @SuppressWarnings("unused")
    private void printReadings()
    {
        System.out.println("ID\tMeasureId\tMeasureDate\t\t\tPEFValue\tFEV1Value");
        for(SpirometerReading pr:_readings)
        {
            System.out.println(pr.getPid()+"\t" + pr.getMeasureID()+"\t\t"+pr.getMeasureDateTime()+"\t"+pr.getPEFValue()+"\t\t"+pr.getFEV1Value());
        }
    }
    
    private String getFirstChildNodeValue(Node node, String name)
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

}
