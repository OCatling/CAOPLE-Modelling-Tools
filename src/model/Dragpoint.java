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
    private final int location;
    
    public Dragpoint(int startingX, int startingY, int location){
        this.npoints = 4;
        this.location = location;
        initXPoints(startingX );
        initYPoints(startingY );
        
    }

    public Dragpoint(float startingX, float startingY, int location) {
        this.npoints = 4;
        this.location = location;
        initXPoints((int) startingX);
        initYPoints((int) startingY);
    }
    
    private void initXPoints(int x){
        x -= 4;
        super.xpoints[0] = x;
        super.xpoints[1] = x + 8;
        super.xpoints[2] = x + 8;
        super.xpoints[3] = x ;
    }
    
    private void initYPoints(int y){
        y -= 4;
        super.ypoints[0] = y;
        super.ypoints[1] = y;
        super.ypoints[2] = y + 8;
        super.ypoints[3] = y + 8;
    }
    
    public void move(int x, int y){
        initXPoints(x);
        initYPoints(y);
    }
    
    @Override
    public void translate(int dx, int dy){
        for(int p = 0; p < npoints; p++ ){
            super.xpoints[p] += dx;
            super.ypoints[p] += dy;
            super.invalidate();
        }
    }
    
    public int getLocation(){ return this.location; } // END OF getLocation
    
    public void view(){
        System.out.println("| - DRAGPOINT: " + this.location + " - |");
        for(int p = 0; p < super.npoints; p++){
            System.out.println(super.xpoints[p] + " | " + super.ypoints[p]);
        }
    }
}
