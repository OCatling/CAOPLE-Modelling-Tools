package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import model.edge.Aggregation;
import model.Caste;
import model.Components;
import model.edge.Composition;
import model.edge.Edge;
import model.edge.Inheritence;
import model.edge.Interaction;
import model.edge.Migration;
import model.Node;


/**
 *
 * @author Oliver
 */
public class CAOPLEModellingTools extends JFrame implements Observer{
    private Components model;
    private String currentAction;
    private static final String[] COMPONENTS = {"Select", "Caste", "Interaction", 
        "Composition", "Aggregation", "Inheritence",
        "Migration"};
    
    
    public static void main(String[] args){
        CAOPLEModellingTools modelling = new CAOPLEModellingTools();
    }
    
    public CAOPLEModellingTools(){
        this.model = new Components();
        this.model.addObserver(this);
        this.currentAction = "Caste";
        this.setEnabled(true);
        
        this.setSize(1200, 1000);
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
            public void actionPerformed(ActionEvent event) {
                currentAction = event.getActionCommand();;
            } // END OF actionPerformed
        }); // END OF addActionListener
        return btn;
    }

    @Override
    public void update(Observable o, Object arg) {
        this.repaint();
    }
    
    private class ModellingBoard extends JComponent{
        private Point start, finish;
        
        public ModellingBoard(){
                       
            this.addMouseListener(new MouseAdapter(){
                
                @Override
                public void mousePressed(MouseEvent event){
                    start = event.getPoint();
                    finish = start;  
                } // END OF mousePressed
                
                @Override
                public void mouseReleased(MouseEvent event){
                    if(currentAction.equalsIgnoreCase("Select")){
                        
                    }
                    else if(currentAction.equalsIgnoreCase("Caste")){
                        CasteDetails details = new CasteDetails();
                        toggleAvailaibilty();
                        model.addNode(createCaste());   
                    }
                    else if(currentAction.equalsIgnoreCase("Interaction")){
                        model.addEdge(new Interaction(start.x, start.y, event.getX(), event.getY() ));
                    } 
                    else if(currentAction.equalsIgnoreCase("Composition")){
                        model.addEdge(new Composition(start.x, start.y, event.getX(), event.getY() ));
                    }
                    else if(currentAction.equalsIgnoreCase("Aggregation")){
                        model.addEdge(new Aggregation(start.x, start.y, event.getX(), event.getY() ));
                    }
                    else if(currentAction.equalsIgnoreCase("Inheritence")){
                        model.addEdge(new Inheritence(start.x, start.y, event.getX(), event.getY() ));
                    }
                    else if(currentAction.equalsIgnoreCase("Migration")){
                        model.addEdge(new Migration(start.x, start.y, event.getX(), event.getY() ));
                    } // END if
             
                    start = null;
                    finish = null;
                } // END OF mouseReleased
                
            }); // END of addMouseListener
            
            this.addMouseMotionListener(new MouseMotionAdapter(){
                
                @Override
                public void mouseDragged(MouseEvent event){
                    finish = new Point(event.getPoint());
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
            
            for(Node node : model.getNodes()){
                graphics.draw(node);
            }
            for(Edge edge : model.getEdges()){
                graphics.setStroke(edge.getStroke());
                graphics.draw(edge);
                
            }
            
            if(start != null && finish != null){
                graphics.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 0.4f));
                graphics.setPaint(Color.BLUE);
                if(currentAction.equalsIgnoreCase("Caste")){
                    graphics.draw(createCaste());
                }
                else{
                    graphics.drawLine(start.x, start.y, finish.x, finish.y);
                }
            }
        } //END OF paint 
        
        private Caste createCaste(){
            int x = Math.min(start.x, finish.x);
            int y = Math.min(start.y, finish.y);

            int width = Math.abs(start.x - finish.x);
            int height = Math.abs(start.y - finish.y);
            return new Caste(x, y, width, height); 
        }
        
        
            
    } // END OF ModellingBoard
    
    private class CasteDetails extends JFrame{
        private String name;
        private String states;
        private String actions;
        
        // Default Constructor 
        public CasteDetails(){
            this.setAlwaysOnTop(true);
            this.setSize(400, 400);   
            
            this.name = "< NAME >";
            this.states = "< STATES >";
            this.actions = "< ACTIONS >";
            
            
            this.add(createInputs(), BorderLayout.CENTER);
            this.setDefaultCloseOperation(close());
            this.setVisible(true);
        }
        
        // Pre-exisiting Caste constructor
        public CasteDetails(Caste caste){
            this.setAlwaysOnTop(true);
            this.setSize(400, 400);  
            
            this.name = caste.getName();
            this.states = caste.getStates();
            this.actions = caste.getActions();
            
            this.add(createInputs(), BorderLayout.CENTER);
            this.setDefaultCloseOperation(close());
            this.setVisible(true);
        }
        
        private int close(){
            toggleAvailaibilty();
            return 2;
        }
        
        private JPanel createInputs(){
            JPanel inputs = new JPanel();
            

            /*for(int i = 0; i < 3; i++ ){
                inputs.add(createInput());
            }*/
            inputs.add(createInput(this.name));
            inputs.add(createInput(this.states));
            inputs.add(createInput(this.actions));
            return inputs;
        }
        
        private JTextField createInput(String type){
            JTextField input = new JTextField(type);
            input.setBorder(BorderFactory.createCompoundBorder(
            input.getBorder(), BorderFactory.createEmptyBorder(10,10,10,10)));
            return input;
        }
                
    }
    
    
    
    
      
}
