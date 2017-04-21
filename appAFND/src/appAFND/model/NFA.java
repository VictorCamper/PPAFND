package appAFND.model;

import appAFND.controller.AlphabetController;
import appAFND.controller.StateController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Victor
 */
public class NFA extends Automaton {
    
    private ArrayList<StateController> states;
    private AlphabetController alphabet;
    private HashMap<StateController, HashMap<String, ArrayList<StateController>>> f;
    private StateController initialState;
    private ArrayList<StateController> finalStates;

    public NFA(ArrayList<StateController> states, AlphabetController alphabet, ArrayList<StateController> finalStates, StateController initialState) {
        super(states, alphabet, finalStates, initialState);
        
        this.f = new HashMap<>();
        for(StateController s : this.states){
            for (char c : this.alphabet.getCharacters()){
                HashMap<String,ArrayList<StateController>> transition = new HashMap<>();
                transition.put(c+"", null);
                HashMap<String,ArrayList<StateController>> tVoid = new HashMap<>();
                tVoid.put("", null);
                f.put(s,transition);
                f.put(s, tVoid);                
            }
        }
    }

    @Override
    public boolean addTransition(StateController from, StateController to, String transition) {
        char[] chars = transition.toCharArray();
        if (chars.length < 2){
            HashMap<String,ArrayList<StateController>> trans = f.get(from);
            if (chars.length == 0){
                trans.get("").add(to);
            }
            else{
                trans.get(chars+"").add(to);
            }
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean removeTransition(StateController from, StateController to, String transition) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
