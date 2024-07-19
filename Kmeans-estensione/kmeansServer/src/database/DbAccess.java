package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilizzata per gestire l'accesso e la connessione al database.
 */
public class DbAccess {

    /**
     * Nome della classe del driver JDBC.
     */
    private final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

    /**
     * Tipo di DBMS (MySQL).
     */
    private final String DBMS = "jdbc:mysql";

    /**
     * Contiene l’identificativo del server su cui risiede la base di dati (per esempio localhost).
     */
    private final String SERVER = "localhost";

    /**
     * Nome del database.
     */
    private final String DATABASE = "MapDB";

    /**
     * Numero di porta per la connessione al database.
     */
    private final String PORT = "3306";

    /**
     *  Nome utente per la connessione al database.
     */
    private final String USER_ID = "MapUser";

    /**
     * Password utente per la connessione al database.
     */
    private final String PASSWORD = "map";

    /**
     * Oggetto Connection per interagire con il database.
     */
    private Connection conn;

    /**
     * Inizializza una connessione al database.
     * @throws DatabaseConnectionException Se si verifica un errore durante la connessione al database.
     */
    public void initConnection() throws DatabaseConnectionException {
        try {
            Class.forName(DRIVER_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            conn = DriverManager.getConnection(DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE, USER_ID, PASSWORD);
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getMessage());
        }
    }

    /**
     * Restituisce la connessione al database.
     * @return L'oggetto Connection per interagire con il database.
     */
     Connection getConnection() {
        return conn;
    }

    /**
     * Chiude la connessione al database.
     * @throws DatabaseConnectionException Se si verifica un errore durante la chiusura della connessione al database.
     */
    public void closeConnection() throws DatabaseConnectionException {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new DatabaseConnectionException(e.getMessage());
        }
    }


}
