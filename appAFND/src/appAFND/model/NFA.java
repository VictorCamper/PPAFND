package appAFND.model;

import appAFND.controller.AlphabetController;
import appAFND.controller.StateController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Victor
 */
public class NFA extends Automaton {
    
  
    public NFA(ArrayList<StateController> states, AlphabetController alphabet, ArrayList<StateController> finalStates, StateController initialState) {
        super(states, alphabet, finalStates, initialState);
        //Caracter vacío esta en todos los alphabet
        alphabet.addCharacter('\u03BB');
        
        f = new HashMap<>();
        for(StateController s : this.states){
            HashMap<String,ArrayList<StateController>> tVoid;
            tVoid = new HashMap<>();
            
            tVoid.put("\u03BB", new ArrayList<>());
            f.put(s, tVoid);  
            
            for (char c : this.alphabet.getCharacters()){
                HashMap<String,ArrayList<StateController>> transition;
                transition = new HashMap<>();
                
                transition.put(Character.toString(c), new ArrayList<>());                
                f.put(s,transition);
            }
        }
    }

    @Override
    public boolean addTransition(StateController from, StateController to, String transition) {
        
            ArrayList<StateController> trans = f.get(from).get(transition);
            
            if (trans.isEmpty()){                
                return trans.add(to);
            }
            else{
                if (!trans.contains(to)){
                    return trans.add(to);
                }                    
            }
            
            return false;
              
    }

    @Override
    public boolean removeTransition(StateController from, StateController to, String transition) {
       char[] chars = transition.toCharArray();
        if (chars.length < 2){
            HashMap<String,ArrayList<StateController>> trans = f.get(from);
            if (chars.length == 0){
                trans.get("\u03BB").remove(to);
            }
            else{
                trans.get(Arrays.toString(chars)).remove(to);
            }
            return true;
        }
        return false; 
    }

    public HashMap<StateController, HashMap<String, ArrayList<StateController>>> getF()
    {
        return f;
    }
}
