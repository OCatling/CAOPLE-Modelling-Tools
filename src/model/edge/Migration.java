/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.edge;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Oliver
 */
public class Migration extends Edge{
    private final static float DASH[] = {10.0f};
    private static final BasicStroke STROKE = new BasicStroke(3.0f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f, DASH, 0.0f);
    private static final Color ARROW_FILL = new Color(0f,0f,0f,.0f );
    
    public Migration(double x1, double y1, double x2, double y2) {
        super(x1, y1, x2, y2);
    }
    
    
    public Migration(float x1, float y1, float x2, float y2){
        super(x1, y1, x2, y2);
    }

    @Override
    public BasicStroke getStroke() {
        return Migration.STROKE;
    }
    
    @Override
    public Color getArrowFill() {
        return Migration.ARROW_FILL;
    }
    
    @Override
    public Polygon createArrowHead(){
        Polygon poly = new Polygon();
        poly.addPoint((int) x2, (int) y2);
        double phi = Math.toRadians(25);
        int barb = 40;
        double dy = y2 - y1;
        double dx = x2 - x1;
        double theta = Math.atan2(dy, dx);
        //System.out.println("theta = " + Math.toDegrees(theta));
        double x, y, rho = theta + phi;
        for(int j = 0; j < 2; j++)
        {
            x = x2 - barb * Math.cos(rho);
            y = y2 - barb * Math.sin(rho);
            poly.addPoint((int)x,(int) y);
            rho = theta - phi;
        }
        
        return poly;
    }
    
}
