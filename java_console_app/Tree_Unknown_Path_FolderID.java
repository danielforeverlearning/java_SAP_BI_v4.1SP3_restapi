package console_restapi;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class Tree_Unknown_Path_FolderID extends JPanel implements TreeSelectionListener{
	
	private DefaultMutableTreeNode top_node;
	private JTree mytree;
	private GUI_Frame my_topmost_gui;
	
	public Tree_Unknown_Path_FolderID(GUI_Frame gui) {
		super(new GridLayout(1,0));
		
		my_topmost_gui = gui;
	}
	
	/** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent ev) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)mytree.getLastSelectedPathComponent();
 
        if (node == null) return;
 
        Object nodeInfo = node.getUserObject();
        if (node.isLeaf()) {
            System.out.println("UNKNOWN_PATH: LEAF = " + (String)nodeInfo);
            my_topmost_gui.HideEnterPathInfo();
        } else {
        	
        	String tempstr = (String)nodeInfo;
            System.out.println("UNKNOWN_PATH: NON-LEAF = " + tempstr);
            
            if (node == top_node)
            	my_topmost_gui.HideEnterPathInfo();
            else {
	            int folderID = -1;
	            try {
					folderID = Integer.parseInt(tempstr);
				}
				catch (NumberFormatException ex) { //NOT a folderID
				}
	            finally { //it is a folderID
	            	my_topmost_gui.ShowEnterPathInfo(folderID);
	            }
            }
        }
    }
	
	public void LoadMap(HashMap<String, ArrayList<DocumentInfo>> map) {
		
		//Create the tree nodes
		top_node = new DefaultMutableTreeNode("FolderIDs with no path defined found:");
		
		Set<String> keyset    = map.keySet();
		Object[] mykeyobjs    = keyset.toArray();
		for (int kk=0; kk < mykeyobjs.length; kk++) {
			
			String key = (String)mykeyobjs[kk];
			
			DefaultMutableTreeNode folderID_node = new DefaultMutableTreeNode(key);
			top_node.add(folderID_node);
			
			ArrayList<DocumentInfo> mydocs = map.get(key);
			for (int dd=0; dd < mydocs.size(); dd++) {
				DefaultMutableTreeNode docinfo_name = new DefaultMutableTreeNode(mydocs.get(dd).name);
				folderID_node.add(docinfo_name);
			}
		}
		
		//Create a tree that allows one selection at a time
		mytree = new JTree(top_node);
		mytree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		//Listen for when the selection changes
		mytree.addTreeSelectionListener(this);
		
		//add the tree to this which is instantiation of a subclass of JPanel
		add(mytree);
	}
	
	
	public ArrayList<String> Get_FolderID_Then_Remove(String FolderID) {
		
		DefaultMutableTreeNode folder_iter = top_node;
		ArrayList<String> mylist = new ArrayList<String>();
		
		for (int nn=0; nn < folder_iter.getChildCount(); nn++) {
			DefaultMutableTreeNode folder = (DefaultMutableTreeNode)folder_iter.getChildAt(nn);
			Object myobj = folder.getUserObject();
			String mystr = (String)myobj;
			if (mystr.equals(FolderID)) {
				
				//get filenames
				DefaultMutableTreeNode files_iter = folder;
				for (int ff=0; ff < files_iter.getChildCount(); ff++) {
					DefaultMutableTreeNode file = (DefaultMutableTreeNode)files_iter.getChildAt(ff);
					Object fileobj = file.getUserObject();
					String filestr = (String)fileobj;
					mylist.add(filestr);
				}
				
				//remove
				folder_iter.remove(nn);
				break;
			}
		}
		
		((DefaultTreeModel)mytree.getModel()).reload();
		
		return mylist;
	}

}
