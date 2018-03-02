package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
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
    
    private JPanel main;
    
    public CAOPLEModellingTools(){
        this.main = new JPanel(new BorderLayout());
        this.model = new Components();
        this.model.addObserver(this);
        this.currentAction = "Caste";
        this.setEnabled(true);
        this.setTitle("CAOPLE Modelling Tools");
        this.add(createComponentButtons(COMPONENTS), BorderLayout.WEST);        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        main.add(new ModellingBoard(), BorderLayout.CENTER);
        main.setOpaque(true);

        JScrollPane scrollpane = new JScrollPane(main);
        this.add(scrollpane);

        pack();
        this.setVisible(true);
    }
    
    public static void main(String[] args){ new CAOPLEModellingTools(); }
    
    /**
     * Method used to control availability to the frame
     */
    public void toggleAvailaibilty(){
        if(this.isEnabled()){
            this.setEnabled(false);
        } else {
            this.setEnabled(true);
        }
    } // END OF toggleAvailaibilty
    
    /*----- Button Creation Methods -----*/
    /**
     * Method to create all the buttons used for model creation
     * @param buttonTitles the array containing all the names/actions used 
     * by the modelling board
     * @return the buttons 
     */
    private Box createComponentButtons(String[] buttonTitles){
        Box btnBox = Box.createVerticalBox();
        for (String buttonTitle : buttonTitles) {
            btnBox.add(createComponentButton(buttonTitle));
        }
        return btnBox;
    } // END OF createComponentButtons
    
    /**
     * Method to create a button used for choosing the action
     * @param name the name of the component being created
     * @return the button
     */
    private JButton createComponentButton(String name){
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
    } // END OF createComponentButton

    @Override
    public void update(Observable o, Object arg) {
        this.repaint();
    }
    
    /* --- COMPONENT USED FOR CREATING GRAPHICAL REPRESENTATION OF MODEL --- */
    private class ModellingBoard extends JPanel {
        private Point start, finish;
        private static final int FONT_SIZE = 16;
        private final Font font;
        
        public ModellingBoard(){
            super();
            this.font = new Font("TimesRoman", Font.PLAIN, FONT_SIZE);
            this.setPreferredSize(new Dimension(1080, 900));
            this.setBackground(Color.WHITE);
            this.addMouseListener(new MouseAdapter(){
                
                @Override
                public void mousePressed(MouseEvent event){
                    start = event.getPoint();
                    finish = start; 
                    if(currentAction.equalsIgnoreCase("Select") && event.getClickCount() == 2){
                        if(model.getIntersectingCaste(start) != null){
                            Caste c = model.getIntersectingCaste(start);
                            CasteDetails details = new CasteDetails(c);
                            model.removeCaste(c);
                            model.addCaste(details.caste);   
                            toggleAvailaibilty();
                        
                        } else if(model.getIntersectingEdge(start) != null){
                            Edge e = model.getIntersectingEdge(start);
                            if(e instanceof Interaction){
                                EdgeDetails details = new EdgeDetails(e);
                                model.removeEdge(e);
                                model.addEdge(details.edge);   
                                toggleAvailaibilty();
                            }
                        }
                    }
                } // END OF mousePressed
                
                @Override
                public void mouseReleased(MouseEvent event){
                    if(currentAction.equalsIgnoreCase("Select")){
                        for(Caste n : model.getCastes()){
                            if(n.contains(event.getPoint()) && !n.isSelected()){
                               n.setSelected(true);
                            } else {
                                n.setSelected(false);
                            }
                        }
                        for(Edge e : model.getEdges()){
                            if(e.getBounds().contains(event.getPoint()) && !e.isSelected()){
                               e.setSelected(true);
                            } else {
                                e.setSelected(false);
                            }
                        }
                    }
                    else if(currentAction.equalsIgnoreCase("Caste") 
                            && model.getIntersectingCaste(start) == null ){
                        Caste c = createCaste();
                        CasteDetails details = new CasteDetails(c);
                        model.addCaste(details.caste);   
                        toggleAvailaibilty();
                    }
                    else if(currentAction.equalsIgnoreCase("Interaction")){
                        toggleAvailaibilty();
                        Interaction i = new Interaction(start.x, start.y, event.getX(), event.getY());
                        EdgeDetails e = new EdgeDetails(i);
                        model.addEdge(e.edge);
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
                    if(currentAction.equalsIgnoreCase("Select")){
                        double dx = finish.getX() - start.x;
                        double dy = finish.getY() - start.y;
                        moveNode(dx, dy);
                        moveEdge(dx, dy);
                    }
                    repaint();
                } // END OF mouseDragged
                /* ----- MOVE FUNCTIONS ----- *?
                /**
                 * Handles the moving of Nodes upon drag
                 * @param dx the distance to move x
                 * @param dy the distance to move y
                 */
                private void moveNode(double dx, double dy){
                    for(Caste n : model.getCastes()){
                        if(n.contains(start)){
                            n.x += dx;
                            n.y += dy;
                            start.x += dx;
                            start.y += dy;
                        }
                    } 
                } // END OF moveNode
                
                /**
                 * Handles the moving of Nodes upon drag
                 * @param dx the distance to move x
                 * @param dy the distance to move y
                 */
                private void moveEdge(double dx, double dy){
                    for(Edge e : model.getEdges()){
                        if(e.getBounds().contains(start)){
                            e.x1 += dx;
                            e.y1 += dy;
                            e.x2 += dx;
                            e.y2 += dy;
                            start.x += dx;
                            start.y += dy;
                        }
                    }   
                } // END OF moveEdge
            }); // END OF addMouseMotionListener
            
            this.setVisible(true);
            
        } // END OF ModellingBoard Constructor
        
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1000, 800);
        }
        
        @Override
        public void paint(Graphics g){
            super.paintComponent(g);
            // Set-up graphics package
            Graphics2D graphics = (Graphics2D)g;
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 1.0f));
            graphics.setStroke(new BasicStroke(3));
            graphics.setFont(font);

            // Drawing all components
            drawNodes(graphics);
            drawEdges(graphics);
            
            // Handle Drag Outline
            handleDragOutline(g);
        } // END OF paint
        
        
        /* ------------------> METHODS FOR DRAWING SHAPES <------------------ */
        /**
         * Draws all nodes
         * @param g2d the graphics package
         */
        private void drawNodes(Graphics2D g2d){
            int innerHeight;
            for(Caste node : model.getCastes()){
                if(node.isSelected()){
                    handleHighlight(g2d, node);
                } else if(!node.isSelected() && !g2d.getColor().equals(Color.BLACK)) {
                    g2d.setColor(Color.BLACK);
                }
                drawCasteDetailsInnerBoxes(g2d, node);
                drawCasteDetails(g2d, node);
                if(node.isNameOnly()) node.height = 80;
                g2d.draw(node);
            }
        } // END OF drawNodes
        
        /**
         * Draws all edges
         * @param g2d the graphics package
         */
        private void drawEdges(Graphics2D g2d){
            for(Edge edge : model.getEdges()){
                if(edge.isSelected()) g2d.setColor(Color.BLUE);
                else if(!edge.isSelected() && !g2d.getColor().equals(Color.BLACK))
                    g2d.setColor(Color.BLACK);   
                if(!edge.getTitle().isEmpty()){
                    g2d.drawString(edge.getTitle(), edge.x1 + 20, edge.y1 - 20);
                }
                g2d.setStroke(edge.getStroke());
                g2d.draw(edge); 
                g2d.drawPolygon(edge.createArrowHead());
                g2d.setPaint(edge.getArrowFill());
                g2d.fillPolygon(edge.createArrowHead());
            }
        } // END OF drawEdges
        
        private void drawCasteDetails(Graphics2D g2d, Caste node){
            FontMetrics metrics = g2d.getFontMetrics(font);
            
            // Draw Name
            int x = node.x + (node.width - metrics.stringWidth(node.getName())) / 2; // Center Name Text
            int y = node.y + 50;
            g2d.drawString( node.getName(), x, y);
            
            if(!node.isNameOnly()){
                drawStates(g2d, node);
                drawActions(g2d, node);  
            }
        } // END OF drawCasteDetails
        
        /**
         * Sequence to draw the states strings
         * @param g2d the graphics package
         * @param node the Caste to get actions from 
         */
        private void drawStates(Graphics2D g2d, Caste node){
            int y = node.y + 96; // (76:Height from top) + (10:Indent) = 86
            int loopSize = getLoopSize(node, node.getStates());
            drawProperties(g2d, node.getStates(), node.x + 30, y, loopSize);
            if(loopSize != node.getStates().size()) 
                drawContinuation(g2d, node, node.y + 90 + (loopSize * FONT_SIZE));
        } // END OF drawStates
        
        /**
         * Sequence to draw the actions strings
         * @param g2d the graphics package
         * @param node the Caste to get actions from 
         */
        private void drawActions(Graphics2D g2d, Caste node){
            int y = node.y + 90 + ((node.height - 80) / 2);
            int loopSize = getLoopSize(node, node.getActions());
            drawProperties(g2d, node.getActions(), node.x + 30, y, loopSize);
            if(loopSize != node.getActions().size()) 
                drawContinuation(g2d, node, node.y + 90 + ((node.height - 90) / 2) + (loopSize * FONT_SIZE));
        } // END OF drawActions
        
        /**
         * Calculates whether the amount of properties would be larger than the 
         * drawn box
         * @param node the Caste
         * @param comparisonList the list to calculate check size of
         * @return 
         */
        private int getLoopSize(Caste node, ArrayList comparisonList){
            int boxHeight = (node.height - 86) / 2; // Height of the state container
            int loopSize = comparisonList.size();
            if(boxHeight - 10 < loopSize * FONT_SIZE){
                loopSize = (boxHeight / FONT_SIZE) - 2;
            }
            return loopSize;
        }
        
        /**
         * Draws either the actions or states of the Caste
         * @param g2d the graphics package
         * @param list the list from which to iterate the details
         * @param x  the starting x coordinate
         * @param y the starting y coordinate
         * @param loopSize the amount of iterations
         */
        private void drawProperties(Graphics2D g2d, ArrayList<String> list, 
                int x, int y, int loopSize){
            for(int i = 0; i < loopSize; i++){
                g2d.drawString(list.get(i), x, y);
                y += FONT_SIZE;
            }
        } // END OF drawProperties
        
        private void drawContinuation(Graphics2D g2d, Caste node, int startingY){
            FontMetrics metrics = g2d.getFontMetrics(font);
            int x = node.x + (node.width - metrics.stringWidth(node.getName())) / 2; // Center Name Text
            g2d.drawString("-----", x, startingY);
        }
        
        /**
         * draws the boxes which contain the details of the caste 
         * (details added separately)
         * @param g the graphics package
         * @param c the Caste from which the dimensions are calculated
         */
        private void drawCasteDetailsInnerBoxes(Graphics g, Caste c){
            Graphics2D g2d = (Graphics2D)g;
            int y = c.y + 20;
            int height = 50;
            g2d.drawRect( c.x + 20, y, c.width - 40, height);
            y += height;
            height = (c.height - 86) / 2;
            g2d.drawRect( c.x + 20, y, c.width - 40, height);
            y += height;
            g2d.drawRect( c.x + 20, y, c.width - 40, height);
        } // END OF drawCasteDetailsInnerBoxes
        
        /**
         * Draws the outline as components are dragged
         * @param g the graphics package
         */
        private void handleDragOutline(Graphics g){
            Graphics2D g2d = (Graphics2D)g;
            if(start != null && finish != null && currentAction != "Select"){
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
        
        private void handleHighlight(Graphics g, Caste c){
            Graphics2D g2d = (Graphics2D)g;
            g2d.setColor(Color.BLUE);
            g2d.draw(new Rectangle((int) c.x - 3, (int) c.y - 3, 6, 6));
            g2d.draw(new Rectangle((int) (c.x + c.width) - 3, (int) c.y - 3, 6, 6));
            g2d.draw(new Rectangle((int) c.x - 3, (int) (c.y + c.height) - 3, 6, 6));
            g2d.draw(new Rectangle((int) (c.x + c.width) - 3, (int) (c.y + c.height) - 3, 6, 6));
        } // END OF handleHighlight
                
        /**
         * Method to draw a rectangle used by the Modelling Board to create 
         * the drag outline 
         * @return the Caste 
         */
        private Caste createCaste(){
            int x = Math.min(start.x, finish.x);
            int y = Math.min(start.y, finish.y);

            int width = Math.abs(start.x - finish.x);
            int height = Math.abs(start.y - finish.y);
            return new Caste(x, y, width, height); 
        } // END OF createCaste
     
    } // END OF ModellingBoard
    
    /* -----------------> POPUP TO RETRIEVE CASTE DETAILS <----------------- */ 
    private class CasteDetails {
        private JTextField name;
        private ArrayList<JTextField> states;
        private ArrayList<JTextField> actions;
        private JCheckBox nameOnlyCheckbox;
        private JPanel mainPanel;
        private JDialog popup;
        
        private Caste caste; 
        
        private final Insets Insets = new Insets(10, 10, 10, 10);
        private final String[] labelNames = {"Name", "States", "Actions"};
        
        /**
         * The Constructor
         * @param caste the object which data can be read from 
         */
        public CasteDetails(Caste caste){
            this.name = new JTextField();
            this.states = new ArrayList<>();
            this.actions = new ArrayList<>();
            this.nameOnlyCheckbox = new JCheckBox();
            
            this.popup = new JDialog();
            this.mainPanel = new JPanel();
            this.caste = caste;
            
            initJTextFieldStorage();
            initComponents();
        } // END OF constructor

        /* -------------- METHODS FOR CREATING THE JDIOALOG BOX ------------- */
        
        /**
         * Creates the frame (window)
         */
        private void initComponents(){
            popup.setAlwaysOnTop(true);
            popup.setResizable(true);
            popup.setSize(500, 500);
            createTitle();
            createMainPanel();
            JScrollPane scrollPane = new JScrollPane(mainPanel);
            popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            popup.addWindowListener(new WindowAdapter(){
                @Override
                public void windowClosing(WindowEvent windowEvent){
                    saveData();
                    toggleAvailaibilty();
                    popup.dispose();
                }
            });
            
            popup.add(scrollPane);
            popup.setVisible(true);
        } // END OF initComponents
        
        /**
         * Used to create the title of the popup box depending if a caste name 
         * exists or not
         */
        private void createTitle(){
            if(this.caste.getName().isEmpty()){
                this.popup.setTitle("Add New Caste");
            } else {
                this.popup.setTitle("Edit " + caste.getName() + " Details");
            }
        }
        
        /**
         * Builds components within the frame
         */
        private void createMainPanel(){
            mainPanel.setSize(400, 400);
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            createNameSection();
            createStatesSection();
            createActionsSection();
            createBottomSection();
        } // END OF createMainPanel
        
        /*-------------------- METHODS FOR PANEL CREATION --------------------*/
        
        /** 
         * Creates the panel used for the naming input area
         */
        private void createNameSection(){
            JPanel panel = new JPanel();
            panel.add(new JLabel(labelNames[0] + ":"));
            panel.add(name);
            mainPanel.add(panel);
        } // END OF createNamePanel
        
        /**
         * Creates the panel used for the states input area
         */
        private void createStatesSection(){
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(createHeading(labelNames[1] + ":"));
            for(JTextField field : states){
                panel.add(field);
            }
            mainPanel.add(panel);
        } // END OF createStatesSection
        
        /**
         * Creates the panel used for the actions input area
         */
        private void createActionsSection(){
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.add(createHeading(labelNames[2] + ":"));
            for(JTextField field : actions){
                panel.add(field);
            }
            mainPanel.add(panel);
            
        }
        
        private void createBottomSection(){
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.add(new Label("Display Name Only?"));
            panel.add(this.nameOnlyCheckbox);
            panel.add(createSubmitButton());
            mainPanel.add(panel);
        }
        
        /*----- Methods For individual element creation -----*/
        /**
         * Creates The text field used throughout the frame;
         * @return the JTextField
         */
        private JTextField createInput(){
                JTextField input = new JTextField(30);
                input.setPreferredSize(new Dimension(320, 40));
                return input;
        } // END OF createInput 
        
        /**
         * Creates Label heading for the pane
         * @param title the section the heading panel belongs to
         * @return the panel with section title, add and remove buttons
         */
        private JPanel createHeading(String title){
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            GridBagConstraints grid = new GridBagConstraints();
            grid.fill = GridBagConstraints.BOTH;
            grid.insets = new Insets(10, 10, 10, 10);
            
            grid.gridx = 0;
            grid.gridy = 0;
            panel.add(new JLabel(title), grid);
            
            grid.gridx = 1;
            grid.gridy = 0;
            panel.add(createAddButton(title, true), grid);
            
            grid.gridx = 2;
            grid.gridy = 0;
            panel.add(createAddButton(title, false), grid);
            return panel;
        } // END OF createHeading
        
        /**
         * Creates button controlling the amount of input fields 
         * @param title the section the button belongs to
         * @param isPlus the boolean controlling whether this adds or removes fields
         * @return the button
         */
        private JButton createAddButton(String title, Boolean isPlus){
            JButton button = new JButton();
            
            if(isPlus) button.setText("+");
            else button.setText("-");
            
            button.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    
                    if(title.equalsIgnoreCase("Actions:") && isPlus){
                        actions.add(createInput());
                    } else if(title.equalsIgnoreCase("Actions:") && !isPlus 
                            && !actions.isEmpty()){
                        actions.remove(states.size() - 1);
                    } else if(title.equalsIgnoreCase("States:") && isPlus){
                        states.add(createInput());
                    } else if(title.equalsIgnoreCase("States:") && !isPlus && 
                            !states.isEmpty()){
                        states.remove(states.size() - 1);
                    } // END OF if
                    
                    mainPanel.removeAll();
                    mainPanel.revalidate();
                    createMainPanel();
                } // END OF actionPerformed 
            }); // END OF addActionListener
            return button;
        } // END OF createAddButton
        
        /**
         * The button responsible for saving entered information into the Caste
         * @return the JButton
         */
        private JButton createSubmitButton(){
            JButton button = new JButton("SUBMIT");
            button.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    saveData();
                    toggleAvailaibilty();
                    popup.dispose();
                }
            });
            return button;
        } // END OF createSubmitButton
        
        /* -------------------- DATA HANDLING FUNCTIONS --------------------  */       
        /**
         * Used to convert String array into JTextFields
         * @param array the ArrayList of Caste storage values being converted 
         * into actual inputs
         * @return the ArrayList of JTextFields
         */
        private ArrayList<JTextField> importData(ArrayList<String> array){
            ArrayList<JTextField> fields = new ArrayList<>();
            for(String string : array){
                JTextField field = createInput();
                field.setText(string);
                fields.add(field);
            }
            return fields;
        } // END OF importArray
        
        /**
         * Used to save data to the caste object
         */
        private void saveData(){
            caste.setName(name.getText());
            
            caste.getStates().clear();
            caste.getActions().clear();
            for(JTextField state : states) caste.addState(state.getText());
            for(JTextField action : actions) caste.addAction(action.getText());
            
            if(nameOnlyCheckbox.isSelected()) caste.setNameOnly(true);
            else caste.setNameOnly(false);
        } // END OF saveData
        
        /**
         * Initialises name, states and actions. Uses data from the input 
         * caste where not null / are applicable
         */
        private void initJTextFieldStorage(){
            this.name = createInput();
            this.name.setText(caste.getName());
            if(!caste.getStates().isEmpty()){
                this.states = importData(caste.getStates());
            } else {
                this.states.add(createInput());
            }
            if(!caste.getActions().isEmpty()){
                this.actions = importData(caste.getActions());
            } else {
                this.actions.add(createInput());
            }
        } // END OF initJTextFieldStorage
        
    } // END OF CasteDetails
    
    /* ------------------> POPUP TO RETRIEVE EDGE TITLE <------------------ */
    private class EdgeDetails{
        private JDialog popup;
        private JPanel mainPanel;
        private JTextField titleInput;
        private Edge edge;
        
        public EdgeDetails(Edge edge){
            this.popup = new JDialog();
            this.mainPanel = new JPanel();
            this.titleInput = new JTextField();
            this.edge = edge;
            initComponents();
        } // END OF Constructor
        
        private void initComponents(){
            this.mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
            this.mainPanel.add(new Label("Set Name: "));
            this.mainPanel.add(titleInput);
            this.mainPanel.add(createSubmitButton());
            this.popup.setSize(new Dimension(400, 100));
            this.popup.add(mainPanel);
            this.popup.setVisible(true);
        } // END OF initComponents
        
        private JButton createSubmitButton(){
            JButton button = new JButton("SUBMIT");
            button.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    edge.setTitle(titleInput.getText());
                    toggleAvailaibilty();
                    popup.dispose();
                } 
            });
            return button;
        } // END OF createSubmitButton
    } // END OF EdgePopup
        
}
                                                                                                                                                                                            