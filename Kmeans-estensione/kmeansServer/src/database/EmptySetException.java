package database;

/**
 * Eccezione lanciata quando un insieme è vuoto e si tenta di accedervi o eseguirvi operazioni.
 */
public class EmptySetException extends Exception {

    /**
     * Crea un'istanza di EmptySetException con un messaggio specifico.
     *
     * @param msg Il messaggio di errore associato all'eccezione.
     */
    public EmptySetException(String msg) {
        super(msg);
    }
}
