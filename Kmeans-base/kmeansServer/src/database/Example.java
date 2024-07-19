package database;

import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un esempio composto da una lista di oggetti.
 * Gli oggetti all'interno dell'esempio sono accessibili tramite indici e possono essere confrontati con altri esempi.
 */
public class Example implements Comparable<Example>{

    /**
     * list di object utilizzata per memorizzare gli esempi.
     */
    private List<Object> example=new ArrayList<Object>();

    /**
     * Aggiunge un oggetto all'esempio.
     * @param o L'oggetto da aggiungere all'esempio.
     */
    public void add(Object o){
        example.add(o);
    }

    /**
     * Restituisce l'oggetto nell'esempio all'indice specificato.
     * @param i L'indice dell'oggetto cercato
     * @return L'oggetto all'indice specificato
     */
    public Object get(int i){
        return example.get(i);
    }

    /**
     * Confronta questo esempio con un altro
     * @param ex L'altro esempio da confrontare.
     * @return Un valore negativo se questo esempio è inferiore, un valore positivo se è superiore, o zero se sono uguali.
     */
    public int compareTo(Example ex) {
        int i=0;
        for(Object o:ex.example){
            if(!o.equals(this.example.get(i)))
                return ((Comparable)o).compareTo(example.get(i));
            i++;
        }
        return 0;
    }

    /**
     * Restituisce una rappresentazione in stringa di un esempio.
     * @return Una stringa che rappresenta l'esempio.
     */
    public String toString(){
        String str="";
        for(Object o:example)
            str+=o.toString()+ " ";
        return str;
        }
}

