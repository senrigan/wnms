package com.gdc.nms.robot.gui.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

public  class Element implements TreeNode {

        private List<Element> nodes;
        private Element parent;

        private String name;

        public Element(String n) {

            nodes = new ArrayList<>();
            name = n;
        }

        @Override
        public String toString() {

            return name;
        }

        protected void setParent(Element parent) {
            this.parent = parent;
        }

        public void add(Element node) {
            node.setParent(this);
            nodes.add(node);
        }

        public void remove(Element node) {
            node.setParent(null);
            nodes.remove(node);
        }

        @Override
        public TreeNode getChildAt(int childIndex) {
            return nodes.get(childIndex);
        }

        @Override
        public int getChildCount() {
            return nodes.size();
        }

        @Override
        public TreeNode getParent() {
            return parent;
        }

        @Override
        public int getIndex(TreeNode node) {
            return nodes.indexOf(node);
        }

        @Override
        public boolean getAllowsChildren() {
            return true;
        }

        @Override
        public boolean isLeaf() {
            return nodes.isEmpty();
        }

        @Override
        public Enumeration children() {
            return Collections.enumeration(nodes);
        }
    }