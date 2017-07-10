/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appAFND.model;

import appAFND.controller.AlphabetController;
import appAFND.controller.StateController;
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
    
    public boolean readWord(String word){
        boolean valid = true;
        char[] characters = word.toCharArray();
        //Mensaje
        Alert accepted = new Alert(Alert.AlertType.INFORMATION);
        accepted.setTitle("Word accepted");
        accepted.setHeaderText("The word was accepted!");
        
        Alert refuse = new Alert(Alert.AlertType.ERROR);
        refuse.setTitle("Not accepted");
        refuse.setHeaderText("The word was not accepted!");
        
        for (char c : characters){
            if (!alphabet.alphabetContains(c)){
                System.out.println(c);
                System.out.println(alphabet.getCharacters());
                // Alerta por si la letra no esta contenida en el alfabeto.
                // Rellenar la alerta.
                afndController.rejectAutomaton();
                
                Alert emptyAlert = new Alert(Alert.AlertType.ERROR);
                emptyAlert.setTitle("Word error");
                emptyAlert.setHeaderText("Some of the characters are not contained in the alphabet");
                emptyAlert.showAndWait();
                //valid = false; 
                return false;
                //Error, the word have a character that isn't in the alphabet of the automaton.  
            }            
        }
        
        ArrayList<StateController> activos = new ArrayList<>();
        ArrayList<StateController> nuevos = new ArrayList<>();

        activos.add(initialState);
        nuevos.add(initialState);


        for(int i = 0; i < characters.length ; i++){
            //Revisar si los nuevos tienen transiciones vacias
            //Se agregan las transiciones vacias a nuevoaux
            while(!(nuevos.isEmpty()))
            {
                //System.out.println("hola");
                ArrayList<StateController> nuevosaux = new ArrayList();
                for(StateController nuevo : nuevos){                    
                    if(!(f.get(nuevo).get("\u03BB").isEmpty())){
                        if(f.get(nuevo).get("\u03BB").contains(nuevo)){
                            for(StateController s :f.get(nuevo).get("\u03BB")){
                                if(s.compareTo(nuevo)!=0)
                                    nuevosaux.add(s);
                            }
                        }
                        else
                            nuevosaux.addAll(f.get(nuevo).get("\u03BB"));
                    }
                }
                //La lista de nuevos = nuevosaux
                nuevos.clear();
                nuevos.addAll(nuevosaux);

                //Agregar a los activos los nuevos
                activos.addAll(nuevos);

            }
            //System.out.println(activos);

            //Leer caracter
            //Agregar a los activos
            ArrayList<StateController> activosaux = new ArrayList();
            //boolean canContinue = true;
            for(StateController activo : activos){
                activosaux.addAll(f.get(activo).get(((Character)characters[i]).toString()));
                /*if(f.get(activo).get(((Character)characters[i]).toString()).isEmpty())
                    activosaux.remove(activo);*/
            }
            /*System.out.println(canContinue);
            if (!canContinue){
                refuse.showAndWait();
                return false;
            }*/
            
            activos.clear();
            activos.addAll(activosaux);
            nuevos.clear();
            nuevos.addAll(activos);

            if(activos.isEmpty()){
               afndController.rejectAutomaton();
                
               refuse.showAndWait();
               return false;
            }

            

        }
        
        while(!(nuevos.isEmpty()))
            {
                //System.out.println("hola");
                ArrayList<StateController> nuevosaux = new ArrayList();
                for(StateController nuevo : nuevos){
                    if(!(f.get(nuevo).get("\u03BB").isEmpty())){
                        nuevosaux.addAll(f.get(nuevo).get("\u03BB"));
                    }
                }
                //La lista de nuevos = nuevosaux
                nuevos.clear();
                nuevos.addAll(nuevosaux);

                //Agregar a los activos los nuevos
                activos.addAll(nuevos);

            }
        
        for(StateController activo : activos){
            //System.out.println("activo:"+activo.getStateLabel());
            if (finalStates.contains(activo)){
                afndController.acceptAutomaton();
                accepted.showAndWait();
                return true;
            }
        }

            
              
        afndController.rejectAutomaton();
        refuse.showAndWait();
        return false;

    }

    public void readChar(String character) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
