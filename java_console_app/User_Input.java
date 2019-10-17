package console_restapi;

import java.util.Scanner;

public class User_Input {
	String myusername;
	String mypassword;
	String PROTOCOL_HOST_PORT;
	String server_type;
	
public void GetUserInput() {
		
		//Eclipse-IDE uses javaw.exe to run code.
		//javaw.exe runs codes without association with current terminal/console
		//so System.Console() returns null
		//so you can not call System.Console().readline()
		//But javaw.exe process still supports standard input/output/error streams
		//so that IDEs such as Eclipe-IDE process can use these streams.
		//(For example, in Eclipse-IDE there is a tab/window
		//which simulates console and shows output
		//from all calls to System.out.println(mystring);
		//That tab/window shows that despite javaw.exe
		//not having associated console (null)
		//the Eclipse-IDE process was able to handle data
		//from the standard output of the javaw.exe process.
		//So the solution here is to use Scanner class
		//which is meant to read data as text from Streams and Readers.
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Please enter username:");
		myusername = scanner.nextLine();
		
		System.out.println("Please enter password:");
		mypassword = scanner.nextLine();
		
		System.out.println("Server choices:");
		System.out.println("     1. MYTEST");
		System.out.println("     2. MYPROD");
		System.out.println();
		System.out.println("Pleaser enter 1 or 2:");
		String serverchoice = scanner.nextLine();
		
		System.out.println();
		System.out.println();
		
		if (serverchoice.equals("2")) {
			PROTOCOL_HOST_PORT = "http://myprodhost:6405/";
			server_type = "PROD_PRODHOSTNAME_6405";
		}
		else {
			PROTOCOL_HOST_PORT = "http://mytesthost:6405/";
			server_type = "TEST_TESTHOSTNAME_6405";
		}
		System.out.println("Server choice is " + server_type + " == " + PROTOCOL_HOST_PORT);
		
		scanner.close();
	}
}
