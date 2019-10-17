package console_restapi;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

public class Tree_Known_Path_FolderID extends JPanel implements TreeSelectionListener{
	
	private JTree mytree;
	private DefaultMutableTreeNode top_node;
	
	public Tree_Known_Path_FolderID() {
		super(new GridLayout(1,0));
		
		top_node = new DefaultMutableTreeNode("FolderIDs with path defined found:");
		
		//Create a tree that allows one selection at a time
		mytree = new JTree(top_node);
		mytree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
				
		//Listen for when the selection changes
		mytree.addTreeSelectionListener(this);
				
		//add the tree to this which is instantion of a subclass of JPanel
		add(mytree);
	}
	
	/** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent ev) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)mytree.getLastSelectedPathComponent();
 
        if (node == null) return;
 
        Object nodeInfo = node.getUserObject();
        if (node.isLeaf()) {
            System.out.println("KNOWN_PATH: LEAF = " + (String)nodeInfo);
        } else {
            System.out.println("KNOWN_PATH: NON-LEAF = " + (String)nodeInfo);
        }
    }
	
	public void Check_And_Add_Path_To_Tree(String big_path_str, ArrayList<String> filelist) {
		
		String[] strarray = big_path_str.split("/");
		DefaultMutableTreeNode mynode_iter = top_node;
		
		for (int ss=0; ss < strarray.length; ss++ ) {
			
			String searchstr = strarray[ss].trim();
			int found_index = -1;
			
			if (searchstr.length() == 0 && ss == 0)
				continue;
			
			for (int nn=0; nn < mynode_iter.getChildCount(); nn++) {
				DefaultMutableTreeNode child = (DefaultMutableTreeNode)mynode_iter.getChildAt(nn);
				Object myobj = child.getUserObject();
				String mystr = (String)myobj;
				if (mystr.equals(searchstr)) {
					found_index = nn;
					break;
				}
			}
			
			if (found_index == -1) { //create new node
				DefaultMutableTreeNode my_new_node = new DefaultMutableTreeNode(searchstr);
				mynode_iter.add(my_new_node);
				
				mynode_iter = my_new_node;
			}
			else { //node found
				mynode_iter = (DefaultMutableTreeNode)mynode_iter.getChildAt(found_index);
			}
		}//for
		
		//mynode_iter now points to folder for inserting filenames from filelist
		for (int dd=0; dd < filelist.size(); dd++) {
			DefaultMutableTreeNode myfilename = new DefaultMutableTreeNode(filelist.get(dd));
			mynode_iter.add(myfilename);
		}
		
		//repaint tree in gui
		((DefaultTreeModel)mytree.getModel()).reload();
	}//Check_And_Add_Path_To_Tree

}//class
