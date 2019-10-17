
//*********************************************************************************************************************
//(1) If not using eclipse IDE, you can get the package compiled from windows command prompt
//    assuming all *.java files are in the current directory:
//    "C:\Program Files\java\jdk1.8.0_144\bin\javac.exe" -d .  console_restapi.java DocumentInfo.java GUI_Frame.java RestAPICaller.java RestAPIResponse.java SAP_BI_WebIntel_REST_helper.java Testing_Debugging_helper.java Tree_Known_Path_FolderID.java Tree_Unknown_Path_FolderID.java User_Input.java XML_helper.java
//
//(2) Create the executable jar file:
//    "C:\Program Files\java\jdk1.8.0_144\bin\jar.exe" cmf console_restapi.mf console_restapi.jar *.java console_restapi
//
//(3) Check the contents of the new jar file:
//    "C:\Program Files\java\jdk1.8.0_144\bin\jar.exe" tf  console_restapi.jar
//
//(4) Run the executable jar file:
//    "C:\Program Files\java\jdk1.8.0_144\bin\java.exe" -jar console_restapi.jar
//
//(5) If all you have is this .jar file and you do not have the source code .java files, you can extract the contents of the jar file to get the source code:
//    "C:\Program Files\java\jdk1.8.0_144\bin\jar.exe"  xvf  console_restapi.jar
//*********************************************************************************************************************


//**********************************************************************************************************************
//(1)
//October 1, 2019
// System: myhost:6400
// User Name:
// Password:
// Authentication: Enterprise
//
//Help ==> About ==>
//
//copyright 2010 - 2014 SAP AG
//SAP BusinessObjects BI Platform 4.1 Support Pack 3
//Version: 14.1.3.1257
//
//https://help.sap.com/viewer/d6bd74f8532c4e978419c7300bda43f8/4.1.11/en-US/fb3c2efc71454d80872cd3029e523dd5.html
//https://help.sap.com/viewer/d6bd74f8532c4e978419c7300bda43f8/4.1.11/en-US/fb3c2efc71454d80872cd3029e523dd5.html
//**********************************************************************************************************************

//*************************************************************************************************************************
//(2) ok closest version i can find docs online is 4.1SP11 but above says 4.1SP3 .....
//    shoot maybe online docs are gone cuz too old ..... may have to stop using web and search for local documentation
//    this is from 4.1SP11 online docs:
//
//    Default Base URLs
//    To use the RESTful web services for Web Intelligence and the BI Semantic Layer, 
//    you must know the protocol, server name, port number and path of the service that listens to the HTTP requests. 
//    You configure the default base URL in the CMC from Applications  REST Web Service  Properties  Access URL. 
//    See chapter 12 of the Business Intelligence Platform Administrator Guide for more information.
//
//    Basic installations of the BI platform that are installed on a single server use the default base URLs:
//
//    Web Service SDK	URL
//    BI Semantic Layer ==> http://<server_name>:6405/biprws/sl/v1
//
//    Web Intelligence  ==> http://<server_name>:6405/biprws/raylight/v1
//*************************************************************************************************************************

//*********************************************************************************************************************
//(3) October 1, 2019
//Shoot started coding for version 4.2 ...... maybe the restapi stuff is slightly different, so far not working :(
//Using the RESTful Web Service SDKs ==> To Log on to the BI platform
//https://help.sap.com/viewer/58f583a7643e48cf944cf554eb961f5b/4.2/en-US/920c29af0fe24ba4b7f1d54b042b546e.html
//https://help.sap.com/viewer/58f583a7643e48cf944cf554eb961f5b/4.2/en-US/920c29af0fe24ba4b7f1d54b042b546e.html
//
//(4) Ok i got a token back ..... i got authentication working
//*********************************************************************************************************************
