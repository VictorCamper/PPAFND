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
public class MoveState extends Command{

    private int x;
    private int y;

    public MoveState(int x, int y, ArrayList<Command> commands, ArrayList<Command> toRedo) {
        super(commands, toRedo);
        this.x = x;
        this.y = y;
    }
    
    @Override
    void execute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
