package database;

/**
 * Eccezione che indica l'assenza di un valore.
 * Questa eccezione può essere lanciata quando ci si aspetta un valore, ma non è presente.
 */
public class NoValueException extends Exception{

    /**
     * Crea un'istanza di NoValueException con un messaggio specifico.
     * @param msg Il messaggio di errore che fornisce dettagli sull'eccezione.
     */
    public NoValueException(String msg) {
        super(msg);
    }
}
