package data;

/**
 * Eccezione che viene lanciata quando il numero di cluster inserito è maggiore del numero di tuple del dataset
 */
public class OutOfRangeSampleSize extends Exception {
    /**
     * Costruttore della classe OutOfRangeSampleSize
     * @param msg stringa che contiene il messaggio di errore
     */
    OutOfRangeSampleSize(String msg) {
        super(msg);
    }
}
