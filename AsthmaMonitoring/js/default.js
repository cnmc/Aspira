// For an introduction to the Navigation template, see the following documentation:
// http://go.microsoft.com/fwlink/?LinkId=232506
(function () {
    "use strict";

    WinJS.Binding.optimizeBindingReferences = true;

    var app = WinJS.Application;
    var activation = Windows.ApplicationModel.Activation;
    var nav = WinJS.Navigation;
    var displayPage = [
           { url: "/pages/home/home.html", title: "Welcome, standby, debriefing page" },
           { url: "/pages/readingPage/readingPage.html", title: "PEF reading Page" },
           { url: "/html/fevReading.html", title: "FEV reading Page" }
    ];
    WinJS.Namespace.define("AsthmaGlobals", {
        "fileConfig": null,
        "airQualityConfig": null,
        "logString":null,
        "dynamicAlertDisplay": false,
        "currMood": "sleepy",
        "canTakeReading": true,
        "teaseIndex": 0,
        "allTeaseImgArray": null,
        "changeMoodSleepyTimeoutId": null,
        "symptomQuestion": false,
        "medicationArray": null,
        "symptomsMedicationText": null,
        "medicationAlertText": null,
        "hasSymptoms": false,
        "idCount": 0,
        "tempPefVal":null,
        "toast": null,
        "toastNotifier": null,
        "notifiedDisconnection" : false
    });
   
    app.addEventListener("activated", function (eventObject) {
        debugLog("debug", "activated", "event listener");
        if (eventObject.detail.kind === activation.ActivationKind.launch) {
            // Unlike other windows applications our app would never be suspended so we do not check 
            //the previous state of the app : Vasu 
            eventObject.setPromise(WinJS.UI.processAll().then(function () {
                getProperties();
                
                return nav.navigate(displayPage[0].url);
            }));
        }
    });

    app.addEventListener("ready", function (eventObject) {
        generateScheduledToasts("Almost time to take a reading, come play with me!");
    });

    app.addEventListener("checkpoint", function (eventObject) {
        debugLog("debug", "suspended", "event listener");
    });
    app.addEventListener("resuming", function (eventObject) {
        debugLog("debug", "resuming", "event listener");
    });
    app.addEventListener("suspending", function (eventObject) {
        debugLog("debug", "suspending", "event listener");
    });
    app.oncheckpoint = function (eventObject) {
        debugLog("debug", "oncheckpoint", "handler???");
        // TODO: This application is about to be suspended. Save any state
        // that needs to persist across suspensions here. If you need to 
        // complete an asynchronous operation before your application is 
        // suspended, call eventObject.setPromise().

        //WE GOT A PROBLEM!!! Our app should never closes : Vasu
        app.sessionState.history = nav.history;
    };

    // This fires up debug logging, comment out in the Release version
    // WinJS.Utilities.startLog("debug");

    app.start();
})();
