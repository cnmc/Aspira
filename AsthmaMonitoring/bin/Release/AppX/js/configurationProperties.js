function setProperties() {
    Windows.Storage.KnownFolders.documentsLibrary.getFileAsync("config.json").done(function (datafile) {
        appendLog("debug", "Start Writing File", "config.json");
        if (AsthmaGlobals.fileConfig != null) {
            Windows.Storage.FileIO.writeTextAsync(datafile, JSON.stringify(AsthmaGlobals.fileConfig));
            appendLog("debug", "Done Reading File", "config.json");
        }
    });
}
getProperties();
function getProperties() {
    Windows.Storage.KnownFolders.documentsLibrary.getFileAsync("config.json").done(function (datafile) {

         appendLog("debug", "Reading File", "config.json");
        if (datafile !== null) {
            Windows.Storage.FileIO.readTextAsync(datafile).done(function (fileContent) {
                try {
                    AsthmaGlobals.fileConfig = JSON.parse(fileContent);
                    if (AsthmaGlobals.fileConfig == null) {
                        getProperties();
                    }

                } catch (e) {
                    // e.message;
                    console.log(e.message);
                    // Windows.UI.Popups.MessageDialog(e.message).showAsync();

                    appendLog("error", "Reading File, caught exception", "config.json");
                }
            });
        }
         appendLog("debug", "Done Reading File", "config.json");
    });
}

// air quality config file
function setAirQualityProperties() {
    Windows.Storage.KnownFolders.documentsLibrary.getFileAsync("airQualityStatus.json").done(function (datafile) {
        appendLog("debug", "writing File", "airQualityStatus.json");

        if (AsthmaGlobals.airQualityConfig != null) {
            Windows.Storage.FileIO.writeTextAsync(datafile, JSON.stringify(AsthmaGlobals.airQualityConfig));
        }
        appendLog("debug", "Done writing File", "airQualityStatus.json");
    });
}
readMedicationfile();
function readMedicationfile() {
    Windows.Storage.KnownFolders.documentsLibrary.getFileAsync("medicationReminder.txt").done(function (datafile) {
        appendLog("debug", "Reading File", "medicationReminder.txt");
        if (datafile !== null) {
            Windows.Storage.FileIO.readTextAsync(datafile).done(function (fileContent) {
                try {
                    AsthmaGlobals.medicationArray = fileContent;
                } catch (e) {
                    // e.message;
                    console.log(e.message);
                    // Windows.UI.Popups.MessageDialog(e.message).showAsync();
                }
            });
        }

        appendLog("debug", "Done Reading File", "medicationReminder.txt");
    });
}

getAirQualityProperties();
function getAirQualityProperties() {
    Windows.Storage.KnownFolders.documentsLibrary.getFileAsync("airQualityStatus.json").done(function (datafile) {
        appendLog("debug", "Reading File", "airQualityStatus.json");
        if (datafile !== null) {
            Windows.Storage.FileIO.readTextAsync(datafile).done(function (fileContent) {
                try {
                    AsthmaGlobals.airQualityConfig = JSON.parse(fileContent);
                } catch (e) {
                    // e.message;
                    console.log(e.message);
                    // Windows.UI.Popups.MessageDialog(e.message).showAsync();
                }
            });
        }
        appendLog("debug", "Done Reading File", "airQualityStatus.json");
    });
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
    var latestLog;
    if (AsthmaGlobals.fileConfig != undefined) {
        latestLog = AsthmaGlobals.fileConfig.config.patientID + " ### " +
             AsthmaGlobals.fileConfig.config.deviceID + " ### " +
              AsthmaGlobals.fileConfig.config.buildVer + " ### " + eventType + " ### " +
              target + " ### " + value + " ### " + currDate + " " + currTime + '\n';
    } else {
         latestLog = 1234 + " ### " +
            1234 + " ### " +
             1234 + " ### " + eventType + " ### " +
             target + " ### " + value + " ### " + currDate + " " + currTime + '\n';
    }
    bufferedLogString += latestLog;
    // bufferedLogArray.push(latestLog);
    AsthmaGlobals.logString = bufferedLogString;
    initializeLogging();
}
/*function getProperties() {
    var eventFile;
    var uri = new Windows.Foundation.Uri('ms-appx:///config.json');
    Windows.Storage.KnownFolders.documentsLibrary.getFileAsync("config.json").done(function (dataFile) {
   // Windows.Storage.StorageFile.getFileFromApplicationUriAsync(uri).then(function (dataFile) {
        // Add code to process the text read from the file
        var result;

        dataFile.openAsync(Windows.Storage.FileAccessMode.read).then(function (stream) {
            var size = stream.size;
            if (size == 0) {
                // Data not found 
            }
            else {
                var inputStream = stream.getInputStreamAt(0);
                var reader = new Windows.Storage.Streams.DataReader(inputStream);

                reader.loadAsync(size).then(function () {
                    var array = new Array(size);
                    reader.readBytes(array);

                    var newString = "";
                    for (var i = 0; i < array.length; i++) {
                        if (array[i] >= 32 && array[i] <= 126) {
                            var c = String.fromCharCode(array[i]);
                            newString += c;
                        }
                    }
                     
                    AsthmaGlobals.fileConfig = JSON.parse(newString);
                     reader.close();
                })

            }
        })
        
    });

  
        // Write Text to a file
        //  Windows.Storage.FileIO.writeTextAsync(eventFile, JSON.stringify(yourJSONObject));

        // Append Text to a file
        //  Windows.Storage.FileIO.appendTextAsync(eventFile, JSON.stringify(yourJSONObject));
    }*/

