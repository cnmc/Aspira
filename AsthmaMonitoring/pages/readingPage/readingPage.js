// For an introduction to the Page Control template, see the following documentation:
// http://go.microsoft.com/fwlink/?LinkId=232511
(function () {
    "use strict";

    WinJS.UI.Pages.define("/pages/readingPage/readingPage.html", {
        // This function is called whenever a user navigates to this page. It
        // populates the page elements with the app's data.
        ready: function (element, options) { 
            appendLog("debug", "Creating reading page", "ready function");
             WinJS.Utilities.id("leftHelpItem1").listen("click", this.navHelpPage, false);
            WinJS.Utilities.id("leftHelpItem2").listen("click", this.navHelpPage, false);
            WinJS.Utilities.id("rightHelpItem3").listen("click", this.navHelpPage, false);
            WinJS.Utilities.id("rightHelpItem4").listen("click", this.navHelpPage, false);
            WinJS.Utilities.id("rightHelpItem5").listen("click", this.navHelpPage, false);
            AsthmaGlobals.symptomQuestion = false;
            document.getElementById('PEFValue').onkeyup = this.currReadingMonitor.bind(this);
            if (AsthmaGlobals.tempPefVal != undefined &&
                AsthmaGlobals.tempPefVal != null) {
                document.getElementById('PEFValue').value = AsthmaGlobals.tempPefVal;
                var confirmBtnMarkup = document.createElement("button");
                confirmBtnMarkup.id = "confirmButton";
                confirmBtnMarkup.innerHTML = "Confirm";
                confirmBtnMarkup.onclick = this.changeScreenFev;
                document.getElementById('inputBoxDivision').appendChild(confirmBtnMarkup);
            }
            //Initialize the awesome stuff
            initializeAnimation();
            // We no longer display a local toast when in focus, so we don't need to hide
            //AsthmaGlobals.toastNotifier.hide(AsthmaGlobals.toast);
            appendLog("debug", "Created reading page", "ready function");

        },

        currReadingMonitor: function (eventInfo) {

            appendLog("debug", "Entering function", "currReadingMonitor function");
            if (validateInput(eventInfo.currentTarget.id)) {
                
                if (!document.getElementById('confirmButton')) {
                    var confirmBtnMarkup = document.createElement("button");
                    confirmBtnMarkup.id = "confirmButton";
                    confirmBtnMarkup.innerHTML = "Confirm";
                    if (document.getElementById("PEFValue")) {
                        confirmBtnMarkup.onclick = this.changeScreenFev;
                    }
                    else if (document.getElementById("FEVValue")) {
                        //it is in the fev sreen now and appending onclick event according to that
                        confirmBtnMarkup.onclick = function () {
                           // if (AsthmaGlobals.symptomQuestion == true) {
                                //change the mood of the fish to happy
                                AsthmaGlobals.currMood = "happy";
                                // setProperties();
                                Windows.Storage.ApplicationData.current.localSettings.values["FEVValCaptured"] =
                                    document.getElementById("FEVValue").value;
                                createSpirometerLog();
                                AsthmaGlobals.canTakeReading = false;
                                askSymptonsView();
                                document.getElementById("FEVValue").disabled = "disabled";
                                document.getElementById('confirmButton').removeNode(true);
                           // }
                        };
                    }
                    document.getElementById('inputBoxDivision').appendChild(confirmBtnMarkup);
                }

             } else {
                if (document.getElementById('confirmButton')) {
                    document.getElementById('confirmButton').removeNode(true);
                }
            }
            appendLog("debug", "leaving function", "currReadingMonitor function");
        },

        navHelpPage: function (eventInfo) {
            appendLog("debug", "Entering function", "navHelpPage function");
            var invokingHelpIcon = eventInfo.currentTarget.id;
            appendLog("navigation", "application", "Seek Help"); 
            appendLog("debug", "leaving function", "navHelpPage function (navigating to helpDisplay)");
            WinJS.Navigation.navigate("/pages/helpDisplay/helpDisplay.html", {"displayContent" : invokingHelpIcon});
        },

        unload: function () {
            // TODO: Respond to navigations away from this page.
        }, 
        changeScreenFev: function () {
            appendLog("debug", "Entering function", "changeScreenFev function");
            appendLog("navigation", "application", "PEF Complete");
            Windows.Storage.ApplicationData.current.localSettings.values["PEFValCaptured"] =
                document.getElementById("PEFValue").value;
            AsthmaGlobals.tempPefVal = document.getElementById("PEFValue").value;
                document.getElementById("headingText").innerHTML = "FEV Reading";
                document.getElementById("PEFValue").id = "FEVValue"
                document.getElementById("FEVValue").value = "";
                document.getElementById("confirmButton").removeNode(true); 
                appendLog("debug", "leaving function", "changeScreenFev function");
        },
        
        updateLayout: function (element, viewState, lastViewState) {
            /// <param name="element" domElement="true" />

            // TODO: Respond to changes in viewState.
        }
    });

    function validateInput(feildName) {
        appendLog("debug", "entering function", "validateInput function");
        var currValueEntered = document.getElementById(feildName).value.trim();
        appendLog("data entry", feildName+" " + "text box", currValueEntered);
        //appendLog("in " + feildName + " text box, user entered " + currValueEntered);

        var re = /^\d{3}\.\d{1}$/g;
        if (feildName == "PEFValue") {
            re = /^\d{1,3}$/g;
        } else {
            re = /^\d{1,3}\.\d{1,2}$/g;
        }
        var result = re.exec(currValueEntered);
        if (result == null) {
            return false;
        }
        else {
            return rangeCheck(feildName, currValueEntered); 
        }
        appendLog("debug", "leaving function", "validateInput function");
    }

    function rangeCheck(feildName, currValueEntered) {
        var upperBound;
        var lowerBound;
        if (feildName == "PEFValue") {
            upperBound = AsthmaGlobals.fileConfig.config.maxValues.PEFValue;
            lowerBound = AsthmaGlobals.fileConfig.config.minValues.PEFValue;
        }
        else if (feildName == "FEVValue") {
            upperBound = AsthmaGlobals.fileConfig.config.maxValues.FEVValue;
            lowerBound = AsthmaGlobals.fileConfig.config.minValues.FEVValue;
        }

        if (currValueEntered > upperBound || currValueEntered < lowerBound) {
            return false;
        }
        return true;
    }

    function askSymptonsView() {

        appendLog("debug", "entering function", "askSymptonsView function");
        var content = "<div id='symptomBox' class='dynamicAlertBox' >"
        content += "<div class='dynamicContent'>";
            content += AsthmaGlobals.fileConfig.config.alertInfo.symptomBoxText;
        content += "</div>";
            content += "<div class='buttonPanel'>";
            content += "<button id='yes' class='button-left'>Yes</button>";
                content += "<button id='no' class='button-right'>No</button>";

            content += "</div>";
        
        content += "</div>";
        $("#symptomBoxDiv").append(content);
        document.getElementById("yes").onclick = logPressYes;
        document.getElementById("no").onclick = logPressNo;
        appendLog("debug", "leaving function", "askSymptonsView function");
          
    }
    function logPressNo() {
        appendLog("debug", "entering function", "logPressNo function");
        document.getElementById("symptomBox").removeNode(true);
        AsthmaGlobals.symptomQuestion = true;
        AsthmaGlobals.hasSymptoms = false;
        appendLog("click", "sympton button", "No");
        Windows.Storage.ApplicationData.current.localSettings.values["symptomsReply"] = "No";
        appendLog("debug", "leaving function", "logPressNo function(going to page home.html)");
        WinJS.Navigation.navigate("/pages/home/home.html");
    }
    function logPressYes() {
        appendLog("debug", "entering function", "logPressYes function");
        AsthmaGlobals.symptomQuestion = true;
        AsthmaGlobals.hasSymptoms = true;
        document.getElementById("symptomBox").removeNode(true);
        appendLog("click", "sympton button", "Yes");
        Windows.Storage.ApplicationData.current.localSettings.values["symptomsReply"] = "Yes";
        appendLog("debug", "Leaving function", "logPressYes function");
        WinJS.Navigation.navigate("/pages/home/home.html(going to page home.html)");
    }

  
})();
