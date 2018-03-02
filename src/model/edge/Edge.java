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

/**
 *
 * @author Oliver
 */
public abstract class Edge extends Line2D {
    public float x1, y1, x2, y2;
    private boolean selected;
    private String title;
    
    public Edge(double x1, double y1, double x2, double y2){
        this.x1 = (float) x1;
        this.y1 = (float) y1;
        this.x2 = (float) x2;
        this.y2 = (float) y2;
        this.selected = false;
        this.title = "";
    }
    
    public Edge(float x1, float y1, float x2, float y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.selected = false;
        this.title = "";
    }

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
    
    public void translate(int a1, int b1, int a2, int b2){
        this.x1 += a1;
        this.y1 += b1;
        this.x2 += a2;
        this.y2 += b2;
    }
    
    public abstract BasicStroke getStroke(); 
    
    public abstract Color getArrowFill();
    
    public abstract Polygon createArrowHead();
    
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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

}
