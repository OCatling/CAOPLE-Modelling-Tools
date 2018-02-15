/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Point;
import model.edge.Edge;
import java.util.ArrayList;
import java.util.Observable;

/**
 *
 * @author Oliver
 */
public class Components extends Observable{
    ArrayList<Node> nodes;
    ArrayList<Edge> edges;
    

    public Components() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
    }
    
    public void addNode(Node node){
        this.nodes.add(node);
        triggerUpdate();
    }
    
    public void addEdge(Edge edge){
        this.edges.add(edge);
        triggerUpdate();
    }
    
    public ArrayList<Node> getNodes(){
        triggerUpdate();
        return this.nodes;
    }
    
    public ArrayList<Edge> getEdges(){
        triggerUpdate();
        return this.edges;
    }
    
    public void triggerUpdate(){
        setChanged();
        notifyObservers();
    }
    
    public void removeNode(Node n){
        this.nodes.remove(n);
    }
    
    public void removeEdge(Edge e){
        this.edges.remove(e);
    }
    
    public Node getIntersectingNode(Point p){
        Node returnNode = null;
        for(Node n : this.nodes){
            if(n.getBounds().contains(p.x, p.y))
                returnNode = n;
        }
        return returnNode;
    }
}
