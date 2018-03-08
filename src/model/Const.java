/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Oliver
 */
public class Const {
    // Constants For Controls
    public static final String SELECT = "Select";
    public static final String CASTE = "Caste";
    public static final String INTERACTION = "Interaction";
    public static final String COMPOSITION = "Composition";
    public static final String AGGREGATION = "Aggregation";
    public static final String INHERITENCE = "Inheritence";
    public static final String MIGRATION = "Migration";
    
    public static final String[] CONTROLS = {SELECT, CASTE, INTERACTION, 
        COMPOSITION, AGGREGATION, INHERITENCE, MIGRATION};
    
    // Constants For Dragpoint Position
    public static final int TOP_LEFT = 0; 
    public static final int TOP_RIGHT = 1;
    public static final int BOTTOM_RIGHT = 2; 
    public static final int BOTTOM_LEFT = 3;
    public static final int EDGE_START = 0;
    public static final int EDGE_END = 1;
    
    public static final int[] CASTE_DRAGPOINT_POSITIONS 
            = {TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT};
    
    public static final int[] EDGE_DRAGPOINT_POSITIONS
            = {EDGE_START, EDGE_END};
   
    // Font Specific
    public static final int FONT_SIZE = 16;

}
