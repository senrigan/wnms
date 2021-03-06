package com.gdc.nms.robot.gui.tree;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.gdc.nms.robot.util.JavaProcessInfo;
import com.gdc.nms.robot.util.JpsManager;

import PAchitaTest.gui.tree.Element;

public class TestTree {

    public static void main(String[] args) {
        new TestTree();
    }

    public TestTree() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }
                
                JTree tree = new JTree(new TestModel(getDataForTree()));

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(new JScrollPane(tree));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
    
    private Element getDataForTree(){
    	Element element=new Element("Servicios");
    	String args[]={"-v"};
    	ArrayList<JavaProcessInfo> runningApps = JpsManager.getRunningApps(args);
    	for (JavaProcessInfo javaProcessInfo : runningApps) {
    		String data = javaProcessInfo.getData();
    		if(data.contains("Robot")){
    			String[] split = data.split("=");
    			String idenfy=split[1];
    			if(idenfy.contains("Robot")){
    				element.add(new Element(idenfy));
    			}
    			
    		}
		}
    	System.out.println(runningApps);
    	return element;
    }

    public static class TestModel implements TreeModel {

        private Element data;

        TestModel(Element data) {
//            data = new Element("data");
//
//            data.add(new Element("One"));
//            data.add(new Element("Two"));
//            data.add(new Element("Three"));
//            data.add(new Element("Four"));
//            data.add(new Element("Five"));
        	this.data=data;
        }

        @Override
        public Object getRoot() {
            return data;
        }

        @Override
        public Object getChild(Object parent, int index) {

            System.out.println("GetChild from " + parent + " @ " + index);

            if (parent instanceof Element) {
                Element p = (Element) parent;
                Object child = p.getChildAt(index);

                System.out.println("child = " + child);
                return child;
            }

            return null;
        }

        @Override
        public int getChildCount(Object parent) {

            if (parent instanceof Element) {
                Element e = (Element) parent;
                System.out.println("childCount = " + parent + "; " + e.getChildCount());
                return e.getChildCount();
            }

            return 0;
        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {

            if (parent instanceof Element && child instanceof Element) {
                Element e = (Element) parent;

                System.out.println("indexOf " + child + " in " + parent + " is " + e.getIndex((Element)child));
                return e.getIndex((Element)child);
            }

            return -1;

        }

        @Override
        public boolean isLeaf(Object node) {
            //List<? super ArrayList> d = (List<? super ArrayList>) node;

            if (node instanceof Element) {
                Element e = (Element) node;
                System.out.println("isLeaf " + e + "; " + (e.getChildCount() == 0));
                return e.getChildCount() == 0;
            }

            return true;
        }

        @Override
        public void valueForPathChanged(TreePath path, Object newValue) {

        }

        @Override
        public void addTreeModelListener(TreeModelListener l) {

        }

        @Override
        public void removeTreeModelListener(TreeModelListener l) {

        }
    }

    
}