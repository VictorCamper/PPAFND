package appAFND.controller;

import appAFND.model.State;
import appAFND.view.StateView;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Victor
 */
public class StateController implements Comparable<StateController>
{
    private State model;
    private StateView view;

    public StateController(State model, StateView view)
    {
        this.model = model;
        this.view = view;
    }
    
    public boolean fromStateContains(TransitionController o) {
        return this.model.fromStateContains(o);
    }

    public TransitionController fromStateGet(int index) {
        return this.model.fromStateGet(index);
    }

    public boolean fromStateAdd(TransitionController e) {
        return this.model.fromStateAdd(e);
    }

    public boolean fromStateRemove(TransitionController o) {
        return this.model.fromStateRemove(o);
    }
    
    public boolean toStateContains(TransitionController o) {
        return this.model.toStateContains(o);
    }

    public TransitionController toStateGet(int index) {
        return this.model.toStateGet(index);
    }

    public boolean toStateAdd(TransitionController e) {
        return this.model.toStateAdd(e);
    }

    public boolean toStateRemove(TransitionController o) {
        return this.model.toStateRemove(o);
    }
    
    public State getStateModel()
    {
        return this.model;
    }
    
    public void drawNode(Group g)
    {
        this.view.drawNode(g);
    }
    
    public StateView getStateView(){
        return this.view;
    }            

    @Override
    public int compareTo(StateController o)
    {
        return this.view.compareTo(o.getStateView());
    }
    
}
