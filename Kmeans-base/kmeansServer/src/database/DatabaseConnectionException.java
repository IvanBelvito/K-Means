package database;
/**
 * Eccezione personalizzata per gestire gli errori relativi alla connessione al database.
 */
public class DatabaseConnectionException extends Exception{

    /**
     * Crea un'istanza di DatabaseConnectionException con il messaggio specificato
     * @param message Il messaggio di errore che descrive l'eccezione
     */
    DatabaseConnectionException(String message){
        super(message);
    }
}
