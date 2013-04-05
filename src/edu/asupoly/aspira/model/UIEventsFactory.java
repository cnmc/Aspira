package edu.asupoly.aspira.model;

import java.util.Properties;

public interface UIEventsFactory {
    UIEvents createUIEvents(Properties props) throws Exception;
}
