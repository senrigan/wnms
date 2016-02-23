package com.gdc.nms.robot.gui;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

public class RobotManager extends JFrame {
	private JTree tree;

	public RobotManager() {
		  DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
	        //create the child nodes
	        DefaultMutableTreeNode vegetableNode = new DefaultMutableTreeNode("Vegetables");
	        DefaultMutableTreeNode fruitNode = new DefaultMutableTreeNode("Fruits");
	 
	        //add the child nodes to the root node
	        root.add(vegetableNode);
	        root.add(fruitNode);
	         
	        //create the tree by passing in the root node
	        tree = new JTree(root);
	        add(tree);
	         
	        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        this.setTitle("JTree Example");       
	        this.pack();
	        this.setVisible(true);
	}
	
	public static void main(String[] args) {
		  SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	                new RobotManager();
	            }
	        });
	}
}
