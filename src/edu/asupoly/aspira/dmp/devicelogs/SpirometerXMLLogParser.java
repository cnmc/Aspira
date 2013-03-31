/*
 * ASPIRA Project
 * This program does parsing
 * of Spirometer log files
 */
package edu.asupoly.aspira.dmp.devicelogs;

import edu.asupoly.aspira.model.SpirometerReading;
import edu.asupoly.aspira.model.SpirometerReadings;
import edu.asupoly.aspira.model.SpirometerXMLReadingsFactory;
import java.io.FileReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class SpirometerXMLLogParser implements SpirometerXMLReadingsFactory
{
    public SpirometerXMLLogParser() {}

    @Override
    public SpirometerReadings createSpirometerXMLReadings(Properties props) throws Exception
    {
        String deviceId  = props.getProperty("deviceid");
        String patientId = props.getProperty("patientid");
        SpirometerReadings _spReadings = new SpirometerReadings(deviceId, patientId);

        try
        {
            InputSource source = new InputSource(new FileReader(props.getProperty("splogfile")));
            Document doc = null;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(source);
            //buildpatientinfo(doc);  // KGDJ: this method does not do anything?
            populateReadings(doc, _spReadings);
        }
        catch (Throwable t) {
            Logger.getLogger(SpirometerXMLLogParser.class.getName()).log(Level.SEVERE, null, t);
        }

        return _spReadings;
    }

    /*
    private void buildpatientinfo(Document doc) throws DeviceLogException   
    {
        NodeList nodeList = doc.getElementsByTagName("Patient");
        int len = nodeList.getLength();
        for (int i = 0; i < len; i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                // KGDJ: Why do we create objects we will never use?
                String id = getFirstChildNodeValue(node, "ID");
                String Sex = getFirstChildNodeValue(node, "Sex");
                String ValueH= getFirstChildNodeValue(node, "ValueH");
                String ValueL = getFirstChildNodeValue(node, "ValueL");
                String RateH= getFirstChildNodeValue(node, "RateH");
                String RateL = getFirstChildNodeValue(node, "RateL");
                String BestValueTarget= getFirstChildNodeValue(node, "BestValueTarget");
                String BestValueType = getFirstChildNodeValue(node, "BestValueType");
                String PatientNotes =getFirstChildNodeValue(node, "PatientNotes");
                Patient pInfo = new Patient(id, Sex, RateH, RateL, ValueH, ValueL, BestValueType, BestValueTarget, PatientNotes);
            }
        }
    }
    */

    private void populateReadings(Document doc, SpirometerReadings _spReadings) throws DeviceLogException
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
                SpirometerReading pr = new SpirometerReading(id,  dt, mid,  pef, fev, err, bvalue);
                _spReadings.addReading(pr);
            }
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
