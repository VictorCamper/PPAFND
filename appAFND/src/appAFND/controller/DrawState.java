/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appAFND.controller;

import java.util.ArrayList;

/**
 *
 * @author kirit
 */
public class DrawState extends Command {

    private int x;
    private int y;
    private String label;
    private ArrayList<Command> commands;
    private ArrayList<Command> toRedo;

    public DrawState(int x, int y, String label, ArrayList<Command> commands, ArrayList<Command> toRedo) {
        super(commands, toRedo);
        this.x = x;
        this.y = y;
        this.label = label;
    }
    
    @Override
    public void execute(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
