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
    ArrayList<Caste> castes;
    ArrayList<Edge> edges;
    

    public Components() {
        this.castes = new ArrayList<>();
        this.edges = new ArrayList<>();
    }
    
    /**
     * Add A Caste To Data
     * @param caste the caste to add
     */
    public void addCaste(Caste caste){
        this.castes.add(caste);
        triggerUpdate();
    } // END OF addCaste
    
    /**
     * Add A Edge To Data
     * @param edge 
     */
    public void addEdge(Edge edge){
        this.edges.add(edge);
        triggerUpdate();
    } // END OF addEdge
    
    /**
     * Accessor method for castes
     * @return the castes
     */
    public ArrayList<Caste> getCastes(){
        triggerUpdate();
        return this.castes;
    } // END OF getCastes
    
    /**
     * Accessor methods for edges
     * @return 
     */
    public ArrayList<Edge> getEdges(){
        triggerUpdate();
        return this.edges;
    } // END OF getEdges
    
    /**
     * Used to update the view
     */
    public void triggerUpdate(){
        setChanged();
        notifyObservers();
    } // END OF triggerUpdate
    
    /**
     * Remove caste from data
     * @param caste the caste to remove
     */
    public void removeCaste(Caste caste){
        this.castes.remove(caste);
        triggerUpdate();
    } // END OF removeCaste
    
    /**
     * Remove Edge from data
     * @param edge the edge to remove 
     */
    public void removeEdge(Edge edge){
        this.edges.remove(edge);
        triggerUpdate();
    } // END OF removeEdge
    
    /**
     * Return Caste based on point
     * @param p the point to get Caste from
     * @return the Caste
     */
    public Caste getIntersectingCaste(Point p){
        Caste returnCaste = null;
        for(Caste n : this.castes){
            if(n.getBounds().contains(p.x, p.y))
                returnCaste = n;
        }
        return returnCaste;
    } // END OF getIntersectingCaste
    
    /**
     * Return Edge based on point
     * @param p the point to get Edge from
     * @return the Edge
     */
    public Edge getIntersectingEdge(Point p){
        Edge returnEdge = null;
        for(Edge e : this.edges){
            if(e.getBounds().contains(p.x, p.y))
                returnEdge = e;
        }
        return returnEdge;
    } // END OF getIntersectingEdge
}
