(function () {
    "use strict";

    WinJS.UI.Pages.define("/pages/home/home.html", {
        // This function is called whenever a user navigates to this page. It
        // populates the page elements with the app's data.
        
        ready: function (element, options) {  
            WinJS.Utilities.id("takeReading").listen("click", this.takeReading, false);
            WinJS.Utilities.id("adminLogin").listen("click", this.adminLogin, false);
            Windows.Storage.ApplicationData.current.localSettings.values["nextReadingTimeoutId"] = setTimeout(
           this.nextReadingAlert, calculateNextReadingTimeout());// set next reading timer
            document.getElementById("nextReadingContent").innerHTML =
                 Windows.Storage.ApplicationData.current.localSettings.values["nextReadingTimeText"];
            AsthmaGlobals.intervalOn = false;//declaring a global var
            Windows.Storage.ApplicationData.current.localSettings.values["checkAirQualityTimeoutId"] = setInterval(
              this.checkAirQuality, 3000);//check air qulity regularly
            //Now Animating ;D

            //1. if user just completed the reading then show the fish happy but 
                //2. change mood back to normal in some time
                
            if (AsthmaGlobals.fileConfig.config.animation.currMood == "happy") {
                changeFishMood("happy");
                Windows.Storage.ApplicationData.current.localSettings.values["changeMoodNormalTimeoutId"] =
                setTimeout(changeFishMood, 60000);
                //TO DO: //3. disable the fish tank until next reading
            } else {
                initializeAnimation();
            }
        },
        
        takeReading: function (eventInfo) { 
            disableAllTimers();
            Windows.Storage.ApplicationData.current.localSettings.values["readingStartTime"] =
                 new Date().toString();
            WinJS.Navigation.navigate("/pages/readingPage/readingPage.html");
        },
        adminLogin: function (eventInfo) { 
            WinJS.Navigation.navigate("/pages/adminPage/adminPage.html");
        },
        nextReadingAlert: function () {
            // User doesnt take readings, so set a timer but clear it if you leave the page
          Windows.Storage.ApplicationData.current.localSettings.values["nextReadingNotTakenTimeoutId"] =
          setTimeout(readingNotTaken, 60000);// set next reading not taken timer
            //2. 
            AsthmaGlobals.nextReadingCardMarkup = document.getElementById("leftPanel").children;
            Windows.Storage.ApplicationData.current.localSettings.values["nextReadingIntervalId"]=
            setInterval((function () {
                if (document.getElementById("nextReadingCard").style.display == "none") {
                    document.getElementById("nextReadingCard").style.display="block";
                }
                else if (document.getElementById("nextReadingCard").style.display == "block" ||
                    document.getElementById("nextReadingCard").style.display == "") { 
                    document.getElementById("nextReadingCard").style.display= "none";
                }
            }), 1000);

        },
       
        checkAirQuality: function () {
            getProperties();// update config from a fresh config file
            
            if (AsthmaGlobals.fileConfig.config.alertInfo.airQualityStatus != "normal" && AsthmaGlobals.intervalOn == false) {
                Windows.Storage.ApplicationData.current.localSettings.values["airQualityAlertInterval"] = setInterval((function () {
                    if (document.getElementById("rightItem").style.display == "none") {
                        document.getElementById("rightItem").style.display = "block";
                    }
                    else if (document.getElementById("rightItem").style.display == "block" ||
                        document.getElementById("rightItem").style.display == "") {
                        document.getElementById("rightItem").style.display = "none";
                    }
                }), 700);
                AsthmaGlobals.intervalOn = true;
            }
            else if (AsthmaGlobals.fileConfig.config.alertInfo.airQualityStatus == "normal" && AsthmaGlobals.intervalOn == true) {
                clearInterval(Windows.Storage.ApplicationData.current.localSettings.values["airQualityAlertInterval"]);
                document.getElementById("rightItem").style.display = "block";
                AsthmaGlobals.intervalOn = false;
            }
        }
    });
})();

function readingNotTaken  () {
    // stop alert that says take reading
    clearInterval(Windows.Storage.ApplicationData.current.localSettings.values["nextReadingIntervalId"]);
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
     changeFishMood("sad");
     
}
function changeFishMood(mood) {
    if (mood != undefined) {
        AsthmaGlobals.fileConfig.config.animation.currMood = mood;
    } else {
        AsthmaGlobals.fileConfig.config.animation.currMood = "normal";
    }
    setProperties();
    initializeAnimation();

}
function disableAllTimers() {
    clearInterval(Windows.Storage.ApplicationData.current.localSettings.values["airQualityAlertInterval"]);
    clearInterval(Windows.Storage.ApplicationData.current.localSettings.values["checkAirQualityTimeoutId"]);
    clearTimeout(Windows.Storage.ApplicationData.current.localSettings.values["nextReadingTimeoutId"]);
    clearInterval(Windows.Storage.ApplicationData.current.localSettings.values["nextReadingIntervalId"]);
    clearTimeout(Windows.Storage.ApplicationData.current.localSettings.values["nextReadingNotTakenTimeoutId"]);
    clearTimeout(Windows.Storage.ApplicationData.current.localSettings.values["changeMoodNormalTimeoutId"]);
}
function calculateNextReadingTimeout() {
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


