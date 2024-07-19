package data;
import java.io.Serializable;
import java.util.*;

/**
 *  Classe che rappresenta un attributo discreto (categorico).
 *  Questa classe estende la classe astratta Attribute e include metodi per la gestione dei valori possibili dell'attributo.
 */
class DiscreteAttribute extends Attribute implements Iterable<String>, Comparable<DiscreteAttribute> {

    /**
     * Valori possibili dell'attributo.
     */
    private TreeSet<String> values;

    /**
     * Invoca il costruttore della classe madre e inizializza il membro values con il parametro in input.
     * @param name,   nome dell'attributo
     * @param index,  identificativo numerico dell'attributo
     * @param values, array di stringhe rappresentanti il dominio dell'attributo
     */
    DiscreteAttribute(String name, int index, String values[]) {
        super(name, index);
        this.values = new TreeSet<String>();
        this.values.addAll(Arrays.asList(values));
    }

    /**
     * Restituisce il numero di volte che un determinato valore compare in un insieme di esempi rispetto all'attributo corrente.
     * @param data   il dataset sul quale contare le occorrenze
     * @param idList l'insieme di esempi sul quale contare le occorrenze
     * @param v      il valore di cui contare le occorrenze
     * @return il numero di volte che il valore v compare nell'insieme di esempi idList rispetto all'attributo corrente
     */
    int frequency(Data data, Set<Integer> idList, String v) {
        int freq=0;
        for (int i : idList) {
            if (data.getAttributeValue(i, this.getIndex()).equals(v)) freq++;
        }
        return freq;
    }

    /**
     * Restituisce l'oggetto Iterator che permette di iterare sui valori possibili dell'attributo
     * @return l'oggetto Iterator
     * @see Iterator
     */
    public Iterator<String> iterator(){
       return values.iterator();
    }

    public int compareTo(DiscreteAttribute o){
        return this.getName().compareTo(o.getName());
    }

}
	