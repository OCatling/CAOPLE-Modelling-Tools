/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import model.*;
import model.edge.Aggregation;
import model.edge.Composition;
import model.edge.Edge;
import model.edge.Inheritence;
import model.edge.Interaction;
import model.edge.Migration;

/**
 *
 * @author Oliver
 */
public class Controller {
    private final Pieces model;
    private final CAOPLEModellingTools view;
    
    private Point start, finish;
    private static final int MOVE = 1;
    private static final int SCALE = 2;
    private String currentAction;
    private int selectAction;
    private int selectedDragpoint;
    
    
    public Controller(Pieces model, CAOPLEModellingTools view){
        this.model = model;
        this.view = view;
    }
    
    /* ------------------------> MOUSE EVENT HANDLER <----------------------- */
   /*public void handleMousePress(MouseEvent event){
        start = event.getPoint();
        finish = start; 
        //Double Click Handler
        if( currentAction.equalsIgnoreCase("Select") && event.getClickCount() == 2){
            if(model.getIntersectingPiece(start) != null){
                Piece c = model.getIntersectingPiece(start);
                if(c instanceof Caste) new CAOPLEModellingTools.CasteDetails((Caste) c); 
                else if(c instanceof Interaction) new CAOPLEModellingTools.EdgeDetails((Edge) c);
                view.toggleAvailaibilty();
            }
        } else if (currentAction.equalsIgnoreCase("Select") && event.getClickCount() == 1){
            if(model.getIntersectingPiece(start) != null){
                if(model.getIntersectingPiece(start).isPointInDragpoint(start.x, start.y)){
                    selectedDragpoint = model.getIntersectingPiece(start).getDragpointFromPoint(start.x, start.y).getLocation();
                    selectAction = SCALE;
                }
                else selectAction = MOVE;
            }
        }
    }*/
   
   /*public void handleMouseRelease(MouseEvent event){
       Piece p;
                    if(currentAction.equals(Const.SELECT) ){
                        if(model.getIntersectingPiece(finish) != null){
                            p = model.getIntersectingPiece(finish);
                            if((p.isSelected() && start == finish) || selectAction == SCALE) 
                                model.getIntersectingPiece(finish).setSelected(false);
                            else model.getIntersectingPiece(finish).setSelected(true);
                        }
                    }
                    else if(currentAction.equals(Const.CASTE) 
                            && model.getIntersectingPiece(start) == null ){
                        Caste c = createCaste();
                        CAOPLEModellingTools.CasteDetails details = new CAOPLEModellingTools.CasteDetails(c);
                        model.addPiece(details.caste);   
                        view.toggleAvailaibilty();
                    }
                    else if(currentAction.equalsIgnoreCase(Const.INTERACTION) 
                            && model.getIntersectingPiece(start) != null
                            && model.getIntersectingPiece(finish) != null){
                        Interaction i = new Interaction(start.x, start.y, event.getX(), event.getY());
                        CAOPLEModellingTools.EdgeDetails e = new CAOPLEModellingTools.EdgeDetails(i);
                        model.addPiece(e.edge);
                        view.toggleAvailaibilty();
                    } 
                    else if(currentAction.equalsIgnoreCase(Const.COMPOSITION) 
                            && model.getIntersectingPiece(start) != null
                            && model.getIntersectingPiece(finish) != null){
                        model.addPiece(new Composition(start.x, start.y, event.getX(), event.getY() ));
                    }
                    else if(currentAction.equalsIgnoreCase(Const.AGGREGATION) 
                            && model.getIntersectingPiece(start) != null
                            && model.getIntersectingPiece(finish) != null){
                        model.addPiece(new Aggregation(start.x, start.y, event.getX(), event.getY() ));
                    }
                    else if(currentAction.equalsIgnoreCase(Const.INHERITENCE) 
                            && model.getIntersectingPiece(start) != null
                            && model.getIntersectingPiece(finish) != null){
                        model.addPiece(new Inheritence(start.x, start.y, event.getX(), event.getY() ));
                    }
                    else if(currentAction.equalsIgnoreCase(Const.MIGRATION) 
                            && model.getIntersectingPiece(start) != null
                            && model.getIntersectingPiece(finish) != null){
                        model.addPiece(new Migration(start.x, start.y, event.getX(), event.getY() ));
                    
                    } else {
                        JOptionPane.showMessageDialog(null, currentAction+ " Must Be Between Two Castes");
                    }// END if
             
                    start = null;
                    finish = null;
                    view.repaint();
   }*/
    
