package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  classe che rappresenta lo schema di una tabella di un database.
 */
public class TableSchema {

    /**
     * oggetto DbAccess utilizzato per la connessione al database.
     */
    private DbAccess db;

    /**
     * lista delle colonne nello schema della tabella.
     */
    private List<Column> tableSchema = new ArrayList<>();

    /**
     * Rappresenta una colonna nello schema della tabella.
     */
    public class Column {
        /**
         * Il nome della colonna
         */
        private final String name;

        /**
         * Il tipo della colonna
         */
        private final String type;

        /**
         * Crea una nuova istanza di Column con il nome e il tipo specificato
         * @param name Il nome della colonna
         * @param type Il tipo della colonn a
         */
        private Column(String name, String type) {
            this.name = name;
            this.type = type;
        }

        /**
         * ritorna il nome della colonna
         * @return Il nome della colonna
         */
        public String getColumnName() {
            return name;
        }

        /**
         * Verifica se il tipo della colonna è numerico
         * @return true se il tipo della colonna è numerico altrimenti false
         */
        public boolean isNumber() {
            return type.equals("number");
        }

        public String toString() {
            return name + ":" + type;
        }
    }

    /**
     * Crea una nuova istanza di TableSchema per la tabella specificata
     * @param db        L'oggetto DbAccess utilizzato per la connessione al database
     * @param tableName Il nome della tabella per cui ottenere lo schema
     * @throws SQLException Se si verifica un errore durante l'accesso ai metadati della tabella
     */
    public TableSchema(DbAccess db, String tableName) throws SQLException {
        this.db = db;
        HashMap<String, String> mapSQL_JAVATypes = new HashMap<>();
        //http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
        mapSQL_JAVATypes.put("CHAR", "string");
        mapSQL_JAVATypes.put("VARCHAR", "string");
        mapSQL_JAVATypes.put("LONGVARCHAR", "string");
        mapSQL_JAVATypes.put("BIT", "string");
        mapSQL_JAVATypes.put("SHORT", "number");
        mapSQL_JAVATypes.put("INT", "number");
        mapSQL_JAVATypes.put("LONG", "number");
        mapSQL_JAVATypes.put("FLOAT", "number");
        mapSQL_JAVATypes.put("DOUBLE", "number");


        Connection conn = db.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM " + tableName + " WHERE 1=0;");
        ResultSetMetaData md = rs.getMetaData();

        for (int i=1; i<=md.getColumnCount(); i++) {
            if (mapSQL_JAVATypes.containsKey(md.getColumnTypeName(i)))
                tableSchema.add(new Column(md.getColumnName(i), mapSQL_JAVATypes.get(md.getColumnTypeName(i))));
        }
        st.close();
        rs.close();
    }

    /**
     * Ottiene il numero di attributi nella tabella
     * @return Il numero di attributi.
     */
    public int getNumberOfAttributes() {
        return tableSchema.size();
    }

    /**
     * Ottiene la colonna all'indice specificato nello schema della tabella
     * @param index L'indice della colonna da ottenere.
     * @return La colonna nello schema della tabella.
     */
    public Column getColumn(int index) {
        return tableSchema.get(index);
    }


}