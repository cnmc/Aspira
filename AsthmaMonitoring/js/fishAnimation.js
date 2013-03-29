//Vasu Gupta @ASU
// cool functions
function fishFloat() {
    $("#fishImage").animate({ top: "+=40px" }, 3000, "swing");
    $("#fishImage").animate({ top: "-=40px" }, 3000, "swing", fishFloat);
};


function createAnimationDiv() {
    var currentStage = calculateImageToShow();
    if (currentStage == 0 || currentStage == undefined || currentStage > 
        AsthmaGlobals.fileConfig.config.animation.totalStages) {
        currentStage = 1;
    } 
    var mood = "normal";
    if ( Windows.Storage.ApplicationData.current.localSettings.values["currMood"] != undefined){
        mood =  Windows.Storage.ApplicationData.current.localSettings.values["currMood"];
    }
    var imgSrc= "/images/fishAnimation/fish_"+mood+"_stage_"+currentStage+".png";
    var content = " <div class='animationMainContainer' id='animationMainContainer'>";
    content += "<div class='fishImage' id='fishImage'>";
    content += "<img id='fish_normal_stage_1' src='" + imgSrc + "'  alt='Fish' />";
    content += "</div>";
    content += "<div class='bowlImage'>";
    content += "<img src='/images/fishAnimation/fishBowl.png' alt='bowl' />";
    content += "</div>";
    content += "</div>";
    calculateImageToShow();
    $("#takeReading").append(content);
}
function initializeAnimation() {
    if (document.getElementById("animationMainContainer")) {
        document.getElementById("animationMainContainer").removeNode(true);
    }
    createAnimationDiv();
    fishFloat()
}
function changeFishMood(mood) {
    if (mood != undefined) {
        Windows.Storage.ApplicationData.current.localSettings.values["currMood"] = mood;
    } else {
        Windows.Storage.ApplicationData.current.localSettings.values["currMood"] = "normal";
    }
    setProperties();
    initializeAnimation();

}
//boring stuff
function calculateImageToShow() {
    var todaysDate = new Date();
    var startDate = new Date(AsthmaGlobals.fileConfig.config.animation.startDateMilliSec); 
    var totalDays = AsthmaGlobals.fileConfig.config.animation.totalDays;
    var totalStages = AsthmaGlobals.fileConfig.config.animation.totalStages;
    var daysElasped = (todaysDate - startDate) / (1000 * 60 * 60 * 24);
    var nextStageDayCount =Math.round( totalDays / totalStages);
    for (var i = 0; i <= totalDays; i += nextStageDayCount) {
        if (daysElasped <= i) {
            return i ;
        }
    }

}
//mood is sad if the second reading is also missed and remains sad even after
//the three or more readings have been missed.
//mood is happy for 5 mins after you take readings.
// mood is normal rest of the time.