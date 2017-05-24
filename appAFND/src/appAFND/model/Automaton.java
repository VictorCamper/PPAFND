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
import java.util.List;
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
                // Alerta por si la letra no esta contenida en el alfabeto.
                // Rellenar la alerta.
                Alert emptyAlert = new Alert(Alert.AlertType.ERROR);
                emptyAlert.setHeaderText("ERROR");
                emptyAlert.setContentText("Some of the letters are not contained in the alphabet");
                
                valid = false; 
                //Error, the word have a character that isn't in the alphabet of the automaton.  
            }            
        }
        
        if (valid){
            /*
            ArrayList<StateController> statesVoid = f.get(initialState).get("");
            ArrayList<StateController> statesChar = f.get(initialState).get("");*/
            
            // HACER FUNCIONES PARA ESTA WEA!
            List<StateController> actives = new ArrayList<>();
            List<StateController> auxiliar = new ArrayList<>();
            for(char c : characters)
            {
                //Lista que llevara los nodos recorridos.
                
                //Se agrega el nodo inicial
                actives.add(initialState);
                boolean continuar = false;
                // ESTO PARA EL LAMBDA 
                do
                {
                    for (StateController active : actives)
                    {
                        HashMap<String, ArrayList<StateController>> thing = f.get(active);
                        auxiliar.addAll(thing.get("\u03bb"));
                        if(!auxiliar.isEmpty())
                        {
                            continuar = true;
                        }
                        else
                        {
                            continuar = false;
                        }
                    }
                    actives.addAll(auxiliar);
                    //Limpio el arraylist
                    auxiliar.clear();
                }while(continuar);
                
                // PARA EL CARACTER
                do
                {
                    for (StateController active : actives)
                    {                        
                        HashMap<String, ArrayList<StateController>> thing = f.get(active);
                        auxiliar.addAll(thing.get(c));
                    }
                    //Se limpia el arraylist actives
                    actives.clear();
                    actives.addAll(auxiliar);
                }while(continuar);
            }
            //AL TERMINAR LA LISTA DE CARACTERES SE COMPRUEBA SI LLEGO A UN NODO FINAL
            for(StateController active : actives)
            {
                if(active.equals(finalStates))
                {
                    Alert accepted = new Alert(Alert.AlertType.INFORMATION);
                    accepted.setTitle("Word Accepted");
                    accepted.setContentText("the word was accepted!");
                    break;
                }
            }
            return true;
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
}
