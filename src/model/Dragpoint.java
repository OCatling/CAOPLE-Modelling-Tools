/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Polygon;

/**
 *
 * @author Oliver
 */
public class Dragpoint extends Polygon{
    
    public Dragpoint(int startingX, int startingY){
        this.npoints = 4;
        initXPoints(startingX);
    }
    
    private void initXPoints(int x){
        this.xpoints[0] = x;
        this.xpoints[1] = x + 6;
        this.xpoints[2] = x;
        this.xpoints[3] = x + 6;
    }
    
    private void initYPoints(int y){
        this.ypoints[0] = y;
        this.ypoints[1] = y;
        this.ypoints[2] = y + 6;
        this.ypoints[3] = y + 6;
    }
}
