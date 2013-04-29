function generateToast(diplayText) {
    var notifications = Windows.UI.Notifications;
    var template = notifications.ToastTemplateType.toastImageAndText01;
    var toastXml = notifications.ToastNotificationManager.getTemplateContent(template);
    var toastTextElements = toastXml.getElementsByTagName("text");
    if (AsthmaGlobals.toastNotifier != null && AsthmaGlobals.toast != null) {
        AsthmaGlobals.toastNotifier.hide(AsthmaGlobals.toast);
    }
    toastTextElements[0].appendChild(toastXml.createTextNode(diplayText));
    var toastImageElements = toastXml.getElementsByTagName("image");
    toastImageElements[0].setAttribute("src", "ms-appx:///images/smalllogo.png");
    toastImageElements[0].setAttribute("alt", "Aspira");
    var toastNode = toastXml.selectSingleNode("/toast");
    toastNode.setAttribute("duration", "long");
    //toastXml.selectSingleNode("/toast").setAttribute("launch", '{"type":"toast","param1":"12345","param2":"67890"}');
    AsthmaGlobals.toast = new notifications.ToastNotification(toastXml);
    AsthmaGlobals.toastNotifier = notifications.ToastNotificationManager.createToastNotifier();
   
    AsthmaGlobals.toastNotifier.show(AsthmaGlobals.toast);
}