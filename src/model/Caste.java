/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author Oliver
 */
public class Caste extends Rectangle{
    private String name;
    private ArrayList<String> states;
    private ArrayList<String> actions;
    private boolean selected;
    private boolean nameOnly;

    public Caste(int x, int y, int width, int height) {
        super(x, y, width, height);
        initFields();
    }
    
    // Default size constructor
    public Caste(int x, int y){
        super(x, y, 200, 300);
        initFields();
    }
    
    private void initFields(){
        this.name = "";
        this.states = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.selected = false;
        this.nameOnly = false;
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
    public boolean isSelected(){
        return selected;
    }
    
    /**
     * Mutator method for selected
     * @param selected the boolean to set
     */
    public void setSelected(Boolean selected){
        this.selected = selected;
    }
    
    /**
     * Accessor method for nameOnly
     * @return the nameOnly
     */
    public boolean isNameOnly(){
        return nameOnly;
    }
    
    /**
     * Mutator method for nameOnly
     * @param nameOnly the boolean to set
     */
    public void setNameOnly(Boolean nameOnly){
        this.nameOnly = nameOnly;
    }
    
}
