The DMP branch is intended for the Data Management Platform component, backend services.
This includes:

1. DAO interfaces and implementations to the datasource
2. RESTful interfaces to the DMP platform
3. Parsing and import routines for spirometer, air quality, and user interaction logs
4. Export routines to Excel and other formats the clinicians may desire

Note this component does not have a GUI of any kind. The build script will build one
or more jarfiles that will be deployed alongside the clinician/analyst web application,
plus a WAR for deploying the RESTful services.
