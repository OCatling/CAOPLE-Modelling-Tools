/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author Oliver
 */
public class Caste extends Rectangle implements Piece{
    private String name;
    private ArrayList<String> states;
    private ArrayList<String> actions;
    private boolean selected;
    private boolean nameOnly;
    private Dragpoint[] dragpoints;

    public Caste(int x, int y, int width, int height) {
        super(x, y, width, height);
        initFields();
        initDragpoints();
    }
    
    // Default size constructor
    public Caste(int x, int y){
        super(x, y, 200, 300);
        initFields();
        initDragpoints();
    }
    
    private void initFields(){
        this.name = "";
        this.states = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.selected = false;
        this.nameOnly = false;
        this.dragpoints = new Dragpoint[4];
    }
    
    private void initDragpoints(){
        this.dragpoints[0] = new Dragpoint(x, y, Const.TOP_LEFT);
        this.dragpoints[1] = new Dragpoint(x + width, y, Const.TOP_RIGHT);
        this.dragpoints[2] = new Dragpoint(x + width, y + height, Const.BOTTOM_RIGHT);
        this.dragpoints[3] = new Dragpoint(x, y + height, Const.BOTTOM_LEFT);
    }

/* ----------------> ACCESSOR AND MUTATOR FOR CASTE DETAILS <---------------- */
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the states
     */
    public ArrayList<String> getStates() {
        return states;
    }

    /**
     * @param states the states to set
     */
    public void setStates(ArrayList<String> states) {
        this.states = states;
    }

    /**
     * @return the actions
     */
    public ArrayList<String> getActions() {
        return actions;
    }

    /**
     * @param actions the actions to set
     */
    public void setActions(ArrayList<String>  actions) {
        this.actions = actions;
    }
    
    /**
     * @param state the value to add to states
     */
    public void addState(String state){
        this.states.add(state);
    }
    
    /**
     * @param action the value to add to actions 
     */
    public void addAction(String action){
        this.actions.add(action);
    }
    
    /**
     * Accessor method for selected
     * @return the selected
     */
    

/* ----------------> METHODS FOR HANDLING CASTE SELECTION <------------------ */
    @Override
    public Boolean isSelected(){
        return selected;
    } // END OF isSelected
    
    /**
     * Mutator method for selected
     * @param selected the boolean to set
     */
    @Override
    public void setSelected(boolean selected){
        this.selected = selected;
    } // END OF setSelected
    

/* -------------> METHODS FOR SETTING THE CASTE AS JUST A NAME <------------- */
    /**
     * Accessor method for nameOnly
     * @return the nameOnly
     */ 
    @Override
    public Boolean isNameOnly(){
        return nameOnly;
    } // END OF isNameOnly
    
    /**
     * Mutator method for nameOnly
     * @param nameOnly the boolean to set
     */
    @Override
    public void setNameOnly(Boolean nameOnly){
        this.nameOnly = nameOnly;
    } // END OF setNameOnly
    

/* ------------------> METHODS FOR HANDLING THE DRAGPOINTS <----------------- */
    /**
     * Checks whether the point is located within one of the Dragpoints
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the boolean 
     */
    @Override
    public Boolean isPointInDragpoint(int x, int y){  
        for(Dragpoint point : dragpoints){
            if(point.getBounds().contains(x, y) || point.contains(x, y)) return true;
        }
        return false;
    }
    
    /**
     * Returns the dragpoint the point is located within;
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the dragpoint
     */
    @Override
    public Dragpoint getDragpointFromPoint(int x, int y){
        for(Dragpoint point : this.dragpoints){
            if(point.getBounds().contains(x, y) || point.contains(x, y)) return point;
        }
        return null;
    } // END OF getDragpointFromPoint
    
    @Override
    public Dragpoint[] getDragpoints(){
        return this.dragpoints;
    } // END OF getDragpoints

    @Override
    public void moveDragpoints(int dx, int dy) {
        for(Dragpoint point : this.dragpoints) point.translate(dx, dy);
    } //END OF moveDragpoints
    

/* ---------------> METHODS FOR MOVING AND SCALING THE CASTE <--------------- */
    /**
     * Move The Caste
     * @param dx the dx (the distance to move x)
     * @param dy the dy (the distance to move y)
     */
    @Override
    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    } // END OF move
    
    @Override
    public void scale(int dx, int dy){
        width -= dx;
        height -= dy;
    } // END OF scale
    
    @Override
    public void scaleWidth(int dx){
        width -= dx;
    } // END OF scaleWidth
    
    @Override
    public void scaleHeight(int dy){
        height -= dy;
    } // END OF scaleHeight
    
    @Override
    public Boolean isInBounds(Point p){
        return this.getBounds().contains(p.x, p.y) 
                || this.isPointInDragpoint(p.x, p.y);
    }

} // END OF Caste