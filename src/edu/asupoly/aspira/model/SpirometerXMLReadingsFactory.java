package edu.asupoly.aspira.model;

import java.util.Properties;

public interface SpirometerXMLReadingsFactory {
    SpirometerReadings createSpirometerXMLReadings(Properties props) throws Exception;
}
