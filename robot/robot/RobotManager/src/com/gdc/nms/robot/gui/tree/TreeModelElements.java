package com.gdc.nms.robot.gui.tree;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
  public  class TreeModelElements implements TreeModel {

        private Element data;

        public TreeModelElements() {
        	data = new Element("Aplication");
        }
        
        public TreeModelElements(Element data) {

        	this.data=data;
        }

        @Override
        public Object getRoot() {
            return data;
        }

        @Override
        public Object getChild(Object parent, int index) {

            if (parent instanceof Element) {
                Element p = (Element) parent;
                Object child = p.getChildAt(index);

                return child;
            }

            return null;
        }

        @Override
        public int getChildCount(Object parent) {

            if (parent instanceof Element) {
                Element e = (Element) parent;
                return e.getChildCount();
            }

            return 0;
        }

        @Override
        public int getIndexOfChild(Object parent, Object child) {

            if (parent instanceof Element && child instanceof Element) {
                Element e = (Element) parent;

                return e.getIndex((Element)child);
            }

            return -1;

        }

        @Override
        public boolean isLeaf(Object node) {
            //List<? super ArrayList> d = (List<? super ArrayList>) node;

            if (node instanceof Element) {
                Element e = (Element) node;
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