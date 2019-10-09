package console_restapi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XML_helper {

	private Document doc;
	
	public Boolean parse(String response) {
		
		try {
			ByteArrayInputStream instream = new ByteArrayInputStream(response.getBytes());		
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(instream);
			return true;
		}
		catch (Exception ex) {
			System.out.println("EXCEPTION CAUGHT IN XML_helper method parse!!!!!");
			String msg = ex.getMessage();
			System.out.println(msg);
			return false;
		}
        
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
	}
	
	public void set_attr(String attrname, String value)
	{   
        Node attrs_node = doc.getElementsByTagName("attrs").item(0);
        NodeList attr_list = attrs_node.getChildNodes();
        for (int ii=0; ii < attr_list.getLength(); ii++) {
        	Node node = attr_list.item(ii);
        	if (node.getNodeType() == Node.ELEMENT_NODE) {
        		Element eElement = (Element) node;
        		if ("attr".equals(eElement.getNodeName())) {
        			
        			String name = eElement.getAttribute("name");
        			
        			if (name.equals(attrname))
        			{
        				eElement.setTextContent(value);
        				return;
        			}
        			
        		}
        	}
        }
	}
	
	public String get_attr(String attrname)
	{
		Node attrs_node = doc.getElementsByTagName("attrs").item(0);
        NodeList attr_list = attrs_node.getChildNodes();
        for (int ii=0; ii < attr_list.getLength(); ii++) {
        	Node node = attr_list.item(ii);
        	if (node.getNodeType() == Node.ELEMENT_NODE) {
        		Element eElement = (Element) node;
        		if ("attr".equals(eElement.getNodeName())) {
        			
        			String name = eElement.getAttribute("name");
        			
        			if (name.equals(attrname))
        			{
        				String value = eElement.getTextContent();
        				return value;
        			}
        			
        		}
        	}
        }
        
        return null;
	}
	
	public String transform_to_String() {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource xmlsource = new DOMSource(doc);
		
			ByteArrayOutputStream tempout = new ByteArrayOutputStream();
			StreamResult xmlresult = new StreamResult(tempout);
			transformer.transform(xmlsource, xmlresult);
			String result = tempout.toString();
			return result;
		}
		catch (Exception ex)
		{
			System.out.println("EXCEPTION CAUGHT IN XML_helper method transform_to_String!!!!!");
			String msg = ex.getMessage();
			System.out.println(msg);
			return "";
		}
	}

}
