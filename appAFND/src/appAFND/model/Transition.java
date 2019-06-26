/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appAFND.model;

import appAFND.controller.StateController;

/**
 *
 * @author felipe
 */
public class Transition
{

    public Transition(StateController from, StateController to) {
        this.from = from;
        this.to = to;
    }
    
    public StateController from;
    public StateController to;

    public StateController getFrom()
    {
        return from;
    }

    public void setFrom(StateController from)
    {
        this.from = from;
    }

    public StateController getTo()
    {
        return to;
    }

    public void setTo(StateController to)
    {
        this.to = to;
    }    
    
}
