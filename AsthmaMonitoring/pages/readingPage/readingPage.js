// For an introduction to the Page Control template, see the following documentation:
// http://go.microsoft.com/fwlink/?LinkId=232511
(function () {
    "use strict";

    WinJS.UI.Pages.define("/pages/readingPage/readingPage.html", {
        // This function is called whenever a user navigates to this page. It
        // populates the page elements with the app's data.
        ready: function (element, options) {
             WinJS.Utilities.id("leftHelpItem1").listen("click", this.navHelpPage, false);
            WinJS.Utilities.id("leftHelpItem2").listen("click", this.navHelpPage, false);
            WinJS.Utilities.id("rightHelpItem3").listen("click", this.navHelpPage, false);
            WinJS.Utilities.id("rightHelpItem4").listen("click", this.navHelpPage, false);
            WinJS.Utilities.id("rightHelpItem5").listen("click", this.navHelpPage, false);
            AsthmaGlobals.symptomQuestion = false;
            document.getElementById('PEFValue').onkeyup = this.currReadingMonitor.bind(this);
            if (Windows.Storage.ApplicationData.current.localSettings.values["PEFValCaptured"] != undefined &&
                 Windows.Storage.ApplicationData.current.localSettings.values["PEFValCaptured"] != null) {
                document.getElementById('PEFValue').value = Windows.Storage.ApplicationData.current.localSettings.values["PEFValCaptured"];
                var confirmBtnMarkup = document.createElement("button");
                confirmBtnMarkup.id = "confirmButton";
                confirmBtnMarkup.innerHTML = "Confirm";
                confirmBtnMarkup.onclick = this.changeScreenFev;
                document.getElementById('inputBoxDivision').appendChild(confirmBtnMarkup);
            }
            //Initialize the awesome stuff
            initializeAnimation();

        },

        currReadingMonitor: function (eventInfo) {
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
        },

        navHelpPage: function (eventInfo) {
            var invokingHelpIcon = eventInfo.currentTarget.id;
            appendLog("navigation", "application", "Seek Help");
            WinJS.Navigation.navigate("/pages/helpDisplay/helpDisplay.html", {"displayContent" : invokingHelpIcon});
        },

        unload: function () {
            // TODO: Respond to navigations away from this page.
        }, 
        changeScreenFev: function () {
            appendLog("navigation", "application", "PEF Complete");
            Windows.Storage.ApplicationData.current.localSettings.values["PEFValCaptured"] =
                document.getElementById("PEFValue").value;

                document.getElementById("headingText").innerHTML = "FEV Reading";
                document.getElementById("PEFValue").id = "FEVValue"
                document.getElementById("FEVValue").value = "";
                document.getElementById("confirmButton").removeNode(true);
               
        },
        
        updateLayout: function (element, viewState, lastViewState) {
            /// <param name="element" domElement="true" />

            // TODO: Respond to changes in viewState.
        }
    });

    function validateInput(feildName) {
        var currValueEntered = document.getElementById(feildName).value.trim();
        appendLog("data entry", feildName+" " + "text box", currValueEntered);
        //appendLog("in " + feildName + " text box, user entered " + currValueEntered);
        var re = /^\d{3}\.\d{1}$/g;
        var result = re.exec(currValueEntered);
        if (result == null) {
            return false;
        }
        else {
            return rangeCheck(feildName, currValueEntered); 
        }
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
          
    }
    function logPressNo() {
        document.getElementById("symptomBox").removeNode(true);
        AsthmaGlobals.symptomQuestion = true;
        AsthmaGlobals.hasSymptoms = false;
        appendLog("click", "sympton button", "No");
        Windows.Storage.ApplicationData.current.localSettings.values["symptomsReply"] = "No";
        WinJS.Navigation.navigate("/pages/home/home.html");
    }
    function logPressYes() {
        AsthmaGlobals.symptomQuestion = true;
        AsthmaGlobals.hasSymptoms = true;
        document.getElementById("symptomBox").removeNode(true);
        appendLog("click", "sympton button", "Yes");
        Windows.Storage.ApplicationData.current.localSettings.values["symptomsReply"] = "Yes";
        WinJS.Navigation.navigate("/pages/home/home.html");
    }

  
})();
