/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appAFND.model;

import appAFND.controller.AlphabetController;
import appAFND.controller.StateController;
import appAFND.controller.TransitionController;
import appAFND.view.AFNDController;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.Alert;


/**
 *
 * @author kirit
 */
public abstract class Automaton {
    protected ArrayList<StateController> states;
    protected AlphabetController alphabet;
    protected HashMap<StateController, HashMap<String, ArrayList<StateController>>> f;
    protected StateController initialState;
    protected ArrayList<StateController> finalStates;
    protected AFNDController afndController;
    
    
    public Automaton(ArrayList<StateController> states, AlphabetController alphabet, ArrayList<StateController> finalStates, StateController initialState, AFNDController afndController) {
        this.states = states;
        this.alphabet = alphabet;
        this.finalStates = finalStates;
        this.initialState = initialState;
        this.afndController = afndController;
    }
    
    
    
    public abstract boolean addTransition(StateController from, StateController to, String transition);
    
    abstract boolean removeTransition(StateController from, StateController to, String transition);
    
    public ArrayList<StateController> getStates() {
        return states;
    }

    public void setStates(ArrayList<StateController> states) {
        this.states = states;
    }
    
    public boolean addState(StateController state){
        this.f.put(state, new HashMap<String, ArrayList<StateController>>());
        for(Character c :this.alphabet.getCharacters()){
            String s = c.toString();
            this.f.get(state).put(s,new ArrayList<>());
        }
        return this.states.add(state);
    }
    
    public boolean removeState(StateController state){
        return this.states.remove(state);
    }

    public AlphabetController getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(AlphabetController alphabet) {
        this.alphabet = alphabet;
    }
    
    public boolean addCharacterToAlphabet(char c){
        return this.alphabet.addCharacter(c);
    }
    
    public boolean removeCharacterFromAlphabet(char c){
        return this.alphabet.removeCharacter(c);
    }

    public HashMap<StateController, HashMap<String, ArrayList<StateController>>> getF() {
        return f;
    }

    public void setF(HashMap<StateController, HashMap<String, ArrayList<StateController>>> f) {
        this.f = f;
    }

    public StateController getInitialState() {
        return initialState;
    }

    public void setInitialState(StateController initialState) {
        this.initialState = initialState;
    }

    public ArrayList<StateController> getFinalStates() {
        return finalStates;
    }

    public void setFinalStates(ArrayList<StateController> finalStates) {
        this.finalStates = finalStates;
    }
    
    public abstract boolean readWord(String word);

    public abstract ArrayList<TransitionController> readChar(String word, ArrayList<StateController> states);
    
}
