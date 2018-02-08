/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.geom.Rectangle2D;

/**
 *
 * @author Oliver
 */
public class Caste extends Node{
    private String name;
    private String states;
    private String actions;

    public Caste(float x, float y, float width, float height) {
        super(x, y, width, height);
    }
    
    // Default size constructor
    public Caste(float x, float y){
        super(x, y, 200, 300);
    }

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
    public String getStates() {
        return states;
    }

    /**
     * @param states the states to set
     */
    public void setStates(String states) {
        this.states = states;
    }

    /**
     * @return the actions
     */
    public String getActions() {
        return actions;
    }

    /**
     * @param actions the actions to set
     */
    public void setActions(String actions) {
        this.actions = actions;
    }

    
}
