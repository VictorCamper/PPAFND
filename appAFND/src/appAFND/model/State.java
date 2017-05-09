/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appAFND.model;

/**
 *
 * @author felipe
 */
public class State
{
    public boolean initial;
    public String label;
    public int positionX;
    public int positionY;
    public boolean active;

    public State(boolean initial, String label, boolean active)
    {
        this.initial = initial;
        this.label = label;
        this.active = active;
    }
    
    public boolean isInitial()
    {
        return initial;
    }

    public void setInitial()
    {
        this.initial = true;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public int getPositionX()
    {
        return positionX;
    }

    public void setPositionX(int positionX)
    {
        this.positionX = positionX;
    }

    public int getPositionY()
    {
        return positionY;
    }

    public void setPositionY(int positionY)
    {
        this.positionY = positionY;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }
    
    
    
    
}
