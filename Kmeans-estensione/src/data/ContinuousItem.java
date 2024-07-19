package data;

/**
 * Classe che modella una coppia Attributo continuo - valore numerico.
 */
public class ContinuousItem extends Item{

    /**
     * richiama il costruttore della super classe
     * @param attribute attributo da inserire in una coppia
     * @param value valore da inserire in una coppia
     */
    ContinuousItem(Attribute attribute, double value) {
        super(attribute, value);
    }

    /**
     * Determina la distanza (in valore assoluto) tra il valore
     * scalato memorizzato nello item corrente (this.getValue()) e quello scalato associato al parametro a.
     * @param a l'oggetto rispetto al quale calcolare la distanza
     * @return distanza tra il valore memarizzato nell'item e quello del parametro.
     */
    @Override
    double distance(Object a) {
        ContinuousAttribute attribute1 = (ContinuousAttribute)this.getAttribute();
        return Math.abs(attribute1.getScaledValue((double)this.getValue())-attribute1.getScaledValue((double)a));
    }


}
