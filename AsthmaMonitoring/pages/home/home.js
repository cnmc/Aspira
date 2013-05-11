﻿(function () {
    "use strict";

    WinJS.UI.Pages.define("/pages/home/home.html", {
        // This function is called whenever a user navigates to this page. It
        // populates the page elements with the app's data.
      
        ready: function (element, options) {
          //  WinJS.Utilities.id("adminLogin").listen("click", this.adminLogin, false);
            WinJS.Utilities.id("helpLogin").listen("click", helpLogin, false);
            WinJS.Utilities.id("takeReading").listen("click", teaseListener, false);
          
            // getMedicationText("0300");
            WinJS.Utilities.id("takeReading").removeEventListener("click", takeReading, false);
            waitForConfigFileToLoad();
            waitForFileToLoad();
            enableSubsequentReading();
            Windows.Storage.ApplicationData.current.localSettings.values["nextReadingTimeoutId"] = setTimeout(
           this.nextReadingAlert, calculateNextReadingTimeout());// set next reading timer
            if (AsthmaGlobals.fileConfig.config.airQualityConfig.airQualityMonitoringEnabled == false) {
                $("#rightItem").hide();
            }
            //writeLogfile();
            document.getElementById("nextReadingContent").innerHTML =
                 Windows.Storage.ApplicationData.current.localSettings.values["nextReadingTimeText"];
            AsthmaGlobals.intervalOn = false;//declaring a global var
           
        //Now Animating ;D

            //1. if user just completed the reading then show the fish happy but 
                //2. change mood back to normal in some time
                
            if (AsthmaGlobals.currMood == "happy") {
                changeFishMood("happy");
                appendLog("navigation", "application", "Reading Complete");
                Windows.Storage.ApplicationData.current.localSettings.values["changeMoodNormalTimeoutId"] =
                setTimeout(changeFishMood, AsthmaGlobals.fileConfig.config.animation.switchMoodSleepy, "sleepy");
                //TO DO: //3. disable the fish tank until next reading
            } else {
                initializeAnimation();
            }
            //set next medication alert
            setMedicationTimeout();
            if (AsthmaGlobals.hasSymptoms == true && AsthmaGlobals.symptomsMedicationText != undefined) {
                initateDynamicAlert("symptoms", AsthmaGlobals.symptomsMedicationText);
                AsthmaGlobals.hasSymptoms = false;
            }
            if (AsthmaGlobals.fileConfig.config.airQualityConfig.airQualityMonitoringEnabled == true) {
                var readingInterval = 900000; // initial 15 mins delay
                
                Windows.Storage.ApplicationData.current.localSettings.values["checkAirQualityTimeoutId"] = setTimeout(
                  this.checkAirQuality, readingInterval);//check air qulity regularly
            }
        },
        
       
        adminLogin: function (eventInfo) {
            WinJS.Navigation.navigate("/pages/adminPage/adminPage.html");
        },
        
        nextReadingAlert: function () {
            //enable user going into the flow when its time for new reading
            
            changeFishMood("attentive"); 
            appendLog("alert", "application home", "Take Reading");
            // Windows.Storage.ApplicationData.current.localSettings.values["bowlClickListener"] = 
            WinJS.Utilities.id("takeReading").listen("click", takeReading, false);
            WinJS.Utilities.id("takeReading").removeEventListener("click", teaseListener, false);
            WinJS.Utilities.id("helpLogin").removeEventListener("click", helpLogin, false);
            // User doesnt take readings, so set a timer but clear it if you leave the page
            Windows.Storage.ApplicationData.current.localSettings.values["nextReadingNotTakenTimeoutId"] =
            setTimeout(readingNotTaken, AsthmaGlobals.fileConfig.config.alertInfo.alertLength);// set next reading not taken timer
            //2. 
            initateDynamicAlert("scheduledReading", "Take a reading, tap on the bowl!!")
            animateNextReading();
            if (AsthmaGlobals.fileConfig.config.alertInfo.sound == true) {
                playAlert();
                 Windows.Storage.ApplicationData.current.localSettings.values["nextReadingSoundAlert"] =
                setInterval(playAlert, 6000);
            }

        },
       
        checkAirQuality: function () {
            getAirQualityProperties();// update config from a fresh config file
            var imageSrc = "/images/traffic_signal_"
            if (AsthmaGlobals.airQualityConfig.airQualityMeter.isConnected == "true") {
                imageSrc += AsthmaGlobals.airQualityConfig.airQualityMeter.readingZone;
            } else {
                imageSrc += "disconnected";
            }
            imageSrc += ".png";
            if (document.getElementById("trafficSignal") != undefined)
            document.getElementById("trafficSignal").setAttribute("src", imageSrc);
            //var timeElapsedLastDynamicReading = new Date().getTime() - AsthmaGlobals.airQualityConfig.airQualityMeter.lastDynamicReadingTakenAt;
            // check for reading zone are we in alert zone? 
            //only one reading can be taken between two subsequent readings
            if ((AsthmaGlobals.airQualityConfig.airQualityMeter.readingZone == AsthmaGlobals.airQualityConfig.airQualityMeter.takeDynamicReadingOn1 ||
                 AsthmaGlobals.airQualityConfig.airQualityMeter.readingZone == AsthmaGlobals.airQualityConfig.airQualityMeter.takeDynamicReadingOn2) &&
               
                AsthmaGlobals.dynamicAlertDisplay == false && 
               
                AsthmaGlobals.airQualityConfig.airQualityMeter.isConnected == "true" && //airquality meter should be connected
               
               // AsthmaGlobals.canTakeReading == true && //enabled to true after X mins of a reading
               
                isNextReadingNear() != true && //scheduled reading should not be nearer than X mins
                 AsthmaGlobals.fileConfig.config.airQualityConfig.airQualityMonitoringEnabled == true
                ) {
                initateDynamicAlert("dynamicReading", AsthmaGlobals.fileConfig.config.alertInfo.dynamicReadingAlertText);
                AsthmaGlobals.dynamicAlertDisplay = true;
            } else if (AsthmaGlobals.airQualityConfig.airQualityMeter.isConnected == "false" && AsthmaGlobals.notifiedDisconnection == false) {
                generateToast("The air quality meter seems to be disconnected, please refer the help manual and connect it again.")
                setTimeout(function () { AsthmaGlobals.notifiedDisconnection = false; }, 900000);
                AsthmaGlobals.notifiedDisconnection = true;
            }
            var readingInterval = 30000;
            if (AsthmaGlobals.airQualityConfig == null) {
                readingInterval = 30000;
            } else {
                readingInterval = AsthmaGlobals.airQualityConfig.airQualityMeter.frequencyReadingThisFile;
            }
            Windows.Storage.ApplicationData.current.localSettings.values["checkAirQualityTimeoutId"] = setTimeout(
                 this.checkAirQuality, readingInterval);//check air qulity regularly
        }
    });
})();
function helpLogin (eventInfo) {
    disableAllTimers();
    WinJS.Navigation.navigate("/pages/contact/contact.html");
}
// animate reading box
function waitForFileToLoad() {
    var check = false;
    var count = 0;
    while (!check) {
        if (AsthmaGlobals.airQualityConfig != null) {
            return;
        }
        if (count == 5000 || count == 10000 || count == 15000) {
            getAirQualityProperties();
        } if (count > 15000) {
            break;
        }
        count++;
    }
}
function waitForConfigFileToLoad() {
    var check = false;
    var count = 0;
    while (!check) {
        if (AsthmaGlobals.fileConfig != null) {
            return;
        }
        if (count == 5000 || count == 10000 || count == 15000) {
            getProperties();
        } if (count > 15000) {
            break;
        }
        count++;
    }
}
function animateNextReading() {

    $("#nextReadingCard").animate({ marginLeft: "+=50px"}, 1000, "swing");
    $("#nextReadingCard").animate({ marginLeft: "-=50px" }, 1000, "swing", animateNextReading);
}
// the next reading should be more than the minimum interval of X min
function isNextReadingNear() {
    
    if (calculateNextReadingTimeout() < AsthmaGlobals.fileConfig.config.alertInfo.intervalInTwoReadings
        || calculatePrevReadingTimeout() < AsthmaGlobals.fileConfig.config.alertInfo.intervalInTwoReadings) {
        return true;
    }
    else {
        return false;
    }
}
// subsequent reading after X mins
function enableSubsequentReading() {
    setTimeout((function () {
        AsthmaGlobals.canTakeReading = true;
    }), AsthmaGlobals.fileConfig.config.alertInfo.intervalInTwoReadings)
}

