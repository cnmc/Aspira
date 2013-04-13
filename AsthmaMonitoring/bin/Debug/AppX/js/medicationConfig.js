
function setMedicationTimeout() {
    var retString = "";
    var allMedicationStr = String(AsthmaGlobals.medicationArray);
    var MedicationInfoArray = allMedicationStr.split(";");
    var morningTime = String(MedicationInfoArray[0]).split("-")[1].trim();
    var eveningTime = String(MedicationInfoArray[1]).split("-")[1].trim();
   
    var alertTextArrayIndex = 3;
    var date = new Date();
    var nextMedicationHour = null;
    if ( Number(String(morningTime).substring(0, 2)) >= date.getHours() &&
        Number(String(morningTime).substring(2,4)) > date.getMinutes()) {
        alertTextArrayIndex = 3;
        nextMedicationHour = morningTime;
    } else if ( Number(String(eveningTime).substring(0, 2)) >= date.getHours() &&
        Number(String(eveningTime).substring(2, 4)) > date.getMinutes()) {
        alertTextArrayIndex = 5;
        nextMedicationHour = eveningTime;
    }
    if (MedicationInfoArray.length >= 7) {
        AsthmaGlobals.symptomsMedicationText = MedicationInfoArray[7];
    }
    if (MedicationInfoArray[alertTextArrayIndex] != undefined || MedicationInfoArray[alertTextArrayIndex] != null
        || MedicationInfoArray[alertTextArrayIndex] != "") {
        AsthmaGlobals.medicationAlertText = MedicationInfoArray[alertTextArrayIndex];
    } else {
        AsthmaGlobals.medicationAlertText = null;
    }
    var nextMedicationObj = new Date();// create the date obj of the time we next reading has to be taken
    // if no reading was found for the day then schedule it for tomorrow
    if (nextMedicationHour == null) {
        nextMedicationHour = morningTime;
        nextMedicationObj.setDate(nextMedicationObj.getDate() + 1);
    }

    nextMedicationObj.setHours(nextMedicationHour.substr(0, 2), nextMedicationHour.substr(2, 2), 00, 00);
 
    // Windows.Storage.ApplicationData.current.localSettings.values["nextMedicationTimeText"] =
    //  displayHours + ":" + nextReadingHour.substr(2, 2) + " " + ampm;
    var timeout = nextMedicationObj.getTime() - new Date().getTime();
    if (Windows.Storage.ApplicationData.current.localSettings.values["nextMedicationTimeoutId"] != null ||
        Windows.Storage.ApplicationData.current.localSettings.values["nextMedicationTimeoutId"] != undefined) {
        clearTimeout(Windows.Storage.ApplicationData.current.localSettings.values["nextMedicationTimeoutId"]);
    }
    //if (AsthmaGlobals.medicationAlertText != null) {
        Windows.Storage.ApplicationData.current.localSettings.values["nextMedicationTimeoutId"] = setTimeout(
      initateDynamicAlert, timeout, "medication");
    //} else {
    //    if (Windows.Storage.ApplicationData.current.localSettings.values["afterNextMedicationTimeoutId"] != null ||
    //        (Windows.Storage.ApplicationData.current.localSettings.values["afterNextMedicationTimeoutId"] != undefined)) {
    //        clearTimeout(Windows.Storage.ApplicationData.current.localSettings.values["afterNextMedicationTimeoutId"]);
    //    }
    //    Windows.Storage.ApplicationData.current.localSettings.values["afterNextMedicationTimeoutId"] = setTimeout(
    //  setMedicationTimeout, timeout+3000);
    //}
}

