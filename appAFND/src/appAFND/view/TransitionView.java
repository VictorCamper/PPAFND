/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appAFND.view;

import appAFND.State;

/**
 *
 * @author felipe
 */
public class TransitionView
{
    public void printTransitionDetails(State from, State to, String label)
    {
        System.out.println("Transition: ");
        System.out.println("From: "+ from);
        System.out.println("To: "+ to);
        System.out.println("Label: "+ label); 
    }
}
