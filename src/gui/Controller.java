/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.ActionEvent;


/**
 *
 * @author Oliver
 */
public class Controller {
    private String currentAction;
    
    public Controller(){
        this.currentAction = "Caste";
    }
    
    public void handleComponentButton(ActionEvent event){
        this.currentAction = event.getActionCommand();
    }
    
}
