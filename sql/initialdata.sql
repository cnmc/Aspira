-- This needs to change as we have the initial values in a property file now.
connect 'jdbc:derby:/Users/kevinagary/CNMC/projects/AsthmaMobile/db/derby_home/aspiradb';
insert into patient values ('patient1','M','notes','bvtype',0,0,0,0,0);
insert into clinician values ('clinician1');
insert into patient_clinician values ('patient1','clinician1');
insert into aqmonitor values ('aqm1','Dylos','DC1110','AQM','patient1');
insert into spirometer values ('spiro1','MicroLife','PF100','Spirometer','patient1');