//This func is called when you click on the bowl
function takeReading(eventInfo) {
    appendLog("click", "Fish Bowl", "Started taking reading");
    AsthmaGlobals.dynamicAlertDisplay = false;
    takeDynamicReading(eventInfo);// 
}
// This func is for dynamic reading.
function takeDynamicReading(eventInfo) {
    changeFishMood("attentive");
    disableAllTimers();
    Windows.Storage.ApplicationData.current.localSettings.values["readingStartTime"] =
         new Date().toString();
    AsthmaGlobals.tempPefVal == null;
    WinJS.Navigation.navigate("/pages/readingPage/readingPage.html");
}


function readingNotTaken  () {
    // stop alert that says take reading
    appendLog("alert", "application", "missed reading");
    clearInterval(Windows.Storage.ApplicationData.current.localSettings.values["nextReadingSoundAlert"]);
    $("#nextReadingCard").stop();
    $("#nextReadingCard").stop();
    AsthmaGlobals.canTakeReading == true;
    //clearInterval(Windows.Storage.ApplicationData.current.localSettings.values["nextReadingIntervalId"]);
    // go set new time for reading
    Windows.Storage.ApplicationData.current.localSettings.values["nextReadingTimeoutId"] = setTimeout(
           this.nextReadingAlert, calculateNextReadingTimeout());// set next reading timer
    // change text of the card: put next time for reading 
     document.getElementById("nextReadingContent").innerHTML =
       Windows.Storage.ApplicationData.current.localSettings.values["nextReadingTimeText"];
     if (document.getElementById("nextReadingCard").style.display == "none") {
         document.getElementById("nextReadingCard").style.display = "block";
     }
    //change the mood of the fish
     changeFishMood("sick");
     
}

