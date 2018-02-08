/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.edge;

import java.awt.Point;
import java.awt.Polygon;

/**
 *
 * @author Oliver
 */
public class Arrowhead extends Polygon{
    private Point origin;
        
    public Arrowhead(int x1, int y1, Point[] points){
        this.origin = new Point(x1, y1);
        for(Point coordinate : points){
            this.addPoint(coordinate.x, coordinate.y);
        }
    }
}
