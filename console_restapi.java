
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

		String myusername = "";
		String mypassword = "";
		String PROTOCOL_HOST_PORT = "http://asdfasdfasdfsa:1234/";
		
		RestAPICaller rest = new RestAPICaller();

		//String gittest = rest.sendGet_https("https://github.com");
		//System.out.println(gittest);
		
		RestAPIResponse myresponse = rest.sendGet(PROTOCOL_HOST_PORT + "biprws/raylight/v1/about");
		if (myresponse.responseCode == 200)
			System.out.println(myresponse.response);
		
		
		myresponse = rest.sendGet(PROTOCOL_HOST_PORT + "biprws/logon/long");
		if (myresponse.responseCode == 200)
			System.out.println(myresponse.response);
		
		
		ByteArrayInputStream instream = new ByteArrayInputStream(myresponse.response.getBytes());		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(instream);
        
        //doc.getDocumentElement().normalize();
        //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		//String userName = doc.getDocumentElement().getAttribute("userName");
		//System.out.println("userName = " + userName);
		//String password = doc.getDocumentElement().getAttribute("password");
		//System.out.println("password = " + password);
		
        //doc.getDocumentElement().setAttribute("userName", "asdf");
		//doc.getDocumentElement().setAttribute("password", "asdf");
		
		//userName = doc.getDocumentElement().getAttribute("userName");
		//System.out.println("userName = " + userName);
		//password = doc.getDocumentElement().getAttribute("password");
		//System.out.println("password = " + password);
        
        Node attrs_node = doc.getElementsByTagName("attrs").item(0);
        NodeList attr_list = attrs_node.getChildNodes();
        for (int ii=0; ii < attr_list.getLength(); ii++) {
        	Node node = attr_list.item(ii);
        	if (node.getNodeType() == Node.ELEMENT_NODE) {
        		Element eElement = (Element) node;
        		if ("attr".equals(eElement.getNodeName())) {
        			
        			String name = eElement.getAttribute("name");
        			System.out.println(name);
        			
        			if (name.equals("userName"))
        				eElement.setTextContent(myusername);
        			else if (name.equals("password"))
        				eElement.setTextContent(mypassword);
        		}
        	}
        }
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource xmlsource = new DOMSource(doc);
		
		ByteArrayOutputStream tempout = new ByteArrayOutputStream();
		StreamResult xmlresult = new StreamResult(tempout);
		transformer.transform(xmlsource, xmlresult);
		String authxml = tempout.toString();
		
		System.out.println();
		System.out.println(authxml);
		
		//****** quick and dirty for now, take out xml header element, i do not think the restapi call likes it
		//authxml = authxml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>", "");
		//System.out.println();
		//System.out.println(authxml);
		
		//****** Try to authenticate and get token **********
		myresponse = rest.sendPost_XML(PROTOCOL_HOST_PORT + "biprws/logon/long", authxml);
		if (myresponse.responseCode == 200)
			System.out.println(myresponse.response);
		
		
		
		
		System.out.println();
        System.out.println("MAIN DONE");
	}

	
	
}//class
