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
    //create tease array
    var allTeaseImgSrc = new Array(); 
    allTeaseImgSrc[0] = "/images/fishAnimation/" + currentStage + "attentive.png";
    allTeaseImgSrc[1] = "/images/fishAnimation/" + currentStage + "scared.png";
    allTeaseImgSrc[2] = "/images/fishAnimation/" + currentStage + "puffed.png";
    AsthmaGlobals.allTeaseImgArray = allTeaseImgSrc;
    var mood = "sleepy";
    if (AsthmaGlobals.currMood != undefined){
        mood = AsthmaGlobals.currMood;
    }
    var imgSrc= "/images/fishAnimation/"+currentStage+mood+".png";
    var content = " <div class='animationMainContainer' id='animationMainContainer'>";
    content += "<div class='fishImage' id='fishImage'>";
    content += "<img id='fishImageID' src='" + imgSrc + "'  alt='Fish' />";
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
       AsthmaGlobals.currMood = mood;
    }
    //else if (mood == "sleepy") {
    //    AsthmaGlobals.changeMoodSleepyTimeoutId = null;
    //}
    else {
       AsthmaGlobals.currMood = "sleepy";
    }
    //setProperties();
    initializeAnimation();

}
//boring stuff
function calculateImageToShow() {
    var todaysDate = new Date();
    var startDate = new Date(AsthmaGlobals.fileConfig.config.animation.startDateMilliSec); 
    var totalDays = AsthmaGlobals.fileConfig.config.animation.totalDays;
    var totalStages = AsthmaGlobals.fileConfig.config.animation.totalStages;
    var daysElasped = (todaysDate - startDate) / (1000 * 60 * 60 * 24);
    var nextStageDayCount = Math.round(totalDays / totalStages);
    var whichStage = 0;
    for (var i = 0; i <= totalDays; i += nextStageDayCount) {
        if (daysElasped <= i) {
            return whichStage ;
        }
        whichStage++;
    }

}
//mood is sad if the second reading is also missed and remains sad even after
//the three or more readings have been missed.
//mood is happy for 5 mins after you take readings.
// mood is normal rest of the time.