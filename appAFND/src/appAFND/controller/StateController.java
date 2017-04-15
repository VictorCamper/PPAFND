/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appAFND.controller;

import appAFND.State;
import appAFND.view.StateView;

/**
 *
 * @author felipe
 */
public class StateController
{
    private State model;
    private StateView view;
    
    public StateController(State model,StateView view)
    {
        this.model = model;
        this.view = view;
    }
    
    public void setStateInitial(Boolean initial)
    {
        model.setInitial(initial);
    }
    
    public boolean getStateInitial()
    {
        return model.getInitial();
    }
    
    public void setStateLabel(String label)
    {
        model.setLabel(label);
    }
    
    public String getStateLabel()
    {
        return model.getLabel();
    }
    
    public void setStatePositionX(int positionX)
    {
        model.setPositionX(positionX);
    }
    
    public int getStatePositionX()
    {
        return model.getPositionX();
    }
    
    public void setStatePositionY(int positionY)
    {
        model.setPositionY(positionY);
    }
    
    public int getStatePositionY()
    {
        return model.getPositionY();
    }

    public boolean isStateActive()
    {
        return model.isActive();
    }

    public void setStateActive(boolean active)
    {
        model.setActive(active);
    }
}
