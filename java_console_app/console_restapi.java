
package console_restapi;


import java.util.ArrayList;
import java.util.HashMap;



public class console_restapi {
	
	
	public static void main(String[] args) throws Exception {
		
		//***********************************************************************************
		//This does actual restful-API calls.
		//Because it takes so long, i can test other parts of the app using old files saved.
		//***********************************************************************************
		User_Input myinput = new User_Input(); 
		myinput.GetUserInput();
		SAP_BI_WebIntel_REST_helper bihelp = new SAP_BI_WebIntel_REST_helper(myinput.myusername, myinput.mypassword, myinput.PROTOCOL_HOST_PORT);
		
		//bihelp.Demo();
		bihelp.Demo2();
		//bihelp.TEST_SAVE_ALL_DOCUMENTS("my_test_output_from_" + myinput.server_type + ".txt");
		
		
		//*************************************************************************************
		//Calls to TEST_MYHOST_6405 take over 3 minutes more like over 5 minutes.
		//Calls to PROD_MYHOST_6405 take a very very long time maybe its because
		//of lots of staff usage in real-time.
		//I am going to use previous output i saved to file for testing gui.
		//*************************************************************************************
		/*****
		Testing_Debugging_helper testdebug = new Testing_Debugging_helper();
		HashMap<String, ArrayList<DocumentInfo>> map = testdebug.TEST_LOAD_ALL_DOCUMENTS("my_test_output_from_TEST_MYHOST_6405.txt");
		
		GUI_Frame guiframe = new GUI_Frame();
		
		Tree_Unknown_Path_FolderID unknown_path_tree_panel = new Tree_Unknown_Path_FolderID(guiframe);
		unknown_path_tree_panel.LoadMap(map);
		
		Tree_Known_Path_FolderID known_path_tree_panel = new Tree_Known_Path_FolderID();

		guiframe.Load_Trees(unknown_path_tree_panel, known_path_tree_panel);
		******/
		
		System.out.println();
        System.out.println("MAIN DONE");
	}

	
	
}//class
