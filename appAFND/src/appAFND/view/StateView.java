/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appAFND.view;

import appAFND.model.State;

/**
 *
 * @author felipe
 */
public class StateView extends State
{
    public void printStateDetails(boolean initial,String label,int positionX,int positionY, boolean active)
    {
        System.out.println("state: ");
        System.out.println("initial: "+ initial);
        System.out.println("label: "+ label);
        System.out.println("positionX: "+ positionX);
        System.out.println("positionY: "+ positionY);
        System.out.println("active: "+ active);
    }
}
