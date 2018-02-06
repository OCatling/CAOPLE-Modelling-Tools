package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Iterator;


/**
 *
 * @author Oliver
 */
public class CAOPLEModellingTools extends JFrame {
    private final Controller controller;
    private static final String[] COMPONENTS = {"Caste", "Interaction", 
        "Composition", "Aggregation", "Inheritence",
        "Migration"};
    private ModellingBoard board;
    
    
    public static void main(String[] args){
        CAOPLEModellingTools modelling = new CAOPLEModellingTools();
    }
    
    public CAOPLEModellingTools(){
        this.controller = new Controller(); 
        this.setSize(800, 800);
        this.setTitle("CAOPLE Modelling Tools");
        this.add(createComponentButtons(COMPONENTS), BorderLayout.WEST);
        this.add(new ModellingBoard(), BorderLayout.CENTER);
        
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    
    public void toggleAvailaibilty(){
        if(this.isEnabled()){
            this.setEnabled(false);
        } else {
            this.setEnabled(true);
        }
        
    }
    
    /*----- Button Creation Methods -----*/
    private Box createComponentButtons(String[] buttonTitles){
        Box btnBox = Box.createVerticalBox();
        for(int i = 0; i < buttonTitles.length; i++){
            btnBox.add(createComponentButton(buttonTitles[i], i));
        }
        return btnBox;
    }
    
    private JButton createComponentButton(String name, final int action){
        JButton btn = new JButton();
        btn.setToolTipText(name);
        btn.setActionCommand(name);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setIcon(new ImageIcon("src/"+name+".png"));
        
        btn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.handleComponentButton(e);
            } // END OF actionPerformed
        }); // END OF addActionListener
        return btn;
    }
    
    private class ModellingBoard extends JComponent{
        private ArrayList<Shape> shapes;
        private Point start, finish;
        
        public ModellingBoard(){
            this.shapes = new ArrayList<>();
            
            
            this.addMouseListener(new MouseAdapter(){
                
                @Override
                public void mousePressed(MouseEvent event){
                    start = new Point(event.getX(), event.getY());
                    finish = start;
                    repaint();
                } // END OF mousePressed
                
                @Override
                public void mouseReleased(MouseEvent event){
                    Shape shape = drawRectangle(start.x, start.y, 
                            event.getX(), event.getY());
                    shapes.add(shape);
                    
                    start = null;
                    finish = null;
                    repaint();
                } // END OF mouseReleased
            }); // END of addMouseListener
            
            this.addMouseMotionListener(new MouseMotionAdapter(){
                
                @Override
                public void mouseDragged(MouseEvent event){
                    finish = new Point(event.getX(), event.getY());
                    repaint();
                } // END OF mouseDragged
            }); // END OF addMouseMotionListener
            
        } // END OF ModellingBoard Constructor
        
        @Override
        public void paint(Graphics g){
            Graphics2D graphics = (Graphics2D)g;
            
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 1.0f));
            graphics.setStroke(new BasicStroke(3));
            
            shapes.stream().forEach((s) -> {
                graphics.draw(s);
            }); // END OF stream
            
            if(start != null && finish != null){
                graphics.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 0.4f));
                graphics.setPaint(Color.BLUE);
                Shape shape = drawRectangle(start.x, start.y, 
                        finish.x, finish.y);
                graphics.draw(shape);
            } //END OF if 
        } //END OF paint
        
        private Rectangle2D.Float drawRectangle(int startX, int startY, 
                int finishX, int finishY){
            
            int x = Math.min(startX, finishX);
            int y = Math.min(startY, finishY);
            
            int width = Math.abs(startX - finishX);
            int height = Math.abs(startY - finishY);
            
            return new Rectangle2D.Float(x, y, width, height);
            
        }
        
    } // END OF ModellingBoard
    
    
    
    
      
}
