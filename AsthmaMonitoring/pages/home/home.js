(function () {
    "use strict";

    WinJS.UI.Pages.define("/pages/home/home.html", {
        // This function is called whenever a user navigates to this page. It
        // populates the page elements with the app's data.
        
        ready: function (element, options) {  
            WinJS.Utilities.id("adminLogin").listen("click", this.adminLogin, false);
            WinJS.Utilities.id("takeReading").listen("click", teaseListener, false);

            WinJS.Utilities.id("takeReading").removeEventListener("click", takeReading, false);
            enableSubsequentReading();
            Windows.Storage.ApplicationData.current.localSettings.values["nextReadingTimeoutId"] = setTimeout(
           this.nextReadingAlert, calculateNextReadingTimeout());// set next reading timer
            //set next medication alert
            if (AsthmaGlobals.fileConfig.config.alertInfo.medicationAlertTime != undefined) {
                Windows.Storage.ApplicationData.current.localSettings.values["nextMedicationTimeoutId"] = setTimeout(
              initateDynamicAlert, calculateNextmedicationTimeout());
            }
            //writeLogfile();
            document.getElementById("nextReadingContent").innerHTML =
                 Windows.Storage.ApplicationData.current.localSettings.values["nextReadingTimeText"];
            AsthmaGlobals.intervalOn = false;//declaring a global var
            Windows.Storage.ApplicationData.current.localSettings.values["checkAirQualityTimeoutId"] = setInterval(
              this.checkAirQuality, 3000);//check air qulity regularly
            //Now Animating ;D

            //1. if user just completed the reading then show the fish happy but 
                //2. change mood back to normal in some time
                
            if (AsthmaGlobals.currMood == "happy") {
                changeFishMood("happy");
                appendLog("User finished taking reading");
                Windows.Storage.ApplicationData.current.localSettings.values["changeMoodNormalTimeoutId"] =
                setTimeout(changeFishMood, AsthmaGlobals.fileConfig.config.animation.switchMoodSleepy, "sleepy");
                //TO DO: //3. disable the fish tank until next reading
            } else {
                initializeAnimation();
            }
        },
        
       
        adminLogin: function (eventInfo) { 
            WinJS.Navigation.navigate("/pages/adminPage/adminPage.html");
        },
        nextReadingAlert: function () {
            //enable user going into the flow when its time for new reading
            dismissAlert();
            changeFishMood("attentive");

           // Windows.Storage.ApplicationData.current.localSettings.values["bowlClickListener"] = 
            WinJS.Utilities.id("takeReading").listen("click", takeReading, false);
            WinJS.Utilities.id("takeReading").removeEventListener("click", teaseListener, false);
            // User doesnt take readings, so set a timer but clear it if you leave the page
          Windows.Storage.ApplicationData.current.localSettings.values["nextReadingNotTakenTimeoutId"] =
          setTimeout(readingNotTaken, 60000);// set next reading not taken timer
            //2. 
          initateDynamicAlert("scheduledReading", "Tap on the bowl to take a reading !!")
          animateNextReading();
            

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
          
            document.getElementById("trafficSignal").setAttribute("src", imageSrc);
            var timeElapsedLastDynamicReading = new Date().getTime() - AsthmaGlobals.airQualityConfig.airQualityMeter.lastDynamicReadingTakenAt;
            if (AsthmaGlobals.airQualityConfig.airQualityMeter.readingZone == AsthmaGlobals.airQualityConfig.airQualityMeter.takeDynamicReadingOn &&
                AsthmaGlobals.dynamicAlertDisplay == false &&
                AsthmaGlobals.airQualityConfig.airQualityMeter.isConnected == "true" &&
                AsthmaGlobals.canTakeReading == true) {
                initateDynamicAlert("dynamicReading", AsthmaGlobals.fileConfig.config.alertInfo.dynamicReadingAlertText);
                AsthmaGlobals.dynamicAlertDisplay = true;
            }
        }
    });
})();
// animate reading box
function animateNextReading() {

    $("#nextReadingCard").animate({ marginLeft: "+=50px"}, 1000, "swing");
    $("#nextReadingCard").animate({ marginLeft: "-=50px" }, 1000, "swing", animateNextReading);
}
// subsequent reading in X mins
function enableSubsequentReading() {
    setTimeout((function () {
        AsthmaGlobals.canTakeReading = true;
    }), AsthmaGlobals.fileConfig.config.alertInfo.intervalInTwoReadings)
}

//This func is called when you click on the bowl
function takeReading(eventInfo) {
    appendLog("User Started taking reading");
    AsthmaGlobals.dynamicAlertDisplay = false;
    takeDynamicReading(eventInfo);// 
}
// This func is for dynamic reading.
function takeDynamicReading(eventInfo) {
    changeFishMood("attentive");
    disableAllTimers();
    Windows.Storage.ApplicationData.current.localSettings.values["readingStartTime"] =
         new Date().toString();
    WinJS.Navigation.navigate("/pages/readingPage/readingPage.html");
}


