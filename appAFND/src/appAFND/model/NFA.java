package appAFND.model;

import appAFND.controller.AlphabetController;
import appAFND.controller.StateController;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Victor
 */
public class NFA {
    
    
    private List<StateController> states;
    private AlphabetController alphabet;
    private ArrayList<StateController> finalStates;
    private StateController initialState;
    private ArrayList<ArrayList> f;

    public NFA(List<StateController> states, AlphabetController alphabet, ArrayList<StateController> finalStates, StateController initialState) {
        this.states = states;
        this.alphabet = alphabet;
        this.finalStates = finalStates;
        this.initialState = initialState;
        
        this.f = new ArrayList<>();
        this.f.add(new ArrayList<String>()); // State's type [0]
        this.f.add(new ArrayList<StateController>()); // States List [1]
        
        /**
         * i is for alphabet[i]
         * j will be for states[j]
         * f[i][j] -> transition
         */
        for (int i = 0; i < this.alphabet.alphabetSize(); i++) {
            this.f.add(new ArrayList<String>()); // One ArrayList for each alphabet's character | this.f[i][j] = q1-q2-...
        }
    }
    
    
}
