package edu.asupoly.aspira.model;

import java.util.Properties;

public interface SpirometerXMLReadingsFactory {
    SpirometerXMLReadings createSpirometerXMLReadings(Properties props) throws Exception;
}
