package data;
/**
 * Classe che rappresenta una coppia attributo-valore discreto
 */
class DiscreteItem extends Item {

    /**
     * Costruttore della classe DiscreteItem
     * @param attributeSet l'attributo dell'item
     * @param value il valore dell'item
     */
    DiscreteItem(DiscreteAttribute attributeSet, String value){
        super(attributeSet, value);

    }

    /**
     * Restituisce il valore della distanza tra due item, calcolata come 1 se il valore dell'item è diverso dal
     * valore dell'oggetto passato come parametro, 0 altrimenti
     * @param a l'oggetto rispetto al quale calcolare la distanza
     * @return 1 se i valori sono differenti, 0 altrimenti
     */
    double distance(Object a) {
        return getValue().equals(a) ? 0 : 1;
    }

}
