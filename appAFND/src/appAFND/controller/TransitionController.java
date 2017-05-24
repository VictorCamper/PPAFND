/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appAFND.controller;

import appAFND.model.State;
import appAFND.model.Transition;
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
    
    public StateController getTransitionFrom()
    {
        return model.getFrom();
    }
    
    public void setTransitionFrom(StateController from)
    {
        model.setFrom(from);
    }
    
    public StateController getTransitionTo()
    {
        return model.getTo();
    }
    
    public void setTransitionTo(StateController to)
    {
        model.setTo(to);
    }
    
}
