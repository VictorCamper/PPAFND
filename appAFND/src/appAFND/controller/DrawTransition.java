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
public class DrawTransition extends Command {

    private int x;
    private int y;
    private String label;
    private Node from;
    private Node to;
    private ArrayList<Command> commands;
    private ArrayList<Command> toRedo;

    public DrawTransition(int x, int y, String label, Node from, Node to, ArrayList<Command> commands, ArrayList<Command> toRedo) {
        super(commands, toRedo);
        this.x = x;
        this.y = y;
        this.label = label;
        this.from = from;
        this.to = to;
    }
    
    @Override
    public void execute(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
