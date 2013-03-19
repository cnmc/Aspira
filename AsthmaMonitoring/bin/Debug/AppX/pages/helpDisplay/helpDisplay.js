// For an introduction to the Page Control template, see the following documentation:
// http://go.microsoft.com/fwlink/?LinkId=232511
(function () {
    "use strict";

    WinJS.UI.Pages.define("/pages/helpDisplay/helpDisplay.html", {
        // This function is called whenever a user navigates to this page. It
        // populates the page elements with the app's data.
        ready: function (element, options) { 
            var invokingHelpIcon = options.displayContent;

            var contentVidDisplayMarkup = document.createElement("video");
            contentVidDisplayMarkup.width = "650";
            contentVidDisplayMarkup.autoplay = "autoplay";

            var contentImgDisplayMarkup = document.createElement("img");
            switch (invokingHelpIcon) {
             
                case "leftHelpItem1":
                    contentVidDisplayMarkup.src = "/video/steps7_8.mp4";
                    document.getElementById('helpDisplayContent').appendChild(contentVidDisplayMarkup);
                    break;
                case "leftHelpItem2":
                    contentImgDisplayMarkup.src = "/images/second.jpg";
                    document.getElementById('helpDisplayContent').appendChild(contentImgDisplayMarkup);
                    break;
                case "rightHelpItem3":
                    contentVidDisplayMarkup.src = "/video/steps3_6.mp4";
                    document.getElementById('helpDisplayContent').appendChild(contentVidDisplayMarkup);
                    break;
                case "rightHelpItem4":
                    var contentDivDisplayMarkup = document.createElement("div");
                    contentDivDisplayMarkup.className = "spiroHelpSteps";
                    contentDivDisplayMarkup.innerHTML = this.createHelpItemSteps();
                    document.getElementById('helpDisplayContent').appendChild(contentDivDisplayMarkup);
                    break;
            }
        },

        createHelpItemSteps: function(){
            var content = "<img src='/images/aspira_spiro.jpg' />";
            content += "<img src='/images/holdSpiroBothHands.jpg' />";
            content += "<ol><li>Turn on the Spirometer (the button right under 'Microlife')</li>";
            content += "<li>Hold the spirometer with both hands up close to your mouth</li>";
            content += "<li>Inhale deeply and hold for a moment</li>";
            content += "<li>Press the spirometer at the blow tube to your lips</li>";
            content += "<li>Blow into the tube as fast and as hard as you can</li>";
            content += "<li>If the device is able to record its measurements, you will hear a long beep</li>";
            content += "<li>When it is ready to record another measurement you will hear 2 short beeps</li>";
            content += "<li>If you do not hear a long beep, just try again when you hear the short beeps</li>";
            content += "<li>Repeat steps 2-6 until you have recorded 3 good measurements</li></ol>";
            return content;
        },
        unload: function () {
            // TODO: Respond to navigations away from this page.
        },

        updateLayout: function (element, viewState, lastViewState) {
            /// <param name="element" domElement="true" />

            // TODO: Respond to changes in viewState.
        }
    });

})();
