/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appAFND.controller;

import java.util.ArrayList;
import javafx.scene.Node;

/**
 *
 * @author kirit
 */
public class SetStateLabel extends Command {

    private Node state;
    private String label;
    private ArrayList<Command> commands;
    private ArrayList<Command> toRedo;

    public SetStateLabel(Node state, String label, ArrayList<Command> commands, ArrayList<Command> toRedo) {
        super(commands, toRedo);
        this.state = state;
        this.label = label;
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
