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

            document.getElementById('PEFValue').onkeyup = this.currReadingMonitor.bind(this);
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
                            //change the mood of the fish to happy
                           AsthmaGlobals.currMood = "happy";
                           // setProperties();
                            Windows.Storage.ApplicationData.current.localSettings.values["FEVValCaptured"] =
                                document.getElementById("FEVValue").value;
                            createSpirometerLog();
                            AsthmaGlobals.canTakeReading = false;
                            WinJS.Navigation.navigate("/pages/home/home.html");
                        };
                    }
                    document.getElementById('middleContent').appendChild(confirmBtnMarkup);
                }

             } else {
                if (document.getElementById('confirmButton')) {
                    document.getElementById('confirmButton').removeNode(true);
                }
            }
        },

        navHelpPage: function (eventInfo) {
            var invokingHelpIcon = eventInfo.currentTarget.id;
            WinJS.Navigation.navigate("/pages/helpDisplay/helpDisplay.html", {"displayContent" : invokingHelpIcon});
        },

        unload: function () {
            // TODO: Respond to navigations away from this page.
        }, 
        changeScreenFev: function () {
            appendLog("User completed PEF reading");
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
        appendLog("in " + feildName + " text box, user entered " + currValueEntered);
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

  
})();
