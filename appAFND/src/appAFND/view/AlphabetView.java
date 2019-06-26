/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appAFND.view;

/**
 *
 * @author Victor
 */
public class AlphabetView {
    
    public void alphabetView(char[] alphabet)
    {
        System.out.print("Alphabet : ");
        for(char alph : alphabet)
        {
            System.out.print(alph);
        }
    }
}
