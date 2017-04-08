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
abstract class Command {
    
    protected ArrayList<Command> commands;
    protected ArrayList<Command> toRedo;

    public Command(ArrayList<Command> commands, ArrayList<Command> toRedo) {
        this.commands = commands;
        this.toRedo = toRedo;
    }
    
    abstract void execute();
    
    public void undo(){
        commands.remove(this);
        toRedo.add(this);
    }

    
    public void redo() {
        commands.add(this);
        commands.remove(this);
    }   
}
