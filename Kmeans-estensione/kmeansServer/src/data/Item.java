package data;
import java.io.Serializable;
import java.util.Set;

/**
 * Classe astratta che modella una coppia generica attributo-valore
 */
public abstract class Item implements Serializable {
    /**
     * Attributo dell'item
     */
    private Attribute attribute;

    /**
     * Valore dell'item
     */
    private Object value;

    /**
     * Costruttore della classe Item
     * @param attribute attributo dell'item
     * @param value valore dell'item
     */
    Item(Attribute attribute, Object value){
        this.attribute=attribute;
        this.value=value;
    }

    /**
     * Restituisce l'attributo dell'item
     * @return l'attributo dell'item
     */
    Attribute getAttribute() {
        return attribute;
    }

    /**
     * Restituisce il valore dell'item
     * @return il valore dell'item
     */
    Object getValue() {
        return value;
    }

    /**
     * Restituisce una rappresentazione in stringa dell'item
     * @return una rappresentazione in stringa dell'item, che coincide con il valore dell'item
     */
    public String toString() {
        return this.value.toString();
    }

    /**
     * Restituisce la distanza tra due item, essendo astratto verrà implementato dalle sottoclassi definite
     * @param a l'oggetto rispetto al quale calcolare la distanza
     * @return la distanza tra i due item
     */
    abstract double distance(Object a);

    /**
     * Aggiorna il valore dell'item, calcolandolo come il prototipo dell'attributo rispetto al clusteredData
     * @param data il dataset
     * @param clusteredData l'insieme degli indici delle transazioni appartenenti al cluster in considerazione
     */
    public void update(Data data, Set<Integer> clusteredData) {
        this.value=data.computePrototype(clusteredData, this.attribute);
    }

}
