package data;

import java.io.Serializable;

/**
 * Classe astratta che rappresenta un attributo
 */
abstract class Attribute implements Serializable {

    /**
    * nome dell'attributo
    */
    private String name;

    /**
     * indice dell'attributo
     */
    private int index;

    /**
     * inizializza i valori dei membri name, index
     * @param name // nome dell'attributo
     * @param index // identificativo numerico dell'attributo
     */
    Attribute(String name, int index) {
        this.name=name;
        this.index=index;
    }

    /**
     * restituisce name di un Attribute
     * @return name, nome dell'attributo
     */
    String getName() {
        return name;
    }

    /**
     * Restituisce index di un attribute
     * @return index, identificativo numerico dell'attributo
     */
    int getIndex(){
        return index;
    }

    /**
     * Restituisce una stringa con il nome dell'attributo
     * @return name, sovrascrive metodo ereditato dalla superclasse e restuisce la stringa rappresentante il nome dell'oggetto (attributo)
     */
    public String toString() {
        return name;
    }

}
