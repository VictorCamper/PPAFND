package appAFND.model;

import appAFND.controller.AlphabetController;
import appAFND.controller.StateController;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Victor
 */
public class DFA extends Automaton
{

    public DFA(ArrayList<StateController> states, AlphabetController alphabet, ArrayList<StateController> finalStates, StateController initialState)
    {
        super(states, alphabet, finalStates, initialState);
        
        f = new HashMap<>();
        for(StateController s : this.states){
            HashMap<String,ArrayList<StateController>> tVoid;
            tVoid = new HashMap<>();
            f.put(s,tVoid);
            
            for(char c : this.alphabet.getCharacters()){
                HashMap<String,ArrayList<StateController>> transition;
                transition = new HashMap<>();
                
                transition.put(Character.toString(c), new ArrayList<>());
                f.put(s,transition);
            }
        }
    }

    @Override
    public boolean addTransition(StateController from, StateController to, String transition)
    {
        ArrayList<StateController> trans = f.get(from).get(transition);
        
        if(trans.isEmpty()){
            return trans.add(to);
        }
        else{
            trans.remove(0); // each state has just one transition for each character
            return trans.add(to);
        }
    }

    @Override
    public boolean removeTransition(StateController from, StateController to, String transition)
    {
        char [] chars = transition.toCharArray();
        HashMap<String,ArrayList<StateController>> trans = f.get(from);
        return trans.get(new Character(chars[0]).toString()).remove(to);
    }
    
    @Override
    public boolean readWord(String word) 
    {
        System.out.println("THIS SHIT IS NOT IMPLEMENTED YET!");
        return false;
    }
}
