/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
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
    
    private Piece selectedPiece;
    
    public Point start, finish;
    private static final int MOVE = 1;
    private static final int SCALE = 2;
    private String currentAction;
    private int selectAction;
    private int selectedDragpoint;
    
    
    public Controller(Pieces model, CAOPLEModellingTools view){
        this.model = model;
        this.view = view;
        
        this.currentAction = Const.SELECT;
    }
    
    public void setCurrentAction(String currentAction){
        this.currentAction = currentAction;
    }
    
/* --------------------------> MOUSE EVENT HANDLER <------------------------- */
   public void handleMousePress(MouseEvent event){
        start = event.getPoint();
        finish = start; 
        
        if( currentAction.equalsIgnoreCase(Const.SELECT)){
            if(event.getClickCount() == 2){  // Double Click Handler
                // If Event Is On A Caste Or Interaction Then Cause Edit Events
                if(model.getIntersectingPiece(start) != null){
                    Piece c = model.getIntersectingPiece(start);
                    if(c instanceof Caste) view.createCastePopup((Caste) c);
                    else if(c instanceof Interaction) view.createEdgePopup((Edge) c);
                    view.toggleAvailaibilty();
                }
            } else if (event.getClickCount() == 1){ // Single Click Handler
                if(model.getIntersectingPiece(start) != null){
                    if(model.getIntersectingPiece(start).isPointInDragpoint(start.x, start.y)){
                        selectedDragpoint = model.getIntersectingPiece(start).getDragpointFromPoint(start.x, start.y).getLocation();
                        selectAction = SCALE;
                    }
                    else selectAction = MOVE;
                } 
            }
        }
    }
   
   public void handleMouseRelease(MouseEvent event){
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
            Caste caste = view.createCastePopup(c);
            model.addPiece(caste);   
            view.toggleAvailaibilty();
        }
        else if(currentAction.equalsIgnoreCase(Const.INTERACTION) 
                && model.getIntersectingPiece(start) != null
                && model.getIntersectingPiece(finish) != null){
            Interaction i = new Interaction(start.x, start.y, event.getX(), event.getY());
            Edge edge = view.createEdgePopup(i);
            addEdge(edge);
            view.toggleAvailaibilty();
        } 
        else if(currentAction.equalsIgnoreCase(Const.COMPOSITION) 
                && model.getIntersectingPiece(start) != null
                && model.getIntersectingPiece(finish) != null){
            Edge edge = new Composition(start.x, start.y, event.getX(), event.getY());
            addEdge(edge);
        }
        else if(currentAction.equalsIgnoreCase(Const.AGGREGATION) 
                && model.getIntersectingPiece(start) != null
                && model.getIntersectingPiece(finish) != null){
            Edge edge = new Aggregation(start.x, start.y, event.getX(), event.getY());
            addEdge(edge);
        }
        else if(currentAction.equalsIgnoreCase(Const.INHERITENCE) 
                && model.getIntersectingPiece(start) != null
                && model.getIntersectingPiece(finish) != null){
            Edge edge = new Inheritence(start.x, start.y, event.getX(), event.getY() );
            addEdge(edge);
        }
        else if(currentAction.equalsIgnoreCase(Const.MIGRATION) 
                && model.getIntersectingPiece(start) != null
                && model.getIntersectingPiece(finish) != null){
            Edge edge = new Migration(start.x, start.y, event.getX(), event.getY() );
            addEdge(edge);

        } else {
            JOptionPane.showMessageDialog(null, currentAction+ " Must Be Between Two Castes");
        }// END if

        start = null;
        finish = null;
        view.repaint();
    }
   
   public void handleMouseDrag(MouseEvent event){
        finish = event.getPoint();
        int dx =  finish.x - start.x;
        int dy =  finish.y - start.y;
        if(currentAction.equalsIgnoreCase(Const.SELECT)){
            if(selectAction == MOVE) moveClickedPiece(dx, dy);
            else if(selectAction == SCALE) scalePiece();
        } 
        view.repaint();
        event.consume();
   }
    
