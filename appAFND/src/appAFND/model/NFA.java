package appAFND.model;

import appAFND.controller.AlphabetController;
import appAFND.controller.StateController;
import appAFND.controller.TransitionController;
import appAFND.view.AFNDController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javafx.scene.control.Alert;

/**
 *
 * @author Victor
 */
public class NFA extends Automaton {
    
  
    public NFA(ArrayList<StateController> states, AlphabetController alphabet, ArrayList<StateController> finalStates, StateController initialState, AFNDController afndController) {
        super(states, alphabet, finalStates, initialState, afndController);
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

    @Override
    public boolean readWord(String word) {
        boolean valid = true;
        char[] characters = word.toCharArray();
        //Mensaje
        Alert accepted = new Alert(Alert.AlertType.INFORMATION);
        accepted.setTitle("Word accepted");
        accepted.setHeaderText("The word was accepted!");
        
        Alert refuse = new Alert(Alert.AlertType.ERROR);
        refuse.setTitle("Word not accepted");
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

    @Override
    public ArrayList<TransitionController> readChar(String word, ArrayList<StateController> states) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
