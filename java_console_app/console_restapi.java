
package console_restapi;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.FileWriter;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
		
		SAP_BI_WebIntel_REST_helper bihelp = new SAP_BI_WebIntel_REST_helper(myusername, mypassword, PROTOCOL_HOST_PORT);
		//bihelp.Demo();
		
		try {
			HashMap<String, ArrayList<DocumentInfo>> map = bihelp.Save_All_Documents();
			if (map != null) {
				int ff=0;
				FileWriter myfile = new FileWriter("my_test_save_all_documents.txt");
				
				Set<String> keyset = map.keySet();
				Object[] mykeyobjs    = keyset.toArray();
				for (int kk=0; kk < mykeyobjs.length; kk++) {
					
					String key = (String)mykeyobjs[kk];
					
					myfile.write(key + "\n");
					myfile.write("-------------------------------------\n");
					
					ArrayList<DocumentInfo> mydocs = map.get(key);
					for (int dd=0; dd < mydocs.size(); dd++) {
						String tempstr = String.format("[%d] %s\n", ff, mydocs.get(dd).name);
						myfile.write(tempstr);
						ff++;
					}
				}
				
				myfile.close();
			}
		}
		catch (Exception ex) {
			System.out.println("EXCEPTION CAUGHT TESTING  bihelp.Save_All_Documents !!!!!");
			ex.printStackTrace();
		}
		
		
		System.out.println();
        System.out.println("MAIN DONE");
	}

	
	
}//class
