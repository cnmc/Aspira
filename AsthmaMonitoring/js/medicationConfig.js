function readMedicationfile() {
    var medicationTimeArray = new Array();
    var medicationIdentificationArray = new Array();
    var currMedName;
    var currMedDesc;
    Windows.Storage.KnownFolders.documentsLibrary.getFileAsync("medicationReminder.txt").done(function (datafile) {

        try {
            Windows.Storage.FileIO.readLinesAsync(datafile).done(function (lines) {
                lines.forEach(function (line) {
                    if (line.indexOf("Medicine Name") != -1) {
                        currMedName = line.substring(line.indexOf(":"), line.length);
                    } else if (line.indexOf("Medicine Description") != -1) {
                        currMedDesc = line.substring(line.indexOf(":"), line.length);
                    } else {
                        medicationTimeArray.push(line);
                        medicationIdentificationArray.push(line);
                    }
                    console.log(line);
                });
           });
        } catch (e) {
            console.log(e.message);
        }

    });
}

function medicationTime(time, name, description) {
    this.time = time;
    this.name = name;
    this.description = description;
}


function getMedicationText(time) {
    var retString = "";
    for (var i = 0; i < allMedicationArray.length; i++) {
        if (allMedicationArray[i].time == time) {
            if (retString != "") {
                retString += "and ";
            }
            retString += allMedicationArray[i].name + " : " + allMedicationArray[i].description;
        }
    }
    return retString;
}


//function getMedicationText(time) {
//    var currMedObjs = getMedicationObjs(time);
//    var retString = "";
//    if (currMedObjs.length > 0) {
//        for (var i = 0; i < currMedObjs.length; i++) {
//            if (retString != "") {
//                retString += "and ";
//            }
//            retString += currMedObjs[i].name + " : " + currMedObjs[i].description;
//        }
//    }
//    return retString;
//}