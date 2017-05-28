package appAFND.model;

import appAFND.controller.StateController;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Dijkstra algorithm adapted for this project.
 * 
 * @author Victor
 */
public class Dijkstra
{
    private int n;
    private StateController s;
    private int [] sp;
    private int [] from;
    private char [] path;
    private boolean [] r;
    private Automaton automaton;
    
    public Dijkstra(Automaton automaton)
    {
        this.automaton = automaton;
        this.n = this.automaton.getStates().size();
        this.s = this.automaton.getInitialState();
        this.sp = new int[this.n];
        this.from = new int[this.n];
        this.path = new char[this.n];
        this.r = new boolean[this.n];        
    }
    
    public void sp()
    {
        for(int i = 0; i < this.n; i++)
        {
            sp[i] = Integer.MAX_VALUE;
            from[i] = Integer.MAX_VALUE;
            r[i] = false;
        }
        
        int initialIndex = this.getIndex(s);
        
        
        this.sp[initialIndex] = 0;
        this.from[initialIndex] = initialIndex;
        
        while(!this.completed())
        {
            int u = this.argMin();
            ArrayList<Element> adjacencyList = new ArrayList<>();

            if (u < this.automaton.states.size())
            {
                this.r[u] = true;
                HashMap<String,ArrayList<StateController>> adl = this.automaton.f.get(this.automaton.getStates().get(u));

                for(Character c : this.automaton.getAlphabet().getCharacters())
                {
                    if(c=='\u03BB')
                    {
                        for (StateController state : adl.get(c.toString()))
                        {
                            Element element = new Element(state,true, '\u03BB');
                            adjacencyList.add(element);
                        }
                    }
                    else 
                    {
                        for (StateController state : adl.get(c.toString()))
                        {
                            Element element = new Element(state, false, c);
                            adjacencyList.add(element);
                        }
                    }
                }
            }
                                
            //int i = 0;
            for(Element element : adjacencyList)
            //while(adjacencyList.size() >= 0)
            {
                int weight = this.sp[u];
                if(!element.empty)
                    weight++;
                if(weight < sp[this.getIndex(element.stateTo)])
                {
                    //System.out.println("actual weight: " + sp[this.getIndex(element.stateTo)]);
                    sp[this.getIndex(element.stateTo)] = weight;
                    //System.out.println("new weight: " + sp[this.getIndex(element.stateTo)]);
                    from[this.getIndex(element.stateTo)] = u;
                    path[this.getIndex(element.stateTo)] = element.getChar();
                }
                //adjacencyList.remove(i);
//                i++;
            }
        }       
    }
    
    public String getShortestWord()
    {
        ArrayList<StateController> finals = this.automaton.getFinalStates();
        HashMap<StateController, String> shortest = new HashMap<>();
        
        ArrayList<String>paths = new ArrayList<>(/*shortest.values()*/);
        
        for(StateController state : finals)
        {
            String path = "";
            
            int indexFinal = this.getIndex(state);
            while (indexFinal != 0)
            {
                path += this.path[indexFinal]; // indexFinal is not a good name
                indexFinal = this.from[indexFinal];
            }
            
            path = new StringBuilder(path).reverse().toString();
            //shortest.put(state, path);            
            paths.add(path);
        }
        
        
        
        String theShortestPath = "";
        int length = Integer.MAX_VALUE;
        
        for(int i = 0; i < paths.size(); i++)
        {
            String val = paths.remove(i);
            paths.add(i, this.removeSpaces(val));
        }
        
        for (String path : paths)
        {
            if(path.length() < length)
            {
                theShortestPath = path;
                length = path.length();
            }
        }
        //System.out.println("sp :" + theShortestPath);
        return theShortestPath;
    }
    
    public String removeSpaces(String str) // Actually, removes the lambda character
    {
        String ourString="";
        for (int i=0; i<str.length() ; i++){
            if(str.charAt(i) != '\u03BB')
            {
                ourString += str.charAt(i);
            }    
        }
        return ourString;
    }
    
    private int getIndex(StateController state) throws IndexOutOfBoundsException
    {
        for (int i = 0; i < this.n; i++)
            if(this.automaton.getStates().get(i).equals(state))
                return i;
        return -1;
        //throw new ArrayIndexOutOfBoundsException("State isn't in States (?)");
    }
    
    private boolean completed()
    {
        for (int i = 0; i < this.n; i++)
            if(r[i] != true)
                return false;
        return true;
    }
    
    private int argMin()
    {
        int min = Integer.MAX_VALUE;
        int index = this.n; // IndexOutOfBoundsException
        
        for (int i = 0; i < this.n; i++)
        {
            if((this.sp[i] < min) && (this.r[i] == false))
            {
                min = this.sp[i];
                index = i;
            }
        }
        return index;
    }
    
    class Element
    {
        private StateController stateTo;
        private boolean empty;
        private char c;

        public Element(StateController stateTo, boolean empty, char c)
        {
            this.stateTo = stateTo;
            this.empty = empty;
            this.c = c;
        }

        public StateController getStateTo()
        {
            return stateTo;
        }

        public void setStateTo(StateController stateTo)
        {
            this.stateTo = stateTo;
        }

        public boolean isEmpty()
        {
            return empty;
        }

        public void setEmpty(boolean empty)
        {
            this.empty = empty;
        }

        public char getChar()
        {
            return c;
        }

        public void setChar(char c)
        {
            this.c = c;
        }
        
        
    }
}
