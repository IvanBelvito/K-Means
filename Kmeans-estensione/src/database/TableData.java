package database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import database.TableSchema.Column;

/**
 *  classe che fornisce metodi per ottenere dati da una tabella di un database.
 */
public class TableData {

    /**
     * Oggetto dbAccess utilizzato per la connessione al database.
     */
    private final DbAccess db;

    /**
     * Crea una nuova istanza di TableData.
     * @param db L'oggetto DbAccess utilizzato per la connessione al database.
     */
    public TableData(DbAccess db) {
        this.db=db;
    }

    /**
     * Ottiene tutte le transazioni distinte dalla tabella specificata.
     * @param table Il nome della tabella da cui ottenere le transazioni distinte.
     * @return Una lista di oggetti Example rappresentanti le transazioni distinte.
     * @throws SQLException     Se si verifica un errore durante l'esecuzione della query SQL.
     * @throws EmptySetException Se non ci sono transazioni distinte nella tabella.
     */
    public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException{
        TableSchema tableSchema = new TableSchema(db, table);
        Statement statement = db.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT DISTINCT * FROM " + table + ";");
        List<Example> distinctTransactions = new ArrayList<Example>();
        while (resultSet.next()) {
            Example ex = new Example();
            for (int i = 0; i < tableSchema.getNumberOfAttributes(); i++) {
                if (tableSchema.getColumn(i).isNumber()) {
                    ex.add(resultSet.getDouble(i + 1));
                } else {
                    ex.add(resultSet.getString(i + 1));
                }
            }
            distinctTransactions.add(ex);
        }
        statement.close();
        resultSet.close();
        if (distinctTransactions.isEmpty()) {
            throw new EmptySetException("Non ci sono transazioni distinte");
        }
        return distinctTransactions;
    }

    /**
     * Ottiene tutti i valori distinti dalla colonna specificata nella tabella specificata.
     * @param table  Il nome della tabella da cui ottenere i valori distinti.
     * @param column La colonna da cui ottenere i valori distinti.
     * @return Un set di oggetti rappresentanti i valori distinti nella colonna.
     * @throws SQLException Se si verifica un errore durante l'esecuzione della query SQL.
     */
    public Set<Object> getDistinctColumnValues(String table, Column column) throws SQLException {
        Statement statement = db.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT DISTINCT " + column.getColumnName() + " FROM " + table + ";");
        Set<Object> distinctValues = new TreeSet<>();
        while (resultSet.next()) {
            if (column.isNumber()) {
                distinctValues.add(resultSet.getDouble(column.getColumnName()));
            } else {
                distinctValues.add(resultSet.getString(column.getColumnName()));
            }
        }
        resultSet.close();
        statement.close();
        return distinctValues;
    }

    /**
     * Ottiene il valore aggregato (MIN o MAX) dalla colonna specificata nella tabella specificata.
     * @param table    Il nome della tabella da cui ottenere il valore aggregato.
     * @param column   La colonna da cui ottenere il valore aggregato.
     * @param aggregate Il tipo di aggregazione desiderato (MIN o MAX).
     * @return Il valore aggregato dalla colonna.
     * @throws SQLException    Se si verifica un errore durante l'esecuzione della query SQL.
     * @throws NoValueException Se non viene restituito alcun valore aggregato.
     */
    public  Object getAggregateColumnValue(String table,Column column,QUERY_TYPE aggregate) throws SQLException,NoValueException{
        Statement statement = db.getConnection().createStatement();
        Object aggrValue;
        String query = "SELECT " + aggregate + "(" + column.getColumnName() + ")" + " FROM " + table + ";";
        ResultSet resultSet = statement.executeQuery(query);
        if(!resultSet.next()){
            throw new NoValueException("Errore: result set è vuoto.");
        }
        if(column.isNumber()){
            aggrValue = resultSet.getDouble(aggregate + "(" + column.getColumnName() + ")");
        }else{
            aggrValue = resultSet.getString(aggregate + "(" + column.getColumnName() + ")");
        }
        if(aggrValue.equals(null)){
            throw new NoValueException("Errore: il valore cercato è null.");
        }
        resultSet.close();
        statement.close();
        return aggrValue;
    }

}
