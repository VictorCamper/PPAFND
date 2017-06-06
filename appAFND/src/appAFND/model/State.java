/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appAFND.model;

import appAFND.controller.TransitionController;
import java.util.ArrayList;

/**
 *
 * @author felipe
 */
public class State
{
    private ArrayList<TransitionController> toState, fromState;

    public State()
    {
        this.toState = new ArrayList<>();
        this.fromState = new ArrayList<>();
    }

    public boolean fromStateContains(TransitionController o) {
        return fromState.contains(o);
    }

    public TransitionController fromStateGet(int index) {
        return fromState.get(index);
    }
    
    public boolean fromStateEmpty() {
        return fromState.isEmpty();
    }
    
    public boolean toStateEmpty() {
        return toState.isEmpty();
    }

    public boolean fromStateAdd(TransitionController e) {
        return fromState.add(e);
    }

    public boolean fromStateRemove(TransitionController o) {
        return fromState.remove(o);
    }
    
    public boolean toStateContains(TransitionController o) {
        return toState.contains(o);
    }

    public TransitionController toStateGet(int index) {
        return toState.get(index);
    }

    public boolean toStateAdd(TransitionController e) {
        return toState.add(e);
    }

    public boolean toStateRemove(TransitionController o) {
        return toState.remove(o);
    }
    
    public ArrayList<TransitionController> getFromState()
    {
        return this.fromState;
    }
   
    public ArrayList<TransitionController> getToState()
    {
        return this.toState;
    }
}
