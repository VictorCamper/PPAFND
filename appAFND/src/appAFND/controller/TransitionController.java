/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appAFND.controller;

import appAFND.State;
import appAFND.Transition;
import appAFND.view.TransitionView;

/**
 *
 * @author felipe
 */
public class TransitionController
{
    private Transition model;
    private TransitionView view;
    
    public TransitionController(Transition model, TransitionView view)
    {
        this.model = model;
        this.view = view;
    }
    
    public State getTransitionFrom()
    {
        return model.getFrom();
    }
    
    public void setTransitionFrom(State from)
    {
        model.setFrom(from);
    }
    
    public State getTransitionTo()
    {
        return model.getTo();
    }
    
    public void setTransitionTo(State to)
    {
        model.setTo(to);
    }
    
    public String getTransitionLabel()
    {
        return model.getLabel();
    }
    
    public void setTransitionLabel(String label)
    {
        model.setLabel(label);
    }
}
