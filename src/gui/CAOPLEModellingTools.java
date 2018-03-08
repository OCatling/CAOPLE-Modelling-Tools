package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import model.*;
import model.edge.*;


/**
 *
 * @author Oliver
 */
public class CAOPLEModellingTools extends JFrame implements Observer{
    private Pieces model;
    private Controller controller;
    private String currentAction;
    
    
    private JPanel main;
    
    public CAOPLEModellingTools(){
        this.main = new JPanel(new BorderLayout());
        this.model = new Pieces();
        this.model.addObserver(this);
        this.controller = new Controller(model, this);
        
        this.currentAction = Const.SELECT;
        this.setEnabled(true);
        this.setTitle("CAOPLE Modelling Tools");
        this.add(createPieceButtons(Const.CONTROLS), BorderLayout.WEST);        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        main.add(new ModellingBoard(), BorderLayout.CENTER);
        main.setOpaque(true);

        JScrollPane scrollpane = new JScrollPane(main);
        scrollpane.setPreferredSize(new Dimension(1080, 800));
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
    private Box createPieceButtons(String[] buttonTitles){
        Box btnBox = Box.createVerticalBox();
        for (String buttonTitle : buttonTitles) {
            btnBox.add(createPieceButton(buttonTitle));
        }
        return btnBox;
    } // END OF createPieceButtons
    
    /**
     * Method to create a button used for choosing the action
     * @param name the name of the component being created
     * @return the button
     */
    private JButton createPieceButton(String name){
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
    } // END OF createPieceButton

    @Override
    public void update(Observable o, Object arg) {
        this.repaint();
    }
    
    /* --- COMPONENT USED FOR CREATING GRAPHICAL REPRESENTATION OF MODEL --- */
    private class ModellingBoard extends JComponent{
        private Point start, finish;
        private final Font font;
        private static final int MOVE = 1;
        private static final int SCALE = 2;
        private int selectAction;
        private int selectedDragpoint;
        
        public ModellingBoard(){
            super();
            this.font = new Font("TimesRoman", Font.PLAIN, Const.FONT_SIZE);
            this.setPreferredSize(new Dimension(2080, 900));
            this.setBackground(Color.WHITE);
            
            initMouseListener();
            initMotionListener();
            
            this.setVisible(true);
            
        } // END OF ModellingBoard Constructor
        
        
        private void initMouseListener(){
            this.addMouseListener(new MouseAdapter(){
                
                @Override
                public void mousePressed(MouseEvent event){
                    start = event.getPoint();
                    finish = start; 
                    //Double Click Handler
                    if(currentAction.equalsIgnoreCase("Select") && event.getClickCount() == 2){
                        if(model.getIntersectingPiece(start) != null){
                            Piece c = model.getIntersectingPiece(start);
                            if(c instanceof Caste) new CasteDetails((Caste) c); 
                            else if(c instanceof Interaction) new EdgeDetails((Edge) c);
                            toggleAvailaibilty();
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
                } // END OF mousePressed
                
                @Override
                public void mouseReleased(MouseEvent event){
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
                        CasteDetails details = new CasteDetails(c);
                        model.addPiece(details.caste);   
                        toggleAvailaibilty();
                    }
                    else if(currentAction.equalsIgnoreCase(Const.INTERACTION) 
                            && model.getIntersectingPiece(start) != null
                            && model.getIntersectingPiece(finish) != null){
                        Interaction i = new Interaction(start.x, start.y, event.getX(), event.getY());
                        EdgeDetails e = new EdgeDetails(i);
                        model.addPiece(e.edge);
                        toggleAvailaibilty();
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
                    repaint();
                } // END OF mouseReleased
                
            }); // END of addMouseListener
        }
        
        private void initMotionListener(){
            this.addMouseMotionListener(new MouseMotionAdapter(){

                @Override
                public void mouseDragged(MouseEvent event){
                    finish = event.getPoint();
                    if(currentAction.equalsIgnoreCase("Select") && selectAction == MOVE){
                        movePiece();
                    } 
                    else if(currentAction.equalsIgnoreCase("Select") && selectAction == SCALE){
                        scalePiece();
                    }
                    repaint();
                    event.consume();
                } // END OF mouseDragged
                
            }); // END OF addMouseMotionListener
        }
        /* ----------------------> MOVE & SCALE PIECES <--------------------- */
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
                            p.getDragpoints()[Const.TOP_LEFT].translate(dx, dy);
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
        
        /* ------------------> METHODS FOR DRAWING SHAPES <------------------ */
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(2160, 1800);
        }
        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.BLUE);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(getForeground());
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
            Graphics2D g2d = (Graphics2D)g;
            drawPieces(g2d);
            
            // Handle Drag Outline
            handleDragOutline(g2d);
        } // END OF paint
        
        /**
         * Draws All Pieces
         * @param g the graphics package
         */
        private void drawPieces(Graphics2D g2d){
            drawCastes(g2d);
            drawEdges(g2d);
        } // END OF drawPieces
        
        /**
         * Draws All Castes
         * @param g2d The graphics package
         */
        private void drawCastes(Graphics2D g2d){
            for(Caste caste : model.getCastes()){
                if(caste.isSelected()){
                    handleHighlight(g2d, caste);
                } else if(!caste.isSelected() && !g2d.getColor().equals(Color.BLACK)) {
                    g2d.setColor(Color.BLACK);
                }
                drawCaste(g2d, caste);
            }
        } // END OF drawCastes
        
        /**
         * Draws A Caste
         * @param g2d the graphics package
         * @param caste the caste
         */
        private void drawCaste(Graphics2D g2d, Caste caste){
            drawCasteDetailsInnerBoxes(g2d, caste);
            drawCasteDetails(g2d, caste);
            g2d.draw(caste);
        } // END OF drawCastes
        
        /**
         * Draws The Castes Details (Name, States and Actions)
         * @param g2d the graphics package
         * @param caste the caste
         */
        private void drawCasteDetails(Graphics2D g2d, Caste caste){
            FontMetrics metrics = g2d.getFontMetrics(font);
            
            // Draw Name
            int x = caste.x + (caste.width - metrics.stringWidth(caste.getName())) / 2; // Center Name Text
            int y = caste.y + 35;
            g2d.drawString( caste.getName(), x, y);
            
            if(!caste.isNameOnly()){
                drawStates(g2d, caste);
                drawActions(g2d, caste);  
            }
        } // END OF drawCasteDetails
        
        /**
         * Sequence to draw the states strings
         * @param g2d the graphics package
         * @param node the Caste to get actions from 
         */
        private void drawStates(Graphics2D g2d, Caste node){
            int y = node.y + 80; // (76:Height from top) + (10:Indent) = 86
            int loopSize = getLoopSize(node, node.getStates());
            drawProperties(g2d, node.getStates(), node.x + 30, y, loopSize);
            if(loopSize != node.getStates().size()) 
                drawContinuation(g2d, node, node.y + 90 + (loopSize * Const.FONT_SIZE));
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
                drawContinuation(g2d, node, node.y + 90 + ((node.height - 90) / 2) + (loopSize * Const.FONT_SIZE));
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
            if(boxHeight - 10 < loopSize * Const.FONT_SIZE){
                loopSize = (boxHeight / Const.FONT_SIZE) - 2;
            }
            return loopSize;
        } // END OF getLoopSize
        
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
                y += Const.FONT_SIZE;
            }
        } // END OF drawProperties
        
        /**
         * Draws the indication the contents are too much for the size of the container
         * @param g2d the graphics package
         * @param node the node being drawn
         * @param startingY the starting position for the continuation
         */
        private void drawContinuation(Graphics2D g2d, Caste node, int startingY){
            FontMetrics metrics = g2d.getFontMetrics(font);
            int x = node.x + (node.width - metrics.stringWidth(node.getName())) / 2; // Center Name Text
            g2d.drawString("-----", x, startingY);
        } // END OF drawContinuation
        
        /**
         * draws the boxes which contain the details of the caste 
         * (details added separately)
         * @param g the graphics package
         * @param c the Caste from which the dimensions are calculated
         */
        private void drawCasteDetailsInnerBoxes(Graphics g, Caste c){
            Graphics2D g2d = (Graphics2D)g;
            int y = c.y + 10;
            int height = 40;
            g2d.drawRect( c.x + 10, y, c.width - 20, height);
            if(!c.isNameOnly()){
                y += height;
                height = (c.height - 60) / 2;
                g2d.drawRect( c.x + 10, y, c.width - 20, height);
                y += height;
                g2d.drawRect( c.x + 10, y, c.width - 20, height);
            }
        } // END OF drawCasteDetailsInnerBoxes
        
        /**
         * Draws All Edges
         * @param g2d the graphics package
         */
        private void drawEdges(Graphics2D g2d){
            for(Edge edge : model.getEdges()){
                if(edge.isSelected())
                    handleHighlight(g2d, edge); 
                else if(!edge.isSelected() && !g2d.getColor().equals(Color.BLACK))
                    g2d.setColor(Color.BLACK);   
                if(!edge.getLabel().isEmpty())
                    g2d.drawString(edge.getLabel(), edge.x1 + 20, edge.y1 - 20);
                drawEdge(g2d, edge);
            }
        } // END OF drawEdges
        
        /**
         * Draws A Edge
         * @param g2d the graphics package
         * @param edge the edge
         */
        private void drawEdge(Graphics2D g2d, Edge edge){
            g2d.setStroke(edge.getStroke());
            g2d.draw(edge); 
            g2d.drawPolygon(edge.createArrowHead());
            g2d.setPaint(edge.getArrowFill());
            g2d.fillPolygon(edge.createArrowHead());
        } // END OF drawEdge
        
        /**
         * Draws the outline as components are dragged
         * @param g the graphics package
         */
        private void handleDragOutline(Graphics2D g2d){
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
        
        /**
         * Draws the caste with highlighted effects
         * @param g the graphics package
         * @param c the caste to draw highlight of
         */
        private void handleHighlight(Graphics2D g2d, Piece c){
            
            for(Dragpoint point : c.getDragpoints()){
                g2d.setColor(Color.BLUE);
                g2d.draw(point);
                
            }
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
            this.nameOnlyCheckbox.setSelected(caste.isNameOnly());
            
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
            this.popup.setAlwaysOnTop(true);
            this.popup.setResizable(true);
            this.popup.setSize(500, 500);
            createTitle();
            createMainPanel();
            JScrollPane scrollPane = new JScrollPane(mainPanel);
            this.popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            this.popup.addWindowListener(new WindowAdapter(){
                @Override
                public void windowClosing(WindowEvent windowEvent){
                    saveData();
                    toggleAvailaibilty();
                    popup.dispose();
                }
            });
            
            this.popup.add(scrollPane);
            this.popup.setVisible(true);
        } // END OF initPieces
        
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
            
            if(nameOnlyCheckbox.isSelected()) {
                caste.setNameOnly(true);
                caste.height = 60;
                caste.getDragpoints()[Const.BOTTOM_LEFT]
                        .move(caste.x, caste.y + caste.height);
                caste.getDragpoints()[Const.BOTTOM_RIGHT]
                        .move(caste.x + caste.width, caste.y + caste.height);
            }
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
            this.titleInput = new JTextField(edge.getLabel());
            this.edge = edge;
            initPieces();
        } // END OF Constructor
        
        private void initPieces(){
            this.mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
            this.mainPanel.add(new Label("Set Name: "));
            this.mainPanel.add(titleInput);
            this.mainPanel.add(createSubmitButton());
            this.popup.setSize(new Dimension(400, 100));
            this.popup.add(mainPanel);
            this.popup.setTitle("Edit Label");
            this.popup.addWindowListener(new WindowAdapter(){
                @Override
                public void windowClosing(WindowEvent windowEvent){
                    edge.setLabel(titleInput.getText());
                    toggleAvailaibilty();
                    popup.dispose();
                }
            });
            this.popup.setVisible(true);
        } // END OF initPieces
        
        private JButton createSubmitButton(){
            JButton button = new JButton("SUBMIT");
            button.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    edge.setLabel(titleInput.getText());
                    toggleAvailaibilty();
                    popup.dispose();
                } 
            });
            return button;
        } // END OF createSubmitButton
    } // END OF EdgePopup
        
}