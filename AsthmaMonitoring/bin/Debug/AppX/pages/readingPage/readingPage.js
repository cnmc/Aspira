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

            document.getElementById('middleContent').onkeypress = this.currReadingMonitor.bind(this);
            var confirmBtnMarkup = document.createElement("button");
            confirmBtnMarkup.id = "confirmButton";
            confirmBtnMarkup.innerHTML = "Confirm";
            confirmBtnMarkup.onclick = this.changeScreenFev;
            document.getElementById('middleContent').appendChild(confirmBtnMarkup);
        },

        currReadingMonitor: function (eventInfo) {
            // TODO: do validation stuff for readings here
        },

        navHelpPage: function (eventInfo) {
            var invokingHelpIcon = eventInfo.currentTarget.id;
            WinJS.Navigation.navigate("/pages/helpDisplay/helpDisplay.html", {"displayContent" : invokingHelpIcon});
        },

        unload: function () {
            // TODO: Respond to navigations away from this page.
        }, 
        changeScreenFev: function(){
            document.getElementById("headingText").innerHTML = "FEV Reading";
            document.getElementById("confirmButton").removeNode(true);
            var confirmBtnFEVMarkup = document.createElement("button");
            confirmBtnFEVMarkup.id = "confirmButton";
            confirmBtnFEVMarkup.innerHTML = "Confirm";
            confirmBtnFEVMarkup.onclick = function () {WinJS.Navigation.navigate("/pages/home/home.html");};
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
