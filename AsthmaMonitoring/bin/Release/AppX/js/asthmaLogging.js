function writeLogfile() {
    Windows.Storage.KnownFolders.documentsLibrary.getFileAsync("asthmaMonitoringLog.txt").done(function (datafile) {
        try {
            var createLog;
            if (AsthmaGlobals.logString != undefined && AsthmaGlobals.logString !=null) {
                Windows.Storage.FileIO.readTextAsync(datafile).done(function (fileContent) {
                    if (fileContent == undefined || fileContent == null || fileContent == "" || fileContent == "undefined") {
                        createLog = { "logs": AsthmaGlobals.logString };
                    } else {
                        createLog = JSON.parse(fileContent);
                        createLog.logs.concat(AsthmaGlobals.logString);
                    }
                    Windows.Storage.FileIO.writeTextAsync(datafile, JSON.stringify(createLog));
                }); 

               
                AsthmaGlobals.logString = null;
                createLog = null;
            }
        } catch (e) {
            console.log(e.message);
        }
        
    }); 
}
//function getLogFile() {
//    Windows.Storage.KnownFolders.documentsLibrary.getFileAsync("asthmaMonitoringLog.json").done(function (datafile) {
//        if (datafile !== null) {
//            Windows.Storage.FileIO.readTextAsync(datafile).done(function (fileContent) {
//                try {
//                    AsthmaGlobals.logString= JSON.parse(fileContent);
//                } catch (e) {
//                    console.log(e.message);
//                }
//            });
//        }
//    });
//}
function initializeLogging() {
    if (AsthmaGlobals.loggingIntervalSet == undefined) {

       // setInterval(getLogFile, AsthmaGlobals.fileConfig.config.logFileWriteFrequency - 2000);
        AsthmaGlobals.loggingIntervalSet =
            setInterval(writeTextLogfile, AsthmaGlobals.fileConfig.config.logFileWriteFrequency);
    }

}
function writeTextLogfile() {
    if (AsthmaGlobals.logString != null) {
        Windows.Storage.KnownFolders.documentsLibrary.getFileAsync("asthmaMonitoringLog.txt").then(function (datafile) {

            Windows.Storage.FileIO.appendTextAsync(datafile, AsthmaGlobals.logString);
            AsthmaGlobals.logString = null;
        });
        
    }
   

}

function appendLog(eventType, target, value) {
    //getLogFile();

    var bufferedLogString = new String();
   // var bufferedLogArray = new Array(); 
    if (AsthmaGlobals.logString != undefined && AsthmaGlobals.logString != null) {
     //   bufferedLogArray = AsthmaGlobals.logString;
        bufferedLogString = AsthmaGlobals.logString;
    }
    var currDate = new Date().toDateString();
    var currTime = new Date().toTimeString();
    var latestLog = AsthmaGlobals.fileConfig.config.patientID + " ### " +
         AsthmaGlobals.fileConfig.config.deviceID + " ### " +
          AsthmaGlobals.fileConfig.config.buildVer + " ### " + eventType + " ### " +
          target + " ### " + value + " ### " + currDate + " " + currTime +  '\n';
    bufferedLogString += latestLog;
   // bufferedLogArray.push(latestLog);
    AsthmaGlobals.logString = bufferedLogString;
    initializeLogging();
}