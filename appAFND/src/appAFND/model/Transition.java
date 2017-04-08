/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appAFND;

/**
 *
 * @author felipe
 */
public class Transition
{
    public State from;
    public State to;
    public String label;

    public State getFrom()
    {
        return from;
    }

    public void setFrom(State from)
    {
        this.from = from;
    }

    public State getTo()
    {
        return to;
    }

    public void setTo(State to)
    {
        this.to = to;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }
    
    
}
