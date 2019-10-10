package console_restapi;

import java.util.HashMap; 
import java.util.ArrayList;

public class SAP_BI_WebIntel_REST_helper {
	
	private String USERNAME;
	private String PASSWORD;
	private String PROTOCOL_HOST_PORT;
	
	private String TOKEN;
	private String TOKEN_WITH_QUOTES;
	
	public SAP_BI_WebIntel_REST_helper(String username, String password, String protocol_host_port) {
		USERNAME = username;
		PASSWORD = password;
		PROTOCOL_HOST_PORT = protocol_host_port;
	}
	
	public Boolean get_token() {
		
		try {
			RestAPICaller rest = new RestAPICaller();
			
			RestAPIResponse myresponse = rest.sendGet(PROTOCOL_HOST_PORT + "biprws/logon/long");
			if (myresponse.responseCode == 200)
				System.out.println(myresponse.response);
			
			//xml helper
			XML_helper auth_help = new XML_helper();
			auth_help.parse(myresponse.response);
			auth_help.set_attr("userName", USERNAME);
			auth_help.set_attr("password", PASSWORD);
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
				TOKEN = token;
				TOKEN_WITH_QUOTES = "\"" + token + "\"";
				
				return true;
			}
			else
				return false;
		}
		catch (Exception ex) {
			System.out.println("EXCEPTION CAUGHT IN class SAP_BI_WebIntel_REST_helper method get_token !!!!!");
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			
			return false;
		}
		
	}
	
	//****************************************************************************
	//returns: HashMap<folderID as String, ArrayList<DocumentInfo>>
	//         null == can not get token 
	//****************************************************************************
	public HashMap<String, ArrayList<DocumentInfo>>  Save_All_Documents() throws Exception {
		
		RestAPICaller rest = new RestAPICaller();
		Boolean token_success = get_token();
		if (token_success == false)
			return null;
		
		HashMap<String, ArrayList<DocumentInfo>> map = new HashMap<String, ArrayList<DocumentInfo>>();
		
		int offset=0;
		Boolean done = false;
		while (done == false) {
			String urlstr = String.format("%sbiprws/raylight/v1/documents?limit=50&offset=%d", PROTOCOL_HOST_PORT, offset);
			RestAPIResponse myresponse = rest.sendGet_token_with_quotes(urlstr, TOKEN_WITH_QUOTES);
			if (myresponse.responseCode == 200) {
				
				String tempstr = String.format("offset = %d", offset);
				System.out.println(tempstr);
				System.out.println(myresponse.response);
				
				XML_helper xmlhelp = new XML_helper();
				xmlhelp.parse(myresponse.response);
				int temp_doc_count = xmlhelp.get_documents(map);
				
				if (temp_doc_count < 50)
					done = true;
				else
					offset += 50;
			}
			else {
				throw new Exception("Got a bad responseCode in class SAP_BI_WebIntel_REST_helper method ListDocuments !!!!!");
			}
		}
		
		return map;
	}
	
	public void Demo() throws Exception {
		RestAPICaller rest = new RestAPICaller();
		
		//String gittest = rest.sendGet_https("https://github.com");
		//System.out.println(gittest);
		
		RestAPIResponse myresponse = rest.sendGet(PROTOCOL_HOST_PORT + "biprws/raylight/v1/about");
		if (myresponse.responseCode == 200)
			System.out.println(myresponse.response);
		
		
		Boolean token_success = get_token();
		if (token_success == false)
		{
			System.out.println("class SAP_BI_WebIntel_REST_helper method Demo, could not get token, exiting demo");
			return;
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
		myresponse = rest.sendGet_token(PROTOCOL_HOST_PORT + "biprws/infostore", TOKEN);
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
		//myresponse = rest.sendGet_token_with_quotes(PROTOCOL_HOST_PORT + "biprws/raylight/v1/documents", TOKEN_WITH_QUOTES);
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
		
		myresponse = rest.sendGet_token_with_quotes(PROTOCOL_HOST_PORT + "biprws/raylight/v1/documents/592369/properties", TOKEN_WITH_QUOTES);
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
				                                                "myoutputfile.xlsx",
				                                                TOKEN_WITH_QUOTES);
		if (report_response_code == 200)
			System.out.println("File dumped to myoutputfile.xlsx");
		
		
		
		//This PDF file dump takes over 3 minutes
		
		//long start_time = System.currentTimeMillis();
		//report_response_code = rest.sendGet_token_with_quotes_file_output(PROTOCOL_HOST_PORT + "biprws/raylight/v1/documents/592369/pages",
        //        															"application/pdf",
        //        															"myoutputfile.pdf,
		//																	TOKEN_WITH_QUOTES);
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
		
		myresponse = rest.sendGet_token_with_quotes(PROTOCOL_HOST_PORT + "biprws/raylight/v1/configuration/functions", TOKEN_WITH_QUOTES);
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
		myresponse = rest.sendGet_token_with_quotes(PROTOCOL_HOST_PORT + "biprws/raylight/v1/configuration/operators", TOKEN_WITH_QUOTES);
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
		
		myresponse = rest.sendGet_token_with_quotes(PROTOCOL_HOST_PORT + "biprws/raylight/v1/documents/592369/variables", TOKEN_WITH_QUOTES);
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
		
		myresponse = rest.sendGet_token_with_quotes(PROTOCOL_HOST_PORT + "biprws/raylight/v1/documents/592369/variables/L2", TOKEN_WITH_QUOTES);
		if (myresponse.responseCode == 200)
		{
			System.out.println(myresponse.response);
		}
	}

}