function disableAllTimers() {
    clearInterval(Windows.Storage.ApplicationData.current.localSettings.values["airQualityAlertInterval"]);
    clearInterval(Windows.Storage.ApplicationData.current.localSettings.values["nextReadingSoundAlert"]);
    clearInterval(Windows.Storage.ApplicationData.current.localSettings.values["checkAirQualityTimeoutId"]);
    clearTimeout(Windows.Storage.ApplicationData.current.localSettings.values["nextReadingTimeoutId"]);
    clearInterval(Windows.Storage.ApplicationData.current.localSettings.values["nextReadingIntervalId"]);
    clearTimeout(Windows.Storage.ApplicationData.current.localSettings.values["nextReadingNotTakenTimeoutId"]);
    clearTimeout(Windows.Storage.ApplicationData.current.localSettings.values["changeMoodNormalTimeoutId"]);
    clearTimeout(Windows.Storage.ApplicationData.current.localSettings.values["nextMedicationTimeoutId"]);
    clearTimeout(Windows.Storage.ApplicationData.current.localSettings.values["changeMoodSleepyTimeoutId"]);
}
function calculateNextReadingTimeout() {
    getProperties();
    var spiroReadingTimeArray = AsthmaGlobals.fileConfig.config.alertInfo.spiroReadingTime;
    spiroReadingTimeArray.sort();
    var currHour = new Date().getHours();
    var currMins = new Date().getMinutes();
    var nextReadingHour = null;
    for (var i = 0; i < spiroReadingTimeArray.length ; i++) {
        //get the largest hour from the array
        if (currHour < String(spiroReadingTimeArray[i]).substr(0, 2)) {
            nextReadingHour = String(spiroReadingTimeArray[i]);
            break;
        } else if (currHour <= String(spiroReadingTimeArray[i]).substr(0, 2)
            && currMins < String(spiroReadingTimeArray[i]).substr(2, 2)) {
            //else if it is the same hour and min when compared to time right now
            nextReadingHour = String(spiroReadingTimeArray[i]);
            break;
        }

    }
    var nextReadingObj = new Date();// create the date obj of the time we next reading has to be taken
    // if no reading was found for the day then schedule it for tomorrow
    if (nextReadingHour == null) {
        nextReadingHour = String(spiroReadingTimeArray[0]);
        nextReadingObj.setDate(nextReadingObj.getDate() + 1);
    }

    nextReadingObj.setHours(nextReadingHour.substr(0, 2), nextReadingHour.substr(2, 2), 00, 00);
    var displayHours = nextReadingObj.getHours();
    if (displayHours == 0) {
        displayHours = "00";
    }
    var ampm = "am";
    if (displayHours >= 12) {
        if (displayHours > 12)
        displayHours -= 12;
        ampm = "pm";
    }
    Windows.Storage.ApplicationData.current.localSettings.values["nextReadingTimeText"] =
        displayHours + ":" + nextReadingHour.substr(2, 2)+" " + ampm;
    return nextReadingObj.getTime() - new Date().getTime();
} 
function initateDynamicAlert(type, description) {
    changeFishMood("attentive");
    var id = AsthmaGlobals.idCount;
    AsthmaGlobals.idCount = AsthmaGlobals.idCount + 1;
    var content = "<div id='dynamicAlertBox"+id+"' class='dynamicAlertBox' >"
    content += "<div class='dynamicContent'>";
    if (description != undefined) {
        content += description;
        if (type != "symptoms") {
            generateToast(description);
        }
    } else {
        content += AsthmaGlobals.medicationAlertText;
        generateToast(AsthmaGlobals.medicationAlertText);
    }
    content += "</div>";
    if (type != "scheduledReading") {
    content += "<div class='buttonPanel'>";
    content += "<span id='dismiss" + id + "' class='button-left'></span>";
   
        if (type == "dynamicReading") {
            content += "<span id='takeReading" + id + "' class='button-right'></span>";
        } else {
            content += "<span id='done" + id + "' class='button-right'></span>";
        }
    
        content += "</div>";
    }
    content += "</div>";
    if (type == "medication" && AsthmaGlobals.medicationAlertText != null && String(AsthmaGlobals.medicationAlertText).trim() != "" && AsthmaGlobals.medicationAlertText != "\r\n"
         && AsthmaGlobals.medicationAlertText != "\n") {
        $("#dynamicInfoBox").append(content);
            appendLog("alert", "application", "medication");
         //   document.getElementById("done").onclick = alertActionComplete;
            var dismissBtn = "<button id='dismiss" + id + "' class='button-left' onclick='dismissAlert(" + id + ")'>Dismiss</button>";
            $("#dismiss"+id).append(dismissBtn);
            var doneBtn = "<button id='done" + id + "' class='button-right' onclick='alertActionComplete(" + id + ")'>Done</button>";
            $("#done"+id).append(doneBtn);
        // document.getElementById("dismiss").onclick = dismissAlert;
        
    } else {
        $("#dynamicInfoBox").append(content);
        if (type != "scheduledReading") {
            if (type == "dynamicReading") {
                appendLog("alert", "application", "dynamic alert");
                var takeReadingBtn = "<button id='takeReading" + id + "' class='button-left' onclick='takeDynamicReading();'>Take Reading</button>";
                $("#takeReading" + id).append(takeReadingBtn);
                //document.getElementById("done").onclick = takeDynamicReading;
                var doneBtn = "<button id='dismiss" + id + "' class='button-right' onclick='dismissAlert(" + id + ")'>Dismiss</button>";
                $("#dismiss" + id).append(doneBtn);
                // document.getElementById("done").onclick = alertActionComplete;

            } else if (type = "symptoms") {
                appendLog("alert", "application", "symptom med Reminder");
                var dismissBtn = "<button id='dismiss" + id + "' class='button-left' onclick='dismissAlert(" + id + ")'>Dismiss</button>";
                $("#dismiss" + id).append(dismissBtn);
                var doneBtn = "<button id='done" + id + "' class='button-right' onclick='alertActionComplete(" + id + ")'>Done</button>";
                $("#done" + id).append(doneBtn);
            }

          //  document.getElementById("dismiss").onclick = dismissAlert;
        }
    }
    setMedicationTimeout();
}

