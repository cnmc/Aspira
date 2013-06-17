-- This needs to change as we have the initial values in a property file now.
connect 'jdbc:derby:/Users/$$USER$$/derby_home/aspiradb';
insert into patient values ('$$USER$$','M','notes','bvtype',0,0,0,0,0);
insert into clinician values ('clinician1');
insert into patient_clinician values ('$$USER$$','clinician1');
insert into aqmonitor values ('$$AQM device id$$$','Dylos','DC1110','AQM','$$USER$$');
insert into spirometer values ('$$Spirometer device id$$$','MicroLife','PF100','Spirometer','$$USER$$');