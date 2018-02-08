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
public abstract class Node extends Rectangle2D.Float{
    
    public Node(float x, float y, float width, float height){
        super(x, y, width, height);
    }
    
    // Default size constructor
    public Node(float x, float y){
        super(x, y, 200, 300);
    }
    
    
}