function dismissAlert(id) {
    changeFishMood("sleepy");
    setMedicationTimeout();
    var divID = "dynamicAlertBox" + id;
    if (document.getElementById(divID) != null || document.getElementById(divID) != undefined) {
        document.getElementById(divID).removeNode(true);
        appendLog("click", "alert", "dismissed");
    }
}

function alertActionComplete(id) {
    changeFishMood("sleepy");
    var divID = "dynamicAlertBox" + id;
    setMedicationTimeout();
    appendLog("click", "alert", "done");
    document.getElementById(divID).removeNode(true);
}

//function calculateNextmedicationTimeout() {
//    getProperties();
//    var medicationReadingTimeArray = AsthmaGlobals.fileConfig.config.alertInfo.medicationAlertTime;
//    medicationReadingTimeArray.sort();
//    var currHour = new Date().getHours();
//    var currMins = new Date().getMinutes();
//    var nextMedicationHour = null;
//    for (var i = 0; i < medicationReadingTimeArray.length ; i++) {
//        //get the largest hour from the array
//        if (currHour < String(medicationReadingTimeArray[i]).substr(0, 2)) {
//            nextMedicationHour = String(medicationReadingTimeArray[i]);
//            break;
//        } else if (currHour <= String(medicationReadingTimeArray[i]).substr(0, 2)
//            && currMins < String(medicationReadingTimeArray[i]).substr(2, 2)) {
//            //else if it is the same hour and min when compared to time right now
//            nextMedicationHour = String(medicationReadingTimeArray[i]);
//            break;
//        }