function readingNotTaken  () {
    // stop alert that says take reading

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
    var content = "<div id='dynamicAlertBox' class='dynamicAlertBox' >"
    content += "<div class='dynamicContent'>";
    if (description != undefined) {
        content += description;
    } else {
        content += AsthmaGlobals.fileConfig.config.alertInfo.medicationAlertText;
    }
    content += "</div>";
    content += "<div class='buttonPanel'>";
    content += "<button id='dismiss' class='button-left'>Dismiss</button>";
    if(type != "scheduledReading"){
        if (type == "dynamicReading") {
            content += "<button id='done' class='button-right'>Take Reading</button>";
        } else {
            content += "<button id='done' class='button-right'>Done</button>";
        }
    }
    content += "</div>";
    content += "</div>";
    $("#dynamicInfoBox").append(content);
    if (type == "dynamicReading") {
        appendLog("User was shown dynamic reading alert");
        document.getElementById("done").onclick = takeDynamicReading;
    } else {
        appendLog("User was shown medication reminder");
        document.getElementById("done").onclick = alertActionComplete;
    }
    document.getElementById("dismiss").onclick = dismissAlert;
}

function dismissAlert() {
    changeFishMood("sleepy");
    appendLog("User Dismissed the alert");
    if (document.getElementById("dynamicAlertBox") != null || document.getElementById("dynamicAlertBox") != undefined)
    document.getElementById("dynamicAlertBox").removeNode(true);
}

function alertActionComplete() {
    changeFishMood("sleepy");
    appendLog("User clicked done on the alert");
    document.getElementById("dynamicAlertBox").removeNode(true);
}

function calculateNextmedicationTimeout() {
    getProperties();
    var medicationReadingTimeArray = AsthmaGlobals.fileConfig.config.alertInfo.medicationAlertTime;
    medicationReadingTimeArray.sort();
    var currHour = new Date().getHours();
    var currMins = new Date().getMinutes();
    var nextMedicationHour = null;
    for (var i = 0; i < medicationReadingTimeArray.length ; i++) {
        //get the largest hour from the array
        if (currHour < String(medicationReadingTimeArray[i]).substr(0, 2)) {
            nextMedicationHour = String(medicationReadingTimeArray[i]);
            break;
        } else if (currHour <= String(medicationReadingTimeArray[i]).substr(0, 2)
            && currMins < String(medicationReadingTimeArray[i]).substr(2, 2)) {
            //else if it is the same hour and min when compared to time right now
            nextMedicationHour = String(medicationReadingTimeArray[i]);
            break;
        }

    }
    var nextMedicationObj = new Date();// create the date obj of the time we next reading has to be taken
    // if no reading was found for the day then schedule it for tomorrow
    if (nextMedicationHour == null) {
        nextMedicationHour = String(medicationReadingTimeArray[0]);
        nextMedicationObj.setDate(nextMedicationObj.getDate() + 1);
    }

    nextMedicationObj.setHours(nextMedicationHour.substr(0, 2), nextMedicationHour.substr(2, 2), 00, 00);
    var displayHours = nextMedicationObj.getHours();
    if (displayHours == 0) {
        displayHours = "00";
    }
    var ampm = "am";
    if (displayHours >= 12) {
        if (displayHours > 12)
            displayHours -= 12;
        ampm = "pm";
    }
   // Windows.Storage.ApplicationData.current.localSettings.values["nextMedicationTimeText"] =
      //  displayHours + ":" + nextReadingHour.substr(2, 2) + " " + ampm;
    return nextMedicationObj.getTime() - new Date().getTime();
}

function teaseListener() {
    
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
/*
function getAdjacentReadingTimeouts() {
    var spiroReadingTimeArray = AsthmaGlobals.fileConfig.config.alertInfo.spiroReadingTime;
   
    var readingPrev = null;
    var hasEntered = false;

    
    for (var i = 0; i < spiroReadingTimeArray.length ; i++) {
        var todaysDate = new Date();
        if (hasEntered) {
            todaysDate.setDate(todaysDate.getDate()+1);
        }
        var getReadingHour = String(spiroReadingTimeArray[i]);
        todaysDate.setHours(getReadingHour.substr(0, 2), getReadingHour.substr(2, 2), 00, 00);
        if (readingPrev != null) {
            Windows.Storage.ApplicationData.current.localSettings.values["spiroReadingTimeout" + i] =
                                                           todaysDate.getTime() - readingPrev.getTime();
        } 
        readingPrev = todaysDate;

        if (hasEntered) {
            break;
        }
        if (i + 1 >= spiroReadingTimeArray.length) {
            i = -1;
            hasEntered = true;
        }
    }

}

function toggleNextReading() {
    if (document.getElementById("leftPanel").innerHTML.trim() != "") {
        document.getElementById("nextReadingCard").removeNode(true);
    }
    else {
        document.getElementById("leftPanel").appendChild(AsthmaGlobals.nextReadingCardMarkup);
    }
    document.getElementById('homepageMain').setAttribute("backgroundColor", "black");
}*/


