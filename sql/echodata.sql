-- Script to select * all tuples from DB, to be run from ij tool
connect 'jdbc:derby:/Users/kevinagary/CNMC/projects/AsthmaMobile/db/derby_home/aspiradb';
select * from "APP"."PARTICLEREADING";
select * from "APP"."SPIROMETERREADING";
select * from "APP"."UIEVENT";
select * from "APP"."PATIENT_CLINICIAN";
select * from "APP"."CLINICIAN";
select * from "APP"."SPIROMETER";
select * from "APP"."AQMONITOR";
select * from "APP"."PATIENT";
disconnect;
exit;
