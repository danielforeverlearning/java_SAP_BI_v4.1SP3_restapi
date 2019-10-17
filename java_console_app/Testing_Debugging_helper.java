package console_restapi;


import java.io.File;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

import java.lang.Integer;
import java.lang.NumberFormatException;

public class Testing_Debugging_helper {
	
	//****************************************************************************
	//returns: HashMap<folderID as String, ArrayList<DocumentInfo>>
	//	       null == maybe file not found or contents of file are bad 
	//
	//current file format example:
	//
	//19588
	//-------------------------------------
	//[0] myfilename1
	//[1] myfilename2
	//17168
	//-------------------------------------
	//[2] myfilename1
	//19589
	//-------------------------------------
	//[3] myfilename1
	//[4] myfilename2
	//[5] myfilename3
	//[6] myfilename4
	//[7] myfilename5
	//19586
	//****************************************************************************

	public HashMap<String, ArrayList<DocumentInfo>> TEST_LOAD_ALL_DOCUMENTS(String input_filename) {
		
		Boolean brand_new_folderID_found = false;
		int  folderID = 0;
		int  last_folderID_found = -1;
		ArrayList<DocumentInfo> folderlist = null;
		
		File infile = null;
		Scanner sc = null;
		HashMap<String, ArrayList<DocumentInfo>> results = null;
		
		try {
			infile = new File(input_filename);
			sc = new Scanner(infile);
			results = new HashMap<String, ArrayList<DocumentInfo>>();
			
			while (true) {
				
				String line = sc.nextLine();
				try {
					folderID = Integer.parseInt(line);
					brand_new_folderID_found = true;
					line = sc.nextLine(); //this should be line of dashes
				}
				catch (NumberFormatException ex) {
					
					brand_new_folderID_found = false;
					
					//you did not get a folderID for sure
					//you just got a DocumentInfo line 
					//or whitespace 
					//or corrupted string from a corrupted text file
					
					//to check to make sure it is not whitespace
					//or corrupted string from a corrupted text file
					//good docinfo means char at position 0 is '['
					//followed by int
					//followed by ']'
					if (line.startsWith("[")) {
						
						int end_bracket_index = line.indexOf(']');
						String temp = line.substring(end_bracket_index + 1);
						
						DocumentInfo docinfo = new DocumentInfo();
						docinfo.folderID = String.format("%d", folderID);
						docinfo.name = temp.trim();
						
						folderlist.add(docinfo);	
					}
					else {
						System.out.println("ERROR in class Testing_Debugging_helper.java found corrupted string or misplaced whitespace !!!!!");
						System.out.println("last_folderID_found == " + last_folderID_found);
						return null;
					}
					
				}
				
				if (brand_new_folderID_found) {
					if (last_folderID_found != folderID) {
						
						if (last_folderID_found == -1) {
							//very 1st folderID found, nothing to save yet
							folderlist = new ArrayList<DocumentInfo>();
							last_folderID_found = folderID;
						}
						else {
							//save last_folderID_found and its list of DocumentInfo
							String temp_folderID = String.format("%d", last_folderID_found);
							results.put(temp_folderID, folderlist);
							
							//when done saving ..... need to continue processing text file
							folderlist = new ArrayList<DocumentInfo>();
							last_folderID_found = folderID;
							
						}
						
					}
					//else ..... technically this should not happen
					//it means you just got the same folderID again
				}
			}
		}
		catch (NoSuchElementException ex) {
			
			//no more lines in file
			
			sc.close();
			return results;
			
		}
		catch (Exception ex) {
			System.out.println("EXCEPTION CAUGHT IN class Testing_Debugging_helper method TEST_LOAD_ALL_DOCUMENTS !!!!!");
			ex.printStackTrace();
			
			sc.close();
			return null;
		}
			
	}//TEST_LOAD_ALL_DOCUMENTS

}//class Testing_Debugging_helper
