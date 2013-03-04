/*
 * ASPIRA Project
 * This program does parsing
 * of Spirometer log files
 */
package spirometerlogparser;

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

public class SpirometerLogParser
{
    PatientInfo pInfo = new PatientInfo();
    List<Readings> _readings= new ArrayList<Readings>();
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
            buildpatientinfo(doc);
            System.out.println("Printing Patient Info -");
            printPatientInfo();
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
    
    void buildpatientinfo(Document doc)
    {
        NodeList nodeList = doc.getElementsByTagName("Patient");
        int len = nodeList.getLength();
        for (int i = 0; i < len; i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                pInfo.FamilyName = getFirstChildNodeValue(node, "FamilyName");
                pInfo.GivenNames = getFirstChildNodeValue(node, "GivenNames");
                pInfo.Address = getFirstChildNodeValue(node, "Address");
                pInfo.Birthday= getFirstChildNodeValue(node, "Birthday");
                pInfo.PhoneEmail = getFirstChildNodeValue(node, "PhoneEmail");
                pInfo.ValueH= getFirstChildNodeValue(node, "ValueH");
                pInfo.ValueL = getFirstChildNodeValue(node, "ValueL");
                pInfo.RateH= getFirstChildNodeValue(node, "RateH");
                pInfo.RateL = getFirstChildNodeValue(node, "RateL");
                pInfo.BestValueTarget= getFirstChildNodeValue(node, "BestValueTarget");
                pInfo.BestValueType = getFirstChildNodeValue(node, "BestValueType");
                pInfo.PatientNotes =getFirstChildNodeValue(node, "PatientNotes");
            }
        }
    }
    void populateReadings(Document doc)
    {
        NodeList nodeList = doc.getElementsByTagName("MeasureRec");
        int len = nodeList.getLength();
        for (int i = 0; i < len; i++) {
            Readings pr = new Readings();
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                pr.pid = getFirstChildNodeValue(node, "ID");
                pr.MeasureID = getFirstChildNodeValue(node, "MeasureID");
                pr.MeasureDate = getFirstChildNodeValue(node, "MeasureDate");
                pr.PEFValue = getFirstChildNodeValue(node, "PEFValue");
                pr.FEV1Value = getFirstChildNodeValue(node, "FEV1Value");
                pr.Error = getFirstChildNodeValue(node, "Error");
                pr.BestValue = getFirstChildNodeValue(node, "BestValue");
                _readings.add(pr);
            }
        }
    }
    
    void printReadings()
    {
        System.out.println("ID\tMeasureId\tMeasureDate\t\t\tPEFValue\tFEV1Value");
        for(Readings pr:_readings)
        {
            System.out.println(pr.pid+"\t" + pr.MeasureID+"\t\t"+pr.MeasureDate+"\t"+pr.PEFValue+"\t\t"+pr.FEV1Value );
        }
    }
    
    void printPatientInfo()
    {
        System.out.println("FamilyName: " + pInfo.FamilyName);
        System.out.println("GivenNames:  " + pInfo.GivenNames);
        System.out.println( "Birthday: " + pInfo.Birthday);
        System.out.println("RateH: "  + pInfo.RateH);
        System.out.println( "RateL: " + pInfo.RateL );
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
