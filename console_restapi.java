
//*****************************************************************************************************************************************
//If not using eclipse IDE, you can compile with:
//C:\Program Files\java\jdk1.8.0_144\bin\javac.exe  console_restapi.java RestAPICaller.java RestAPIResponse.java XML_helper.java
//
//Get the package compiled:
//C:\Program Files\java\jdk1.8.0_144\bin\javac.exe -d .  console_restapi.java RestAPICaller.java RestAPIResponse.java XML_helper.java
//
//and run with:
//C:\Program Files\java\jdk1.8.0_144\bin\java.exe   console_restapi.console_restapi
//*****************************************************************************************************************************************

//**********************************************************************************************************************
//(1)
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

package console_restapi;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;



public class console_restapi {
	
	
	
	public static void main(String[] args) throws Exception {

		if (args.length < 3)
		{
			System.out.println("Usage:   console_restapi.console_restapi  (username) (password) (PROTOCOL_HOST_PORT)");
			System.out.println("Example: console_restapi.console_restapi  myusername  mypassword  http://myhost:myport/");
			System.out.println("Example: console_restapi.console_restapi  myusername  mypassword  http://myhost:6405/");
			return;
		}
		
		String myusername         = args[0];
		String mypassword         = args[1];
		String PROTOCOL_HOST_PORT = args[2];
		
		RestAPICaller rest = new RestAPICaller();

		//String gittest = rest.sendGet_https("https://github.com");
		//System.out.println(gittest);
		
		RestAPIResponse myresponse = rest.sendGet(PROTOCOL_HOST_PORT + "biprws/raylight/v1/about");
		if (myresponse.responseCode == 200)
			System.out.println(myresponse.response);
		
		
		myresponse = rest.sendGet(PROTOCOL_HOST_PORT + "biprws/logon/long");
		if (myresponse.responseCode == 200)
			System.out.println(myresponse.response);
		
		//xml helper
		XML_helper auth_help = new XML_helper();
		auth_help.parse(myresponse.response);
		auth_help.set_attr("userName", myusername);
		auth_help.set_attr("password", mypassword);
		String authxml = auth_help.transform_to_String();
		
		System.out.println();
		System.out.println(authxml);
		
		
		//****** Try to authenticate and get token **********
		myresponse = rest.sendPost_XML(PROTOCOL_HOST_PORT + "biprws/logon/long", authxml);
		if (myresponse.responseCode == 200)
		{
			System.out.println(myresponse.response);
		
			//xml helper
			XML_helper token_help = new XML_helper();
			token_help.parse(myresponse.response);
			String token = token_help.get_attr("logonToken");
			
			System.out.println("token = " + token);
			rest.store_token(token);
		}
		
		//****************************************************************************
		//sbo41_bip_rest_ws_en.pdf
		//
		//Business Intelligence Platform RESTful Web Service
		//
		//The Business Intelligence platform RESTful web service SDK lets you access the BI platform using the
		//HTTP protocol. You can use this SDK to log on to the BI platform, navigate the BI platform repository,
		//access resources, and perform basic resource scheduling. You can access this SDK by writing
		//applications that use any programming language that supports the HTTP protocol, or by using any tool
		//that supports making HTTP requests. Both XML and JSON (JavaScript Object Notation) request and
		//response formats are supported.
		//
		//Use the RESTful web services SDK under the following conditions:
		//	• You want to access BI platform repository objects or perform basic scheduling.
		//	• You want to use a programming language that is not supported by other BI platform SDKs.
		//	• You do not want to download and install BI platform libraries as a part of your application.
		//	If you want to programmatically access the advanced functionality of the BI platform, including server
		//	administration, security configuration, and modifying the repository, use one of the BI platform SDKs
		//	that support these features. For example, use the SAP BusinessObjects Business Intelligence platform
		//	Java SDK, the SAP BusinessObjects Business Intelligence platform .NET SDK, or the SAP
		//	BusinessObjects Business Intelligence platform Web Services SDK to access the advanced features
		//	of the BI platform.
		//****************************************************************************
		myresponse = rest.sendGet_token(PROTOCOL_HOST_PORT + "biprws/infostore");
		if (myresponse.responseCode == 200)
		{
			System.out.println(myresponse.response);
		}
		
		//*************************************************************************************
		//sbo41_webi_restful_ws_en.pdf
		//
		//SAP Web Intelligence RESTful Web Service
		//
		//The Web Intelligence RESTful web service SDK is an API used for manipulating the folIowing:
		//	• manipulating Web Intelligence documents and reports
		//	• retrieving data from a dataprovider
		//	• retrieving a list of available universes and details of an universes
		//	• scheduling documents
		//	It cannot be used to edit/create SAP Web Intelligence documents.
		//	The Web Intelligence RESTful web service SDK relies on the BI platform RESTful web services API
		//	for session management and repository access. Before starting with the Web Intelligence RESTful web
		//	service SDK, we strongly recommend that you to read the Business Intelligence Platform RESTful Web
		//	Service Developer Guide
		//******************************************************************************************************
		
		//******************************************************************************************************
		//3.2 Managing Documents
		//
		//Below are the main operations available to manage Web Intelligence documents.
		//You can get information about:
		//• Configuration formats
		//• Custom formats
		//• Documents
		//• Font mappings
		//• Functions
		//• Operators
		//• Report skins
		//You can manage
		//• Alerters
		//• Attachments
		//• Change tracking
		//• Documents, including exporting documents
		//• Links
		//• Styles
		//• Stylesheets (CSS)
		//The default URL to request Web Intelligence RESTful web services is the following:
		//http://<serverName>:6405/biprws/raylight/vx
		//Note:
		//Management of auto save & auto recovery configuration is currently not supported.
		//
		//(Skipping copy and paste about localization ..... assuming everybody reads/writes english
		//
		//******************************************************************************************************
		
		//****************************************************************************************
		//3.2.2 Document: retrieving, copying, or creating
		//
		//Use this URL to:
		//• Get the document list from the CMS ( GET <url>/documents).
		//• Copy a document (POST <url>/documents).
		//• Create an empty document (POST <url>/documents).
		//
		//Getting the Web Intelligence document list from the CMS:
		//This retrieves the list of document stored in the CMS. The documents are sorted by name. The list
		//depends on user access rights. You can also specify the number of documents to return for the list and
		//the first document to be used as the start document in the document list that you want to retrieve.
		//
		//Request:
		//GET http://<serverName>:6405/biprws/raylight/vx/documents
		//****************************************************************************************
		
		
		//shoot this call took 202426 milliseconds or a little over 3 minutes
		
		//long start_time = System.currentTimeMillis();
		//myresponse = rest.sendGet_token_with_quotes(PROTOCOL_HOST_PORT + "biprws/raylight/v1/documents");
		//long end_time   = System.currentTimeMillis();
		//if (myresponse.responseCode == 200)
		//{
		//	System.out.println(myresponse.response);
		//}
		//long timeElapsed = end_time - start_time;
		//String timestr = String.format("timeElased = %d milliseconds", timeElapsed);
		//System.out.println(timestr);
		
		
		//**************************************************************************************************************************
		//3.2.4 Document properties
		//
		//Use these functions to list or edit the document properties that are visible in the "Document Summary".
		//Certain settings are attributed automatically and cannot be set manually (for example, the last refresh
		//time).
		//
		//Related Topics
		//• Getting the properties of a document
		//• Update the properties of a document
		//
		//3.2.4.1 Getting the properties of a document
		//
		//Get the properties of a document referenced by its documentId parameter. (GET <url>documents/{documentId}/properties
		//
		//Note:
		//{documentId}: The identifier of the Web Intelligence document is retrieved in the document list by:
		//GET http://<serverName>:6405/biprws/raylight/vx/documents
		//
		//Retrieves the properties of a document
		//
		//Request:
		//GET http://<serverName>:6405/biprws/raylight/vx/documents/{documentId}/properties
		//*****************************************************************************************************************************
		
		myresponse = rest.sendGet_token_with_quotes(PROTOCOL_HOST_PORT + "biprws/raylight/v1/documents/592369/properties");
		if (myresponse.responseCode == 200)
		{
			System.out.println(myresponse.response);
		}
		
		
		//*******************************************************************************************************************
		//3.2.5 Exporting documents
		//
		//You can export a document in two ways: the entire document, or in paginated mode. You use the two
		//following urls:
		//• Export a document (GET -s <url>/documents/{documentId}[?parameters])
		//• Export a document in paginated mode (GET -s <url>/documents/{documentId}/pages)
		//
		//The output format can be:
		//• XML
		//• PDF
		//• Excel 2003
		//• Excel 2007
		//
		//Related Topics
		//• Exporting an entire document
		//• Exporting a document in paginated mode
		//
		//3.2.5.1 Exporting an entire document
		//You can export documents in the following formats:
		//• XML
		//• PDF
		//• Excel 2003
		//• Excel 2007
		//
		//Exporting a document
		//Note:
		//If HTML output is chosen, image links will be generated by Raylight: thus, the logon token must still be
		//valid when HTML output is displayed (to be able to get images from the generated links)
		//Request:
		//GET http://<serverName>:6405/biprws/raylight/vx/documents/{documentId}[?parameters]
		//*******************************************************************************************************************
		
		int report_response_code = rest.sendGet_token_with_quotes_file_output(PROTOCOL_HOST_PORT + "biprws/raylight/v1/documents/592369",
				                                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
				                                                "myoutputfile.xlsx");
		if (report_response_code == 200)
			System.out.println("File dumped to myoutputfile.xlsx");
		
		
		
		//This PDF file dump takes over 3 minutes
		
		//long start_time = System.currentTimeMillis();
		//report_response_code = rest.sendGet_token_with_quotes_file_output(PROTOCOL_HOST_PORT + "biprws/raylight/v1/documents/592369/pages",
        //        															"application/pdf",
        //        															"myoutputfile.pdf");
		//long end_time = System.currentTimeMillis();
		//long timeElapsed = end_time - start_time;
		//String timestr = String.format("timeElased = %d milliseconds", timeElapsed);
		//System.out.println(timestr);
		//if (report_response_code == 200)
		//	System.out.println("File dumped to myoutputfile.pdf");
		
		//***********************************************************************************************************
		//3.2.6 Managing styles, formats, fonts, skins, and charsets ..... (skip for now)
		//***********************************************************************************************************
		
		//***********************************************************************************************************
		//3.2.7 Managing functions, operators, and variables
		//
		//This section describes the different methods you can use for managing formula engine functions and
		//operators, and managing variables.
		//• Get the list of available formula engine functions (GET <url>/configuration/functions)
		//• Get the list of available formula engine operators (GET <url>/configuration/operators)
		//• Get the content of a documents variables dictionary (GET <url>/documents/{documentId}/variables)
		//• Add a new expression to a documents variables dictionary (POST <url>/documents/{documentId}/variables)
		//• Get the definition of a variable from a documents variable dictionary (GET <url>/documents/{documentId}/variables/{variableId})
		//• Modify the definition of an variable from a documents variable dictionary (PUT <url>/documents/{documentId}/variables/{variableId})
		//• Delete a variable from a documents variable dictionary (DELETE <url>/documents/{documentId}/variables/{variableId})
		//
		//Related Topics
		//• Getting the list of available formula engine functions
		//• Getting the list of available formula engine operators
		//• Getting the list of variables
		//• Adding a variable definition
		//• Getting the definition of a variable
		//• Modifying a variable definition
		//• Deleting a variable definition
		//***********************************************************************************************************
		
		//***********************************************************************************************************
		//3.2.7.1 Getting the list of available formula engine functions
		//Gets all functions of the available formula engine. This can be used to create formulas in the Report
		//Specification or define variables in the document dictionary.
		//
		//Request URL
		//GET http://<serverName>:6405/biprws/raylight/vx/configuration/functions
		//***********************************************************************************************************
		
		myresponse = rest.sendGet_token_with_quotes(PROTOCOL_HOST_PORT + "biprws/raylight/v1/configuration/functions");
		if (myresponse.responseCode == 200)
		{
			System.out.println(myresponse.response);
		}
		
		//********************************************************************************************************************
		//3.2.7.2 Getting the list of available formula engine operators
		//Gets all operators of the formula engine. This can be used to create formulas in the Report Specification
		//or define variables in the document dictionary.
		//
		//Request URL
		//GET http://<serverName>:6405/biprws/raylight/vx/configuration/operators
		//********************************************************************************************************************
		myresponse = rest.sendGet_token_with_quotes(PROTOCOL_HOST_PORT + "biprws/raylight/v1/configuration/operators");
		if (myresponse.responseCode == 200)
		{
			System.out.println(myresponse.response);
		}
		
		//**************************************************************************************************************
		//3.2.7.3 Getting the list of variables
		//Use this report to:
		//	• Get the content of a documents variables dictionary (GET <url>/documents/{documentId}/variables)
		//
		//Getting the content of a documents variables dictionary
		//
		//Request:
		//	GET http://<serverName>:6405/biprws/raylight/vx/documents/{documentId}/variables
		//***************************************************************************************************************
		
		myresponse = rest.sendGet_token_with_quotes(PROTOCOL_HOST_PORT + "biprws/raylight/v1/documents/592369/variables");
		if (myresponse.responseCode == 200)
		{
			System.out.println(myresponse.response);
		}
		
		//*******************************************************************************************************************
		//3.2.7.4 Adding a variable definition
		//Adding a new expression to the documents variables dictionary
		//You define the expression in the body which is defined in an .xml file saved in the current path (usually
		//the same path as the cURL tool). For example; variable2.xml.
		//
		//Note:
		//• The formula must be valid.
		//• You can only create a measure, an attribute or a dimension.
		//• When you create an attribute, the associated dimension is mandatory.
		//
		//Request:
		//POST http://<serverName>:6405/biprws/raylight/vx/documents/{documentId}/variables
		//********************************************************************************************************************
		
		//********************************************************************************************************************************************
		//3.2.7.5 Getting the definition of a variable
		//Use this to:
		//	• Get the definition of a variable from a documents variable dictionary (GET <url>/documents/{documentId}/variables/{variableId} )
		//
		//Note:
		//	{documentId}: The identifier of the Web Intelligence document retrieved in the document list 
		//by: GET http://<serverName>:6405/biprws/raylight/vx/documents
		//
		//Note:
		//  {variableId}: The identifier of the Web Intelligence variable retrieved in the document's variable list
		//by: GET http://<serverName>:6405/biprws/raylight/vx/documents/documentId/variables
		//
		//Getting the definition of a variable used by a document
		//
		//Request:
		//	GET http://<serverName>:6405/biprws/raylight/vx/documents/{documentId}/variables/{variableId}
		//*********************************************************************************************************************************************
		
		myresponse = rest.sendGet_token_with_quotes(PROTOCOL_HOST_PORT + "biprws/raylight/v1/documents/592369/variables/L2");
		if (myresponse.responseCode == 200)
		{
			System.out.println(myresponse.response);
		}
		
		System.out.println();
        System.out.println("MAIN DONE");
	}

	
	
}//class
