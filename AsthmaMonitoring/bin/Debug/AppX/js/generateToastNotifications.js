function generateToast() {
    var notifications = Windows.UI.Notifications;
    var template = notifications.ToastTemplateType.toastImageAndText01;
    var toastXml = notifications.ToastNotificationManager.getTemplateContent(template);
    var toastTextElements = toastXml.getElementsByTagName("text");
    toastTextElements[0].appendChild(toastXml.createTextNode("Hello World!"));
    var toastImageElements = toastXml.getElementsByTagName("image");
    toastImageElements[0].setAttribute("src", "ms-appx:///images/smalllogo.png");
    toastImageElements[0].setAttribute("alt", "Aspira");
    var toastNode = toastXml.selectSingleNode("/toast");
    toastNode.setAttribute("duration", "long");
    toastXml.selectSingleNode("/toast").setAttribute("launch", '{"type":"toast","param1":"12345","param2":"67890"}');
    var toast = new notifications.ToastNotification(toastXml);
    var toastNotifier = notifications.ToastNotificationManager.createToastNotifier();
    toastNotifier.show(toast);
}