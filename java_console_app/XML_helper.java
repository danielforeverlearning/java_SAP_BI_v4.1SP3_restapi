package console_restapi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.HashMap;

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
	
	public Boolean parse_xml_file(String filename) {
		try {
			FileInputStream filestream = new FileInputStream(filename);		
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(filestream);
			return true;
		}
		catch (Exception ex) {
			System.out.println("EXCEPTION CAUGHT IN XML_helper method parse_xml_file!!!!!");
			String msg = ex.getMessage();
			System.out.println(msg);
			return false;
		}
	}
	
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
	
	
	//****************************************************************************
	//input: map == HashMap<folderID as String, ArrayList<DocumentInfo>>
	//map is modified
	//see XML_FILE_TEST_get_documents()
	//****************************************************************************
	public int get_documents(HashMap<String, ArrayList<DocumentInfo>> map)
	{
		int doc_info_count = 0;
		Node main_node = doc.getElementsByTagName("documents").item(0);
		NodeList doc_list = main_node.getChildNodes();
		for (int ii=0; ii < doc_list.getLength(); ii++) {
			Node doc = doc_list.item(ii);
			short mynodetype = doc.getNodeType();
			if (mynodetype == Node.ELEMENT_NODE) {
				Element doc_elem = (Element) doc;
				if ("document".equals(doc_elem.getNodeName())) {
					
					DocumentInfo my_doc_info = new DocumentInfo();
					
					NodeList inside_list = doc_elem.getChildNodes();
					for (int xx=0; xx < inside_list.getLength(); xx++) {
        			
						String name = inside_list.item(xx).getNodeName();
						if (name.equals("id"))
							my_doc_info.documentID  = inside_list.item(xx).getTextContent();
						else if (name.equals("cuid"))
							my_doc_info.cuid        = inside_list.item(xx).getTextContent();
						else if (name.equals("name"))
							my_doc_info.name        = inside_list.item(xx).getTextContent();
						else if (name.equals("folderId"))
							my_doc_info.folderID    = inside_list.item(xx).getTextContent();
						else if (name.equals("state"))
							my_doc_info.state       = inside_list.item(xx).getTextContent();
						else {
							String tempstr = inside_list.item(xx).getTextContent();
							short temptype = inside_list.item(xx).getNodeType();
							
							int dummy = 2;
							dummy++;
						}
					}
					
					//**************
					//hashmap add
					//**************
					if (my_doc_info.folderID != null && my_doc_info.folderID.length() > 0) {
						if (map.containsKey(my_doc_info.folderID)) {
							ArrayList<DocumentInfo> list_doc_info = map.get(my_doc_info.folderID);
							list_doc_info.add(my_doc_info);
						}
						else {
							ArrayList<DocumentInfo> my_new_list = new ArrayList<DocumentInfo>();
							my_new_list.add(my_doc_info);
							map.put(my_doc_info.folderID, my_new_list);
						}
						doc_info_count++;
					}
        		}//if document
			}//if Node.ELEMENT_NODE
		}//for
		
		return doc_info_count;
	}//get_documents
	
	
	public void XML_FILE_TEST_get_documents() {
		HashMap<String, ArrayList<DocumentInfo>> testmap = new HashMap<String, ArrayList<DocumentInfo>>();
		XML_helper xmlhelp = new XML_helper();
		xmlhelp.parse_xml_file("doclist_50_offset0.xml");
		int count_get_documents = xmlhelp.get_documents(testmap);
		ArrayList<DocumentInfo> mydocs = testmap.get("16118");
		if (mydocs != null) {
			for (int ii=0; ii < mydocs.size(); ii++)
				System.out.println("XML_FILE_TEST_get_documents: " + mydocs.get(ii).name);
		}
		else
			System.out.println("XML_FILE_TEST_get_documents: mydocs == null");
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
