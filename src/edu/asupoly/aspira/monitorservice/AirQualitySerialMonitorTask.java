package edu.asupoly.aspira.monitorservice;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.asupoly.aspira.Aspira;
import edu.asupoly.aspira.dmp.AspiraDAO;
import edu.asupoly.aspira.dmp.IAspiraDAO;
import edu.asupoly.aspira.model.AirQualityReadings;
import edu.asupoly.aspira.model.ParticleReading;

public class AirQualitySerialMonitorTask extends AspiraTimerTask {
    // wonder if these should be props
    private static final int BAUD_RATE = SerialPort.BAUDRATE_9600;
    private static final int DATA_BITS = SerialPort.DATABITS_8;
    private static final int STOP_BITS = SerialPort.STOPBITS_1;
    private static final int PARITY    = SerialPort.PARITY_NONE;
    private static final int MASK      = 
            SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;
    private static final int TIMEOUT   = 500; // 1/2 sec timeout on serial port read
    
    private static final Logger LOGGER = Aspira.getAspiraLogger();
    private static int __eventCount = 0;
    
    private SerialPort __serialPort;
    private Properties __props;
    
    public AirQualitySerialMonitorTask() {
        super();
    }
    
    @Override
    public void run() {
        System.out.println("IN AQSMTask, processed serial events since last call: " + __eventCount);
        __eventCount = 0;
    }
    
    @Override
    public void finalize() {
        try {
            if (__serialPort.isOpened()) {
                if (__serialPort.closePort()) System.out.println("CLOSED SERIAL PORT");
                else System.out.println("Serial port already closed!");
            }
        } catch (Throwable t) {
            // silently discard
        }
    }
    
    @Override
    public boolean init(Properties p) {
        boolean rval = true;
        // check we have deviceId, patientId, and file
        __props = new Properties();

        String deviceId     = Aspira.getAirQualityMonitorId();
        String patientId    = Aspira.getPatientId();
        String serialPort   = p.getProperty("aqmSerialPort");
        if (deviceId != null && patientId != null && serialPort != null) {
            __props.setProperty("deviceid", deviceId);
            __props.setProperty("patientid", patientId);
            __props.setProperty("aqmSerialPort", serialPort);
            
            try {
                // this section initializes the serialPort
                __serialPort = new SerialPort(serialPort);
                rval = __serialPort.openPort();
                if (rval) {
                    __serialPort.setParams(BAUD_RATE, DATA_BITS, STOP_BITS, PARITY);
                    __serialPort.setEventsMask(MASK);
                    // we purge initially to try and clear buffers
                    __serialPort.purgePort(SerialPort.PURGE_RXCLEAR | SerialPort.PURGE_TXCLEAR);
                    __serialPort.addEventListener(new AspiraDylosSerialEventListener());
                }
            } catch (SerialPortException spe) {
                LOGGER.log(Level.SEVERE, "Unable to open and configure serial port for Dylos Logger: " + spe.getMessage());
                rval = false;
            }
            
        } else {
            rval = false;
        }
        _isInitialized = rval;
        
        // This section tries to initialize the last reading date
        _lastRead = new Date(0L);  // Jan 1 1970, 00:00:00
        try {
            IAspiraDAO dao = AspiraDAO.getDAO();
            AirQualityReadings aqr = dao.findAirQualityReadingsForPatientTail(__props.getProperty("patientid"), 1);
            if (aqr != null) {
                ParticleReading e = aqr.getFirstReading();
                if (e != null) {
                    _lastRead = e.getDateTime();
                }
            }
        } catch (Throwable t) {
            LOGGER.log(Level.WARNING, "Unable to get last reading time");
        }
        
        return rval;
    }
    
    // nested class
    class AspiraDylosSerialEventListener implements SerialPortEventListener {        
        @Override
        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR()){//If data is available
                if(event.getEventValue() > 0){//Check bytes count in the input buffer
                    //Read data, if 10 bytes available 
                    try {
                        byte buffer[] = __serialPort.readBytes(event.getEventValue(), TIMEOUT);
                        System.out.print("SERIAL EVENT LISTENER #" + ++__eventCount + ":\t" + new String(buffer));
                    }
                    catch (SerialPortException ex) {
                        ex.printStackTrace();
                    }
                    catch (SerialPortTimeoutException spte) {
                        spte.printStackTrace();
                    }
                }
            }
            else if(event.isCTS()){//If CTS line has changed state
                if(event.getEventValue() == 1){//If line is ON
                    System.out.println("CTS - ON");
                }
                else {
                    System.out.println("CTS - OFF");
                }
            }
            else if(event.isDSR()){///If DSR line has changed state
                if(event.getEventValue() == 1){//If line is ON
                    System.out.println("DSR - ON");
                }
                else {
                    System.out.println("DSR - OFF");
                }
            }
        }
    }
}
