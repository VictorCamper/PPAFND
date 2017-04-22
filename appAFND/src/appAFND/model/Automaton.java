/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appAFND.model;

import appAFND.controller.AlphabetController;
import appAFND.controller.StateController;
import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * @author kirit
 */
public abstract class Automaton {
    private ArrayList<StateController> states;
    private AlphabetController alphabet;
    private HashMap<StateController, HashMap<String, ArrayList<StateController>>> f;
    private StateController initialState;
    private ArrayList<StateController> finalStates;
    
    
    public Automaton(ArrayList<StateController> states, AlphabetController alphabet, ArrayList<StateController> finalStates, StateController initialState) {
        this.states = states;
        this.alphabet = alphabet;
        this.finalStates = finalStates;
        this.initialState = initialState;
    }
    
    
    
    abstract boolean addTransition(StateController from, StateController to, String transition);
    
    abstract boolean removeTransition(StateController from, StateController to, String transition);
    
    public boolean readWord(String word){
        boolean valid = true;
        char[] characters = word.toCharArray();
        
        for (char c : characters){
            if (!alphabet.alphabetContains(c)){
                valid = false; 
                //Error, the word have a character that isn't in the alphabet of the automaton.  
            }            
        }
        
        if (valid){
            /*
            ArrayList<StateController> statesVoid = f.get(initialState).get("");
            ArrayList<StateController> statesChar = f.get(initialState).get("");*/
            
            //If ends in a final state
            return true;
            //Else
            //return false;
        }
        
        return false;
        
    }

    public ArrayList<StateController> getStates() {
        return states;
    }

    public void setStates(ArrayList<StateController> states) {
        this.states = states;
    }
    
    public boolean addState(StateController state){
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
}
