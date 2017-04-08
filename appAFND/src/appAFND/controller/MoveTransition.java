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
public class MoveTransition extends Command {
    
    private Node from;
    private Node to;

    public MoveTransition(Node from, Node to, ArrayList<Command> commands, ArrayList<Command> toRedo) {
        super(commands, toRedo);
        this.from = from;
        this.to = to;
    }

    @Override
    void execute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
