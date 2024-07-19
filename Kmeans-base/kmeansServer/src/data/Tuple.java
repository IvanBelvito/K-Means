package data;

import java.io.Serializable;
import java.util.Set;

/**
 * Classe che modella una Tupla, una sequenza di coppie Attributo-Valore
 */
public class Tuple implements Serializable {

    /**
     * Array di Item che rappresenta la tupla
     */
    private Item[] tuple;

    /**
     * Costruttore della classe Tuple
     * @param size la dimensione della tupla
     */
    Tuple(int size) {
        tuple = new Item[size];
    }

    /**
     * Restituisce la dimensione della tupla
     * @return la dimensione della tupla
     */
    public int getLength() {
        return tuple.length;
    }

    /**
     * Restituisce l'i-esimo item della tupla
     * @param i l'indice dell'item da restituire
     * @return l'i-esimo item della tupla
     */
    public Item get(int i) {
        return tuple[i];
    }

    /**
     * Aggiunge un item alla tupla
     * @param c l'item da aggiungere
     * @param i l'indice dell'item da aggiungere
     */
    void add(Item c,int i) {
        tuple[i]=c;
    }

    /**
     * Determina la distanza tra la tupla riferita da obj e la
     * tupla corrente (riferita da this). La distanza è ottenuta come la somma delle
     * distanze tra gli item in posizioni uguali nelle due tuple.
     * @param obj la tupla rispetto alla quale calcolare la distanza
     * @return la distanza tra le due tuple
     */
    public double getDistance(Tuple obj) {
        double distance=0;

        for(int i=0; i<tuple.length; i++) {
            distance += tuple[i].distance(obj.get(i).getValue()); //distanza tra tuple della classe e valore preso da obj passato come input
        }

        return distance;
    }

    /**
     * Restituisce la media delle distanze tra la tupla corrente ed un insieme di tuple
     * @param data il dataset contenente tutte le tuple
     * @param clusteredData l'insieme di tuple rispetto alle quali calcolare la distanza
     * @return la media delle distanze
     */
    public double avgDistance(Data data,  Set<Integer> clusteredData) {
        double p, sumD = 0.0;
        for (int i : clusteredData) {
            double d = getDistance(data.getItemSet(i));
            sumD += d;
        }
        p = sumD / clusteredData.size();
        return p;
    }

}