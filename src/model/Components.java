/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.edge.Edge;
import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Observable;
import model.Node;

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
}
