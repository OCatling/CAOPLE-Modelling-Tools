/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.edge;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import model.Caste;
import model.Const;
import model.Dragpoint;
import model.Piece;

/**
 *
 * @author Oliver
 */
public abstract class Edge extends Line2D implements Piece {
    public float x1, y1, x2, y2;
    private boolean selected;
    private String title;
    private Dragpoint[] dragpoints;
    
    private Caste[] targetNodes;
    
    public Edge(double x1, double y1, double x2, double y2){
        this.x1 = (float) x1;
        this.y1 = (float) y1;
        this.x2 = (float) x2;
        this.y2 = (float) y2;
        this.selected = false;
        this.title = "";
        this.dragpoints = new Dragpoint[2];
        this.targetNodes = new Caste[2];
        initDragpoints();
    }
    
    public Edge(float x1, float y1, float x2, float y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.selected = false;
        this.title = "";
        this.targetNodes = new Caste[2];
        this.dragpoints = new Dragpoint[2];
        initDragpoints();
    }
    
    private void initDragpoints(){
        this.dragpoints[Const.EDGE_START] = new Dragpoint(x1, y1, 0);
        this.dragpoints[Const.EDGE_END] = new Dragpoint(x2, y2, 1);
    }

    
/* ----------------------> ACCESSOR METHODS FOR POINTS <--------------------- */
    @Override
    public double getX1() {
        return this.x1;
    }

    @Override
    public double getY1() {
        return this.y1;
    }

    @Override
    public Point2D getP1() {
        return new Point((int) x1, (int) y1);
    }

    @Override
    public double getX2() {
        return this.x2;
    }

    @Override
    public double getY2() {
        return this.y2;
    }

    @Override
    public Point2D getP2() {
        return new Point((int) x2, (int) y2);
    }

    @Override
    public void setLine(double x1, double y1, double x2, double y2) {
        this.x1 = (float) x1;
        this.y1 = (float) y1;
        this.x2 = (float) x2;
        this.y2 = (float) y2;
    }

    @Override
    public Rectangle2D getBounds2D() {
        float x, y, w, h;
            if (x1 < x2) {
                x = x1;
                w = x2 - x1;
            } else {
                x = x2;
                w = x1 - x2;
            }
            if (y1 < y2) {
                y = y1;
                h = y2 - y1;
            } else {
                y = y2;
                h = y1 - y2;
            }
            return new Rectangle2D.Float(x, y, w, h);
    }

    
/* ----------------> ACCESSOR AND MUTATOR METHODS FOR LABEL <---------------- */
    /**
     * @return the title
     */
    public String getLabel() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setLabel(String title) {
        this.title = title;
    }
/* ----------------> METHODS FOR HANDLING NAMING CONVENTIONS <--------------- */
    @Override
    public Boolean isNameOnly(){return false;}
    @Override
    public void setNameOnly(Boolean nameOnly){}
/* ----------------> METHODS FOR HANDLING CASTE SELECTION <------------------ */    
    /**
     * Accessor method for selected
     * @return the selected
     */
    @Override
    public Boolean isSelected(){
        return selected;
    }
    
    /**
     * Mutator method for selected
     * @param selected the boolean to set
     */
    @Override
    public void setSelected(boolean selected){
        this.selected = selected;
    }
    
    @Override
    public Boolean isInBounds(Point p){
        return this.getBounds().contains(p.x, p.y) 
                || this.isPointInDragpoint(p.x, p.y);
    }

    
/* ------------------> METHODS FOR HANDLING THE DRAGPOINTS <----------------- */
    /**
     * Checks whether the point is located within one of the Dragpoints
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the boolean 
     */
    @Override
    public Boolean isPointInDragpoint(int x, int y){ 
        return getDragpointFromPoint(x, y) != null;   
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
            if(point.getBounds().contains(x, y)) return point;
        }
        return null;
    } // END OF getDragpointFromPoint
    
    /**
     * Accessor method for the dragpoints
     * @return the dragpoints
     */
    @Override
    public Dragpoint[] getDragpoints(){ return this.dragpoints; } // END OF getDragpoints
    
    /**
     * Method to move all dragpoints
     * @param dx the dx (distance to move x)
     * @param dy the dy (distance to move y)
     */
    @Override
    public void moveDragpoints(int dx, int dy) {
        for(Dragpoint point : this.dragpoints) point.translate(dx, dy);
    } // END OF moveDragpoints
    
/* ---------------> METHODS FOR MOVING AND SCALING THE CASTE <--------------- */
    /**
     * Move The Edge
     * @param dx the dx (the distance to move x)
     * @param dy the dy (the distance to move y)
     */
    @Override
    public void translate(int dx, int dy) {
        this.x1 += dx;
        this.x2 += dx;
        this.y1 += dy;
        this.y2 += dy;
    } // END OF translate
    
    @Override
    public void scale(int dx, int dy){
        this.x1 -= dx;
        this.x2 -= dx;
        this.y1 -= dy;
        this.y2 -= dy;
    }
    @Override
    public void scaleWidth(int dx){
        this.x1 -= dx;
        this.x2 -= dx;
    }
    @Override
    public void scaleHeight(int dy){
        this.y1 -= dy;
        this.y2 -= dy;
    }

/* ------------------> ABSTRACT METHODS FOR ARROW HANDLING <----------------- */
    public abstract BasicStroke getStroke(); 
    
    public abstract Color getArrowFill();
    
    public abstract Polygon createArrowHead();
    
/* -------------------------> TARGET NODE HANDLING <------------------------- */
    
    public void setStartTargetNodes( Caste startC, Caste finishC ){
        this.targetNodes[0] = startC;
        this.targetNodes[1] = finishC;
    }
    
    public void recalculatePoints(){
        recalculateStart();
        recalculateFinish();
    }
    
    private void recalculateStart(){
        x1 = recalculateX(targetNodes[0], x1, x2);
        y1 = recalculateY(targetNodes[0], y1, y2);
    }
    
    private void recalculateFinish(){
        x2 = recalculateX(targetNodes[1], x2, x1);
        y2 = recalculateY(targetNodes[1], y2, y1);
    }
    
    private float recalculateX(Caste c, float startingX, float finishX){
        // Left To Right: If Finish Point To The Right Of Target Node
        if(finishX > c.x + c.width)
            startingX = c.x + c.width;
        // Right To Leeft: If Finish Point To The Left Of Target Node
        else if(finishX < c.x)
            startingX = c.x;
        return startingX;
    }
    
    private float recalculateY(Caste c, float startingY, float finishY){
        if(finishY > c.y + c.height)
            startingY = c.y + c.height;
        // Right To Leeft: If Finish Point To The Left Of Target Node
        else if(finishY < c.y)
            startingY = c.y;
        return startingY;        
    }
    
}
