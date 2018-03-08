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
import model.edge.Interaction;

/**
 *
 * @author Oliver
 */
public class Pieces extends Observable{
    ArrayList<Piece> components;
    

    public Pieces() {
        this.components = new ArrayList<>();
    }
    
    /**
     * Add Piece to components
     * @param c the component to add
     */
    public void addPiece(Piece c){
        this.components.add(c);
        triggerUpdate();
    } // END OF addPiece

    /**
     * Remove Piece from data
     * @param c the component to remove
     */
    public void removePiece(Piece c){
        this.components.remove(c);
        triggerUpdate();
    } // END OF removePiece
    
    /**
     * Return Caste based on point
     * @param p the point to get Caste from
     * @return the Caste
     */
    public Piece getIntersectingPiece(Point p){
        for(Piece c : this.components)
            if(c.getBounds().contains(p.x, p.y))
                return c;
        triggerUpdate();
        return null;
    } // END OF getIntersectingCaste
    
    /**
     * Used to update the view
     */
    public void triggerUpdate(){
        setChanged();
        notifyObservers();
    } // END OF triggerUpdate
    
    /**
     * Accessor method for components
     * @return the components
     */
    public ArrayList<Piece> getPieces(){
        triggerUpdate();
        return this.components;
    } // END OF getPiece
    
    /**
     * Access The Castes In The Data
     * @return the Castes from the ArrayList
     */
    public ArrayList<Caste> getCastes(){ 
        ArrayList<Caste> results = new ArrayList<>();
        this.components.stream().filter((c) -> (c instanceof Caste))
                .forEach((c) -> { results.add((Caste) c); });
        triggerUpdate();
        return results;
    }
    
    /**
     * Access The Edges In The Data
     * @return the Castes from the Edges
     */
    public ArrayList<Edge> getEdges(){ 
        ArrayList<Edge> results = new ArrayList<>();
        this.components.stream().filter((e) -> (e instanceof Edge))
                .forEach((e) -> { results.add((Edge) e); });
        triggerUpdate();
        return results;
    }
    
    /**
     * Access The Interactions In The Data
     * @return the Interactions from the ArrayList
     */
    public ArrayList<Interaction> getInteractions(){ 
        ArrayList<Interaction> results = new ArrayList<>();
        this.components.stream().filter((i) -> (i instanceof Interaction))
                .forEach((i) -> { results.add((Interaction) i); });
        triggerUpdate();
        return results;
    }
}