    /* ------------------------> MOVE & SCALE PIECES <----------------------- */
    /**
     * Handles the moving of Pieces upon drag
     * @param dx the distance to move x
     * @param dy the distance to move y
     */
    private void movePiece(){
        int dx =  finish.x - start.x;
        int dy =  finish.y - start.y;
        for(Piece p : model.getPieces()){
            if(p.getBounds().contains(start)){
                p.translate(dx, dy);
                p.moveDragpoints(dx, dy);
                start.x += dx;
                start.y += dy;
            }
        } 
    } // END OF moveNode
    
    private void scalePiece(){
            int dx =  finish.x - start.x;
            int dy =  finish.y - start.y;
            for(Piece p : model.getPieces()){
                if(p.isSelected()){
                    switch (selectedDragpoint) {
                        case Const.TOP_LEFT:
                            p.getDragpoints()[this.selectedDragpoint].translate(dx, dy);
                            p.getDragpoints()[Const.TOP_RIGHT].translate(0, dy);
                            p.getDragpoints()[Const.BOTTOM_LEFT].translate(dx, 0);
                            p.translate(dx, dy);
                            p.scale(dx, dy);
                            break;
                        case Const.TOP_RIGHT:
                            if(p.isNameOnly()){
                                p.getDragpoints()[Const.TOP_RIGHT].translate(dx, 0);
                                p.scaleWidth(-dx);
                            } else {
                                p.getDragpoints()[Const.TOP_RIGHT].translate(dx, dy);
                                p.getDragpoints()[Const.TOP_LEFT].translate(0, dy);
                                p.scale(-dx, dy);
                                p.translate(0, dy);
                            }
                            p.getDragpoints()[Const.BOTTOM_RIGHT].translate(dx, 0);
                            break;
                        case Const.BOTTOM_LEFT:
                            if(p.isNameOnly()){
                                p.getDragpoints()[Const.BOTTOM_LEFT].translate(dx, 0);
                                p.getDragpoints()[Const.TOP_LEFT].translate(dx, 0);
                                p.translate(dx, 0);
                                p.scaleWidth(dx);
                            } else {
                                p.getDragpoints()[Const.BOTTOM_LEFT].translate(dx, dy);
                                p.getDragpoints()[Const.BOTTOM_RIGHT].translate(0, dy);
                                p.getDragpoints()[Const.TOP_LEFT].translate(dx, 0);
                                p.scale(dx, -dy);
                                p.translate(dx, 0);
                            }
                            break;
                        case Const.BOTTOM_RIGHT:
                            p.getDragpoints()[Const.TOP_RIGHT].translate(dx, 0);
                            if(p.isNameOnly()){
                                p.getDragpoints()[Const.BOTTOM_RIGHT].translate(dx, 0);
                                p.scaleWidth(-dx);
                            } else {
                                p.getDragpoints()[Const.BOTTOM_RIGHT].translate(dx, dy);
                                p.getDragpoints()[Const.BOTTOM_LEFT].translate(0, dy);
                                p.scale(-dx, -dy);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
            start.x += dx;
            start.y += dy;
            
        }
    /*public void closeCasteDetails(){
        saveData();
        toggleAvailaibilty();
        popup.dispose();
    }*/
    
    public void deleteSelected(){
        System.out.println("WAKAK");
        model.getPieces().stream().filter((piece) 
                -> (piece.isSelected())).forEach((piece) -> {
            model.removePiece(piece);
        });
    }
}
