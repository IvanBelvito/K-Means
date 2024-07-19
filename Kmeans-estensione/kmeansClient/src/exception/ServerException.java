package exception;
/**
 * Classe che rappresenta un'eccezione personalizzata per gestire errori nel server.
 * Viene utilizzata per segnalare problemi o errori che si verificano durante l'elaborazione delle richieste del client nel server.
 */
public class ServerException extends Exception {

    /**
     * Crea una nuova istanza di ServerException con il messaggio specificato.
     * @param message messaggio di errore associato a questa eccezione.
     */
    public ServerException(String message) {
        super(message);
    }
}





