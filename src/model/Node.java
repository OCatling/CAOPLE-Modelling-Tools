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
public abstract class Node extends Rectangle2D.Float {
    private boolean selected;
    
    public Node(float x, float y, float width, float height){
        super(x, y, width, height);
        this.selected = false;
    }
    
    // Default size constructor
    public Node(float x, float y){
        super(x, y, 200, 300);
        this.selected = false;
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
    
    
}
