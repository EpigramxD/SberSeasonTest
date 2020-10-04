package org.test;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private Node parent = null;
    private int number;
    private String name;
    private String value = "";
    private List<Node> children;

    public Node(int number, String name) {
        this.number = number;
        this.name = name;
    }

    public void add(Node node) {
        if(this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(node);
    }

    public void write(NodeWriter writer) {
        writer.write(this);
        if(this.children != null) {
            for(Node node : this.children) {
                node.write(writer);
            }
        }
    }

    public void remove(Node node) {
        this.children.remove(node);
    }

    public List<Node> getChildren() {
        return this.children;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        if(parent != null) {
            this.parent = parent;
            parent.add(this);
        }
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
