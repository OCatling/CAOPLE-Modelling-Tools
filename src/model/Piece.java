/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Point;
import java.awt.Shape;

/**
 *
 * @author Oliver
 */
public interface Piece extends Shape {
/* ----------------> METHODS FOR HANDLING NAMING CONVENTIONS <--------------- */
    public Boolean isNameOnly();
    public void setNameOnly(Boolean nameOnly);
    
/* ---------------------> METHODS FOR HANDLING SELECTION <------------------- */
    public Boolean isSelected();
    public void setSelected(boolean selected);
    public Boolean isInBounds(Point p);
    
/* ------------------> METHODS FOR HANDLING THE DRAGPOINTS <----------------- */
    public Boolean isPointInDragpoint(int x, int y);
    public Dragpoint getDragpointFromPoint(int x, int y);
    public Dragpoint[] getDragpoints();
    public void moveDragpoints(int dx, int dy);
    
/* ---------------> METHODS FOR MOVING AND SCALING THE CASTE <--------------- */
    public void translate(int dx, int dy); // (move)
    public void scale(int dx, int dy);
    public void scaleWidth(int dx);
    public void scaleHeight(int dy);
}
