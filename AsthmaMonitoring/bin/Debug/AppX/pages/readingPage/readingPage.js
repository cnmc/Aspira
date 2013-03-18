// For an introduction to the Page Control template, see the following documentation:
// http://go.microsoft.com/fwlink/?LinkId=232511
(function () {
    "use strict";

    WinJS.UI.Pages.define("/pages/readingPage/readingPage.html", {
        // This function is called whenever a user navigates to this page. It
        // populates the page elements with the app's data.
        ready: function (element, options) {
            // TODO: Initialize the page here.
           // WinJS.Utilities.id("PEFValue").listen("click", this.currReadingMonitor, false);
         //   WinJS.Utilities.id("PEFValue").o
            document.getElementById('middleContent').onkeypress = this.currReadingMonitor.bind(this);
            var confirmBtnMarkup = document.createElement("button");
            confirmBtnMarkup.id = "confirmButton";
            confirmBtnMarkup.innerHTML = "Confirm";
            confirmBtnMarkup.onclick = this.changeScreenFev;
            document.getElementById('middleContent').appendChild(confirmBtnMarkup);
        },

        currReadingMonitor: function (eventInfo) {
            // validate input 
           // if (validateInput("PEFValue")) {
                
           // }

           
        }, 

        unload: function () {
            // TODO: Respond to navigations away from this page.
        },
        goHome: function () {
            WinJS.Navigation.navigate("/pages/home/home.html");
        },
        changeScreenFev: function(){
            document.getElementById("headingText").innerHTML = "FEV Reading";
            document.getElementById("confirmButton").removeNode(true);
            var confirmBtnFEVMarkup = document.createElement("button");
            confirmBtnFEVMarkup.id = "confirmButton";
            confirmBtnFEVMarkup.innerHTML = "Confirm1";
            confirmBtnFEVMarkup.onclick = function () {
                WinJS.Navigation.navigate("/pages/home/home.html");
            };
            document.getElementById('middleContent').appendChild(confirmBtnFEVMarkup);
        },
        
        updateLayout: function (element, viewState, lastViewState) {
            /// <param name="element" domElement="true" />

            // TODO: Respond to changes in viewState.
        }
    });

    function validateInput(feildName) {
        var currValueEntered = document.getElementById(feildName).value;
        var re = new RegExp("\d+(\.\d{1,2})?");
        var result = re.exec(currValueEntered);
        if (result == null)
            return false;
        else
            return true;
    }

   
    
})();
