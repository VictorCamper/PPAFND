package appAFND.controller;

import appAFND.model.Alphabet;
import appAFND.view.AlphabetView;
import java.util.ArrayList;

/**
 *
 * @author Victor
 */
public class AlphabetController 
{
    private Alphabet model;
    private AlphabetView view;

    public AlphabetController(Alphabet model, AlphabetView view) 
    {
        this.model = model;
        this.view = view;
    }
    
    public ArrayList<Character> getCharacters(){
        return model.getCharacters();
    }
    
    public int alphabetSize()
    {
        return this.model.alphabetSize();
    }
    
    public boolean alphabetContains(Character c)
    {
        return this.model.alphabetContains(c);
    }
    
    public Character getCharacter(int index)
    {
        return this.model.getCharacter(index);
    }
    
    public void addCharacter(int index, Character element)
    {
        this.model.addCharacter(index, element);
    }
    
    public boolean removeCharacter(int index)
    {
        return this.model.removeCharacter(index);
    }
    
    
    
}
