/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import model.Caste;
import model.Const;

/**
 *
 * @author Oliver
 */
public class CasteDetails {
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
                    //toggleAvailaibilty();
                    popup.dispose();
                }
            });
            
            popup.add(scrollPane);
            popup.setVisible(true);
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
                    //toggleAvailaibilty();
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
