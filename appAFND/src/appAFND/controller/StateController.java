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
    
    public void setStateLabel(String label)
    {
        model.setLabel(label);
    }
    
    public String getStateLabel()
    {
        return model.getLabel();
    }

    public boolean isStateActive()
    {
        return model.isActive();
    }

    public void setStateActive(boolean active)
    {
        model.setActive(active);
    }
    
    private State getState()
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