//    }
//    var nextMedicationObj = new Date();// create the date obj of the time we next reading has to be taken
//    // if no reading was found for the day then schedule it for tomorrow
//    if (nextMedicationHour == null) {
//        nextMedicationHour = String(medicationReadingTimeArray[0]);
//        nextMedicationObj.setDate(nextMedicationObj.getDate() + 1);
//    }

//    nextMedicationObj.setHours(nextMedicationHour.substr(0, 2), nextMedicationHour.substr(2, 2), 00, 00);
//    var displayHours = nextMedicationObj.getHours();
//    if (displayHours == 0) {
//        displayHours = "00";
//    }
//    var ampm = "am";
//    if (displayHours >= 12) {
//        if (displayHours > 12)
//            displayHours -= 12;
//        ampm = "pm";
//    }
//   // Windows.Storage.ApplicationData.current.localSettings.values["nextMedicationTimeText"] =
//      //  displayHours + ":" + nextReadingHour.substr(2, 2) + " " + ampm;
//    return nextMedicationObj.getTime() - new Date().getTime();
//}

function teaseListener() {
    appendLog("click", "Fish Bowl", "tease");
    if (AsthmaGlobals.allTeaseImgArray.length == AsthmaGlobals.teaseIndex) {
        AsthmaGlobals.teaseIndex = 0;
    }
    document.getElementById("fishImageID").removeAttribute("src");
    document.getElementById("fishImageID").setAttribute("src", AsthmaGlobals.allTeaseImgArray[AsthmaGlobals.teaseIndex]);
    if (AsthmaGlobals.changeMoodSleepyTimeoutId == null)
    {
        AsthmaGlobals.changeMoodSleepyTimeoutId =
        setTimeout(function () {
            AsthmaGlobals.changeMoodSleepyTimeoutId = null;
            changeFishMood();
        }, 10000, "sleepy");
    }
    AsthmaGlobals.teaseIndex = AsthmaGlobals.teaseIndex + 1;
}
// calculates time elapsed from previous reading
function calculatePrevReadingTimeout() {
    getProperties();
    var spiroReadingTimeArray = AsthmaGlobals.fileConfig.config.alertInfo.spiroReadingTime;
    spiroReadingTimeArray.sort();
    var currHour = new Date().getHours();
    var currMins = new Date().getMinutes();
    var prevReadingHour = null;
    for (var i = spiroReadingTimeArray.length - 1; i >=0  ; i--) {
        //get the largest hour from the array
        if (currHour > String(spiroReadingTimeArray[i]).substr(0, 2)) {
            prevReadingHour = String(spiroReadingTimeArray[i ]);
            break;
        } else if (currHour == String(spiroReadingTimeArray[i]).substr(0, 2)
            && currMins == String(spiroReadingTimeArray[i]).substr(2, 2)) {
            //else if it is the same hour and min when compared to time right now
            return AsthmaGlobals.fileConfig.config.alertInfo.intervalInTwoReadings - 1000;
           
        }
        else if (currHour >= String(spiroReadingTimeArray[i]).substr(0, 2)
            && currMins > String(spiroReadingTimeArray[i]).substr(2, 2)) {
            //else if it is the same hour and min when compared to time right now
            prevReadingHour = String(spiroReadingTimeArray[i ]);
            break;
        }

    }
    var prevReadingObj = new Date();// create the date obj of the time we next reading has to be taken
    // if no reading was found for the day then schedule it for tomorrow
    if (prevReadingHour == null || prevReadingHour == "undefined") {
        prevReadingHour = String(spiroReadingTimeArray[spiroReadingTimeArray.length - 1]);
        prevReadingObj.setDate(prevReadingObj.getDate() - 1);
    }

    prevReadingObj.setHours(prevReadingHour.substr(0, 2), prevReadingHour.substr(2, 2), 00, 00);
    var displayHours = prevReadingObj.getHours();
    if (displayHours == 0) {
        displayHours = "00";
    }
    var ampm = "am";
    if (displayHours >= 12) {
        if (displayHours > 12)
            displayHours -= 12;
        ampm = "pm";
    }
    Windows.Storage.ApplicationData.current.localSettings.values["prevReadingTimeText"] =
        displayHours + ":" + prevReadingHour.substr(2, 2) + " " + ampm;
    
    return new Date().getTime() - prevReadingObj.getTime();
}

function playAlert() {
    document.getElementById("audioAlert").play();
}