package edu.asupoly.aspira.model;

import java.util.Properties;

public interface AirQualityReadingsFactory {
    AirQualityReadings createAirQualityReadings(Properties props) throws Exception;
}
