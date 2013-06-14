function setNotificationXml(notifications, displayText) {
    var template = notifications.ToastTemplateType.toastImageAndText01;
    var toastXml = notifications.ToastNotificationManager.getTemplateContent(template);
    if (toastXml != null) {
        var toastTextElements = toastXml.getElementsByTagName("text");
        //if (AsthmaGlobals.toastNotifier != null && AsthmaGlobals.toast != null) {
        //    AsthmaGlobals.toastNotifier.hide(AsthmaGlobals.toast);
        //}
        toastTextElements[0].appendChild(toastXml.createTextNode(displayText));
        var toastImageElements = toastXml.getElementsByTagName("image");
        toastImageElements[0].setAttribute("src", "ms-appx:///images/smalllogo.png");
        toastImageElements[0].setAttribute("alt", "Aspira");
        var toastNode = toastXml.selectSingleNode("/toast");
        toastNode.setAttribute("duration", "long");
    }
    return toastXml;
}

// this really should only be called by the generate scheduled method below
function calculateScheduledNotifications() {
    debugLog("debug", "entering function", "calculateScheduledNotifications");
    getProperties();
    var totalDays = AsthmaGlobals.fileConfig.config.animation.totalDays;
    var spiroReadingTimeArray = AsthmaGlobals.fileConfig.config.alertInfo.spiroReadingTime;
    spiroReadingTimeArray.sort();
    var nextReadingHour;
    var nextReadingMins;
    var nextReadingDate;
    var futureReadingDate = new Date(new Date().getTime() + 60000 * 5); // has to be at least 3 minutes out from now, see below
    var notifyTimes = new Array();
    var count = 0;

    for (var j = 0; j < 2; j++) {
        // Scheduled toasts are maxed at 4096 or you get an exception, allowing 1/2 max
        for (var i = 0; i < spiroReadingTimeArray.length && count < 2048; i++) {
            //get the largest hour from the array
            nextReadingHour = spiroReadingTimeArray[i].substr(0, 2);
            nextReadingMins = spiroReadingTimeArray[i].substr(2, 2);
            nextReadingDate = new Date();
            nextReadingDate.setHours(nextReadingHour);
            nextReadingDate.setMinutes(nextReadingMins);
            nextReadingDate.setDate(nextReadingDate.getDate() + j);
            if (nextReadingDate > futureReadingDate) {
                notifyTimes[count] = nextReadingDate;
                count++;
            }
        }
    }
    return notifyTimes;
}

// This function is meant to be called once on initialization to setup all of the
// scheduled toasts for the lifetime of the clinical trial.
function generateScheduledToasts(displayText) {
    var notifications = Windows.UI.Notifications;
    var toastXML = setNotificationXml(notifications, displayText);
    var toastTimes = calculateScheduledNotifications();

    AsthmaGlobals.toastNotifier = notifications.ToastNotificationManager.createToastNotifier();
    if (toastTimes != undefined && toastTimes != null) {
        // Remove any scheduled toast with the same id. from Microsoft sample code KG
        var scheduled = AsthmaGlobals.toastNotifier.getScheduledToastNotifications();
        for (var i = 0, len = scheduled.length; i < len; i++) {
            // The itemId value is the unique ScheduledTileNotification.Id assigned to the 
            // notification when it was created.
            if (scheduled[i].id === "aspiratoast_" + i) {
                debugLog("debug", "Removed aspira_toast scheduled toast", "generateToast");
                AsthmaGlobals.toastNotifier.removeFromSchedule(scheduled[i]);
            }
        }
        for (var j = 0, len = toastTimes.length; j < len; j++) {
            // Have a single scheduled notification go off 3 minutes before the actual reading is due, 2 minute reminder below
            var nextDate = new Date(toastTimes[j].getTime() - 60000 * 3);
            // it has to be in the future
            var toast = new notifications.ScheduledToastNotification(toastXML, nextDate, 120000, 1); // repeat once!
            toast.id = "aspiratoast_" + j;
            AsthmaGlobals.toastNotifier.addToSchedule(toast);
        }
    }
}

// This generates a local toast unless the app is onscreen
function generateToast(displayText) {
    if (document.hidden) {
        var notifications = Windows.UI.Notifications;
        var toastXml = setNotificationXml(notifications, displayText);

        if (toastXml != undefined && toastXml != null) {
            AsthmaGlobals.toast = new notifications.ToastNotification(toastXml);
            AsthmaGlobals.toastNotifier.show(AsthmaGlobals.toast);
        }
    }
}