/* -----------------------------> PIECE CONTROL <---------------------------- */
    /**
     * Handles the moving of Pieces upon drag
     * @param dx the distance to move x
     * @param dy the distance to move y
     */
    private void moveClickedPiece(int dx, int dy){
        for(Piece p : model.getPieces()){
            if(p.getBounds().contains(start)){
                movePiece(dx, dy, p);
                start.x += dx;
                start.y += dy;
            }
        } 
    } // END OF moveNode
    
    /**
     * Parameters distance X and distance Y to move the selected Piece(s)
     * @param dx the distance to move x
     * @param dy the distance to move y
     */
    public void moveAllSelected(int dx, int dy){
        for(Piece p : model.getPieces()){
            if(p.isSelected()) 
                movePiece(dx, dy, p);
        }
    }
    
    /**
     * Parameters distance X and distance Y to move the selected Individual Piece
     * @param dx the distance to move x
     * @param dy the distance to move y
     * @param p the Piece
     */
    private void movePiece(int dx, int dy, Piece p){
        p.translate(dx, dy);
        p.moveDragpoints(dx, dy);
    }
    
    /**
     * Decease / Increase The Size Of Selected Piece(s)
     */
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
    
    /**
     * Delete All Selected Pieces
     */
    private void deleteSelected(){
        ArrayList<Piece> dp = new ArrayList<>();
        model.getPieces().stream().filter((p) -> (p.isSelected())).forEach((p) -> {
            dp.add(p);
        });
        dp.stream().forEach((p) -> {
            model.removePiece(p);
        });
    }
    
/* ---------------------------> CREATION METHODS <--------------------------- */
    /**
     * Method to draw a rectangle used by the Modelling Board to create 
     * the drag outline 
     * @return the Caste 
     */
    public Caste createCaste(){
        int x = Math.min(start.x, finish.x);
        int y = Math.min(start.y, finish.y);

        int width = Math.abs(start.x - finish.x);
        int height = Math.abs(start.y - finish.y);
        return new Caste(x, y, width, height); 
    } // END OF createCaste
    
    private void addEdge(Edge edge){
        Caste startingCaste = model.getIntersectingCaste(start);
        Caste finishingCaste = model.getIntersectingCaste(finish);
        edge.setStartTargetNodes(startingCaste, finishingCaste);
        edge.recalculatePoints();
        model.addPiece(edge);
    }
    
    /**
     * Draws the outline as components are dragged
     * @param g2d the graphics package
     */
    public void handleDragOutline(Graphics2D g2d){
        if(start != null && finish != null && !currentAction.equals(Const.SELECT)){
            g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.4f));
            g2d.setPaint(Color.BLUE);
            if(currentAction.equalsIgnoreCase("Caste")){
                g2d.draw(createCaste());
            }       
            else{
                g2d.drawLine(start.x, start.y, finish.x, finish.y);
            }
        }
    } // END OF handleDrag
    
/* -------------------------> KEY PRESS <--------------------------------- */
    /**
     * Set The Delete Key TO Delete Piece(s)
     * @param im the InputMap
     * @param am the ActionMap
     */
    public void setDeleteKey(InputMap im, ActionMap am){
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0, false), "Delete");
        am.put("Delete", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
               deleteSelected();
            }     
        });
    }
    
    /**
     * Set The Left Arrow Key To Move Piece(s) Left
     * @param im the InputMap
     * @param am the ActionMap
     */
    public void setLeftKey(InputMap im, ActionMap am){
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "Left");
        am.put("Left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveAllSelected(-2, 0);
            }
        });
    }
    
    /**
     * Set The Right Arrow Key To Move Piece(s) Right
     * @param im the InputMap
     * @param am the ActionMap
     */
    public void setRightKey(InputMap im, ActionMap am){
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "Right");
        am.put("Right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveAllSelected(2, 0);
            }
        });
    }
    
    /**
     * Set The Right Up Key To Move Piece(s) Up
     * @param im the InputMap
     * @param am the ActionMap
     */
    public void setUpKey(InputMap im, ActionMap am){
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "Up");
        am.put("Up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveAllSelected(0, -2);
            }
        });
    }
    
    /**
     * Set The Down Arrow Key To Move Piece(s) Down
     * @param im the InputMap
     * @param am the ActionMap
     */
    public void setDownKey(InputMap im, ActionMap am){
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "Down");
        am.put("Down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveAllSelected(0, 2);
            }
        });
    }
}
