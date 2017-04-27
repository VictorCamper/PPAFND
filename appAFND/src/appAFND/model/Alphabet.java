package appAFND.model;

import java.util.ArrayList;

/**
 *
 * @author felipe
 */
public class Alphabet
{
    private ArrayList<Character> alphabet;

    public Alphabet() 
    {
        this.alphabet = new ArrayList<>();
    }
    
    public ArrayList<Character> getCharacters(){
        return this.alphabet;
    }

    public int alphabetSize() 
    {
        return alphabet.size();
    }

    public boolean alphabetContains(Character c) 
    {
        return alphabet.contains(c);
    }

    public Character getCharacter(int index) 
    {
        return alphabet.get(index);
    }

    public boolean addCharacter(Character element) 
    {
        if(element == '\u03BB')
        {
            this.alphabet.add(0,element);
            return true;
        }
        else
        {
            return this.alphabet.add(element);
        }
    }

    public boolean removeCharacter(Character element) 
    {
        return alphabet.remove(element);
    }
    
}
