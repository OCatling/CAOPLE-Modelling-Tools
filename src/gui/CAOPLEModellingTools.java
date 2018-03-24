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
    
    
    private JPanel main;
    
    public CAOPLEModellingTools(){
        this.main = new JPanel(new BorderLayout());
        this.model = new Pieces();
        this.model.addObserver(this);
        this.controller = new Controller(model, this);
        
        this.setEnabled(true);
        this.setTitle("CAOPLE Modelling Tools");
        this.add(createPieceButtons(Const.CONTROLS), BorderLayout.WEST);        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        main.add(new ModellingBoard(), BorderLayout.CENTER);
        //main.setOpaque(true);
        main.setBackground(Color.WHITE);

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
        for (String buttonTitle : Const.CONTROLS) {
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
                controller.setCurrentAction(name);
            } // END OF actionPerformed
        }); // END OF addActionListener
        return btn;
    } // END OF createPieceButton
    
    public Caste createCastePopup(Caste caste){
        return new CasteDetails(caste).caste;
    }
    
    public Edge createEdgePopup(Edge edge){
        return new EdgeDetails(edge).edge;
    }

    @Override
    public void update(Observable o, Object arg) {
        this.repaint();
    }
    
    /* --- COMPONENT USED FOR CREATING GRAPHICAL REPRESENTATION OF MODEL --- */
    private class ModellingBoard extends JComponent{
        private final Font font;
        
        public ModellingBoard(){
            super();
            this.font = new Font("TimesRoman", Font.PLAIN, Const.FONT_SIZE);
            initMouseListener();
            initMotionListener();
            initKeyBindings();
            
            this.setVisible(true);
            
        } // END OF ModellingBoard Constructor
        
        private void initKeyBindings(){
            InputMap im = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ActionMap am = this.getActionMap();
            
            // Delete Button
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0, false), "Delete");
            am.put("Delete", new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.deleteSelected();
                }     
            });
            
            // Left Button
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "Left");
            am.put("Left", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.moveAllSelected(-2, 0);
                }
            });
            
            // Right Button
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "Right");
            am.put("Right", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.moveAllSelected(2, 0);
                }
            });
            
            // Up Button
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "Up");
            am.put("Up", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.moveAllSelected(0, -2);
                }
            });
            
            // Down Button
            im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "Down");
            am.put("Down", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.moveAllSelected(0, 2);
                }
            });
        }
        
        private void initMouseListener(){
            this.addMouseListener(new MouseAdapter(){
                
                @Override
                public void mousePressed(MouseEvent event){
                    controller.handleMousePress(event);
                } // END OF mousePressed
                
                @Override
                public void mouseReleased(MouseEvent event){
                    controller.handleMouseRelease(event);
                }
                
            }); // END of addMouseListener
        }
        
        private void initMotionListener(){
            this.addMouseMotionListener(new MouseMotionAdapter(){

                @Override
                public void mouseDragged(MouseEvent event){
                    controller.handleMouseDrag(event);
                } // END OF mouseDragged
                
            }); // END OF addMouseMotionListener
        }
        
        /* ------------------> METHODS FOR DRAWING SHAPES <------------------ */
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(2160, 1800);
        }
        
        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(getForeground());
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
            controller.handleDragOutline(g2d);
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
            g2d.setColor(Color.WHITE);
            g2d.fill(caste);
            g2d.setColor(Color.BLACK);
            g2d.draw(caste);
            drawCasteDetailsInnerBoxes(g2d, caste);
            drawCasteDetails(g2d, caste);
            
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
        private void drawCasteDetailsInnerBoxes(Graphics2D g2d, Caste c){
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