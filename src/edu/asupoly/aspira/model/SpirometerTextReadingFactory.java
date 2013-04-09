package edu.asupoly.aspira.model;

import java.util.Properties;

public interface SpirometerTextReadingFactory {
    SpirometerReadings createSpirometerTextReadings(Properties props) throws Exception;
}
