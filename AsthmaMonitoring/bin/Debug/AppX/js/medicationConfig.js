function readMedicationfile1() {
    var allMedicationArray = new Array();
    var currMedName;
    var currMedDesc;
    AsthmaGlobals.medicationArray = new Array();
    var getFile = Windows.Storage.KnownFolders.documentsLibrary.getFileAsync("medicationReminder.txt").then(function (datafile) {

        try {
            var getLines= Windows.Storage.FileIO.readLinesAsync(datafile).then(function (lines) {
              
                lines.forEach(function (line) {
                    if (line.indexOf("Medicine Name") != -1) {
                        currMedName = line.substring(line.indexOf(":"), line.length);
                    } else if (line.indexOf("Medicine Description") != -1) {
                        currMedDesc = line.substring(line.indexOf(":"), line.length);
                    } else {
                        var medicatnObj = new medicationClass(line, currMedName, currMedDesc);
                        AsthmaGlobals.medicationArray.push(medicatnObj);
                    }
                });

       });
       WinJS.Promise.join(getLines);
        } catch (e) {
            console.log(e.message);
        }
    });
    WinJS.Promise.join(getFile);
   
}


function medicationClass(time, name, description) {
    this.time = time;
    this.name = name;
    this.description = description;
}
function whichTime(morning, evening){
    var date = new Date();
    if (Number(String(morning).substring(0,1))  > date.getHours() && 
        Number(String(morning).substring(0,1))  >= date.getMinutes()){
        return
    }
}
function setMedicationTimeout(time) {
    var retString = "";
    var allMedicationStr = String(AsthmaGlobals.medicationArray);
   var MedicationInfoArray = allMedicationStr.split(";");
   var morningTime = String(MedicationInfoArray[0]).split("-")[1].trim();
   var eveningTime = String(MedicationInfoArray[1]).split("-")[1].trim();
   var alertTextArrayIndex = 3;
   var date = new Date();
   if (Number(String(morningTime).substring(0,2))  > date.getHours() ){
       alertTextArrayIndex = 3;
   }else if  (Number(String(eveningTime).substring(0,2))  > date.getHours()) {
       alertTextArrayIndex = 5;
   }

   var alertText = MedicationInfoArray[alertTextArrayIndex];
    if (allMedicationArray == null) {
        goToSleep(1000);
    }
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
function goToSleep(time) {
    
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