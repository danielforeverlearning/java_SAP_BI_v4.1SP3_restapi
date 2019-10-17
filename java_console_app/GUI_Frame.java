package console_restapi;


import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.*;

import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.BorderFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;



public class GUI_Frame extends JFrame implements ActionListener,
												 WindowListener,
												 WindowFocusListener,
												 WindowStateListener  {
	
	private JPanel      EnterPathInfoPanel;
	private JLabel      EnterPathInfoLabel;
	private JTextField  EnterPathInfoTextField;
	private String      EnterPathInfoFolderID;
	
	private JScrollPane              KnownPathScrollPane;
	private Tree_Known_Path_FolderID KnownPathTreePanel;
	
	private JScrollPane                UnknownPathScrollPane;
	private Tree_Unknown_Path_FolderID UnknownPathTreePanel;
	
	private HashMap<String, String>    PathMap;
	
	public GUI_Frame() {
		
		super("console_restapi GUI_Frame");
		
		CreateEnterPathInfoPanel();
		
		addWindowListener(this);
		addWindowFocusListener(this);
		addWindowStateListener(this);
		
		setSize(1100,1000);
		setLayout(null);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void LoadPathMap() {
		
		if (PathMap == null)
			PathMap = new HashMap<String, String>();
		
		File infile = new File("PathMap.txt");
		Scanner sc = null;
		
		try {
			sc = new Scanner(infile);
		}
		catch (FileNotFoundException ex) {
			//PathMap.txt file not found, that is ok for now
			return;
		}
		
		//PathMap.txt file is found, continue processing
		try {
			while (true) {
				String line = sc.nextLine();
				int colonindex = line.indexOf(':');
				if (colonindex != -1) {
					String folderIDstr = line.substring(0, colonindex);
					String pathinfo = line.substring(colonindex + 1);
					PathMap.put(folderIDstr, pathinfo);					
					ArrayList<String> filelist = UnknownPathTreePanel.Get_FolderID_Then_Remove(folderIDstr);
					KnownPathTreePanel.Check_And_Add_Path_To_Tree(pathinfo, filelist);
				}
				else
					throw new Exception("Found a line in class GUI_Frame method LoadPathMap without a colon character !!!!!");
			}
		}
		catch (NoSuchElementException ex) {	
			//no more lines in file
			
			sc.close();
			return;
		}
		catch (Exception ex) {
			System.out.println("EXCEPTION CAUGHT IN class GUI_Frame method LoadPathMap !!!!!");
			System.out.println(ex.getMessage());
			ex.printStackTrace();
			
			sc.close();
			return;
		}
	}//LoadPathMap
	
	
	private void SavePathMap() {
		try {
			if (PathMap != null) {
				FileWriter myfile = new FileWriter("PathMap.txt");
				
				Set<String> keyset    = PathMap.keySet();
				Object[] mykeyobjs    = keyset.toArray();
				for (int kk=0; kk < mykeyobjs.length; kk++) {
					
					String key = (String)mykeyobjs[kk];
					String value = PathMap.get(key);
					String tempstr = String.format("%s:%s\n", key, value);
					myfile.write(tempstr);	
				}
				myfile.close();
			}
		}
		catch (Exception ex) {
			System.out.println("EXCEPTION CAUGHT in class GUI_Frame method SavePathMap !!!!!");
			ex.printStackTrace();
		}
	}
	
	public void  Load_Trees(Tree_Unknown_Path_FolderID unknown_path_tree_panel, Tree_Known_Path_FolderID known_path_tree_panel) {
		
		UnknownPathTreePanel = unknown_path_tree_panel;
		KnownPathTreePanel = known_path_tree_panel;
		
		UnknownPathScrollPane = new JScrollPane(unknown_path_tree_panel);
		UnknownPathScrollPane.setViewportBorder(BorderFactory.createLineBorder(Color.black));
		UnknownPathScrollPane.setBounds(30, 30, 1000, 300);
		add(UnknownPathScrollPane);
		
		UnknownPathScrollPane.revalidate();
		UnknownPathScrollPane.repaint();
		
		KnownPathScrollPane = new JScrollPane(known_path_tree_panel);
		KnownPathScrollPane.setViewportBorder(BorderFactory.createLineBorder(Color.black));
		KnownPathScrollPane.setBounds(30, 350 ,1000, 300);
		add(KnownPathScrollPane);
		
		KnownPathScrollPane.revalidate();
		KnownPathScrollPane.repaint();
		
		LoadPathMap();
	}

	
	public void actionPerformed(ActionEvent ev) {
		
		Object myobj = ev.getSource();
		JButton mybutton = (JButton)myobj;
		String actstr = mybutton.getActionCommand();
		
		if (actstr.equals("CANCEL"))
			HideEnterPathInfo();
		else if (actstr.equals("SAVE")) {
			SavePathInfo();
			HideEnterPathInfo();
		}
		
	}
		
	private void SavePathInfo() {
		
		
		String pathinfo = EnterPathInfoTextField.getText().trim();
		String tempstr = String.format("SavePathInfo: %s", pathinfo);
		System.out.println(tempstr);
		
		if (PathMap == null)
			PathMap = new HashMap<String, String>();
		
		PathMap.put(EnterPathInfoFolderID, pathinfo);
		
		ArrayList<String> filelist = UnknownPathTreePanel.Get_FolderID_Then_Remove(EnterPathInfoFolderID);
		KnownPathTreePanel.Check_And_Add_Path_To_Tree(pathinfo, filelist);
	}
	
	public void HideEnterPathInfo() {
		this.remove(EnterPathInfoPanel);
		
		//SetEnableRecursive(KnownPathScrollPane, true);
		//SetEnableRecursive(UnknownPathScrollPane, true);
		
		revalidate();
		repaint();
		
	}
	
	public void ShowEnterPathInfo(int folderID) {
		String labelstr = String.format("Please enter folder path for folderID %d:", folderID);
		EnterPathInfoLabel.setText(labelstr);
		EnterPathInfoFolderID = String.format("%d", folderID);
		
		add(EnterPathInfoPanel);
		
		//SetEnableRecursive(KnownPathScrollPane, false);
		//SetEnableRecursive(UnknownPathScrollPane, false);
		
		revalidate();
		repaint();
		
	}
	
	//**************************************************************************
	//Save this method for now to disable or enable the trees.
	//The problem is that there is single-click and double-click on an
	//unknown folderID.
	//**************************************************************************
	private void SetEnableRecursive(Component container, boolean enable){
	    container.setEnabled(enable);

	    try {
	        Component[] components= ((Container) container).getComponents();
	        for (int i = 0; i < components.length; i++) {
	        	SetEnableRecursive(components[i], enable);
	        }
	    } catch (ClassCastException e) {

	    }
	}
 
	private void CreateEnterPathInfoPanel() {
		
		EnterPathInfoPanel = new JPanel();
		EnterPathInfoPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		EnterPathInfoLabel  = new JLabel("");
		EnterPathInfoPanel.add(EnterPathInfoLabel);
		
		EnterPathInfoTextField = new JTextField(64);
		EnterPathInfoPanel.add(EnterPathInfoTextField);
		
		JButton cancelbutton = new JButton("CANCEL");
		cancelbutton.setActionCommand("CANCEL");
		cancelbutton.addActionListener(this);
		
		EnterPathInfoPanel.add(cancelbutton);
		cancelbutton.setBounds(5, 30, 30, 30);
		
		JButton savebutton = new JButton("SAVE");
		savebutton.setActionCommand("SAVE");
		savebutton.addActionListener(this);
		
		EnterPathInfoPanel.add(savebutton);
		savebutton.setBounds(50, 30, 30, 30);
		
		EnterPathInfoPanel.setBounds(30, 670, 1000, 200);
	}
	
	public void windowClosed(WindowEvent e) {
        System.out.println("windowClosed");
    }
	
	public void windowDeactivated(WindowEvent e) {
	    System.out.println("windowDeactivated");
	}
	
	public void windowActivated(WindowEvent e) {
        System.out.println("windowActivated");
    }
	
	public void windowDeiconified(WindowEvent e) {
        System.out.println("windowDeiconified");
    }
	
	public void windowIconified(WindowEvent e) {
        System.out.println("windowIconified");
    }
	
	public void windowClosing(WindowEvent e) {
        System.out.println("windowClosing");
        System.out.println("saving path map");
        SavePathMap();
    }
	
	public void windowOpened(WindowEvent e) {
        System.out.println("windowOpened");
    }
	
	public void windowLostFocus(WindowEvent e) {
        System.out.println("windowLostFocus");
    }
	
	public void windowGainedFocus(WindowEvent e) {
        System.out.println("windowGainedFocus");
    }
	
	public void windowStateChanged(WindowEvent e) {
        System.out.println("windowStateChanged");
    }
}
