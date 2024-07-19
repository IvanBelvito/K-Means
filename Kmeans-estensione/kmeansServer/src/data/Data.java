package data;

import database.*;
import java.util.*;
import java.sql.SQLException;

/**
 *  Classe che rappresenta un insieme di esempi.
 *  Questa classe contiene un Lista di oggetti Example che rappresenta il dataset, una Lista di Attribute e il numero di esempi.
 */
public class Data {

    /**
     * Lista di oggetti Example che rappresenta il dataset
     */
    private List<Example> data;

    /**
     * Numero di esempi
     */
    private final int numberOfExamples;

    /**
     * Lista degli attributi
     */
    private final List<Attribute> attributeSet = new LinkedList<Attribute>();;

   /**
    * Costruisce un oggetto Data a partire da una tabella specificata nel database.
    * @param tableName Il nome della tabella dal quale caricare i dati.
    */
    public Data(String tableName) {

        DbAccess dbAccess = new DbAccess();
        try {
            dbAccess.initConnection();
        } catch (DatabaseConnectionException e) {
            System.out.println("Errore durante la connessione con il database: " + e.getMessage());
        }

        TableData tableData = new TableData(dbAccess);
        try {
            this.data = tableData.getDistinctTransazioni(tableName);
        } catch (SQLException | EmptySetException e) {
            System.out.println("Errore: " + e.getMessage());
        }

        this.numberOfExamples = data.size();

        try{
            TableSchema tableSchema = new TableSchema(dbAccess, tableName);

            for(int i = 0; i < tableSchema.getNumberOfAttributes(); i++){
                if (tableSchema.getColumn(i).isNumber()) {
                    try{
                        double minValue = (double) tableData.getAggregateColumnValue(tableName, tableSchema.getColumn(i), QUERY_TYPE.MIN);
                        double maxValue = (double) tableData.getAggregateColumnValue(tableName, tableSchema.getColumn(i), QUERY_TYPE.MAX);
                        attributeSet.add(new ContinuousAttribute(tableSchema.getColumn(i).getColumnName(), i, minValue, maxValue));
                    }catch(SQLException | NoValueException e){
                        System.out.println("Errore: " + e.getMessage());
                    }
                }else{
                    String[] values = new String[tableData.getDistinctColumnValues(tableName, tableSchema.getColumn(i)).size()];
                    tableData.getDistinctColumnValues(tableName,tableSchema.getColumn(i)).toArray(values);
                    attributeSet.add(new DiscreteAttribute(tableSchema.getColumn(i).getColumnName(), i, values));
                }
            }
        }catch(SQLException e) {
            System.out.println("Errore durante la creazione dello schema della tabella: " + e.getMessage());
        }
        try {
            dbAccess.closeConnection();
        }catch(DatabaseConnectionException e) {
            System.out.println("La chiusura della connessione non è andata a buon fine. " + e.getMessage());
        }
    }

    /**
     * Restituisce il numero di esempi
     * @return Il numero di esempi
     */
    public int getNumberOfExamples() {
        return numberOfExamples;
    }

    /**
     * Restituisce la dimensione di AttributeSet
     * @return, cardinalità dell'insieme degli attributi
     */
    public int getNumberOfAttributeSet() {
        return attributeSet.size();
    }

    /**
     * Restituisce attributeSet
     * @return List di Attribute che rappresenta lo schema del dataset
     */
    private List<Attribute> getAttributeSchema() {
        return attributeSet;
    }

    /**
     * Restituisce il valore di un attributo di un esempio
     * @param exampleIndex,   indice dell'esempio
     * @param attributeIndex, indice della colonna
     * @return, valore assunto in data
     */
    public Object getAttributeValue(int exampleIndex, int attributeIndex) {
        return data.get(exampleIndex).get(attributeIndex);
    }

    /**
     * Restituisce la rappresentazione in stringa del dataset
     * @return Il dataset sotto forma di stringa
     */
    public String toString() {

        String s = "";
        for (Attribute v : getAttributeSchema()) {
            s += v.getName() + ",";
        }

        for (int i = 0; i < getNumberOfExamples(); i++) {
            s = s + "\n" + (i + 1) + ": ";
            for (int j = 0; j < getNumberOfAttributeSet(); j++) {
                s = s + getAttributeValue(i,j) + ", ";
            }
        }

        return s;
    }

    /**
     * Restituisce una Tupla che contiene i valori degli attributi di un esempio
     * @param index l'indice dell'esempio
     * @return la Tupla che contiene i valori degli attributi di un esempio
     */
    public Tuple getItemSet(int index) {
        Tuple tuple = new Tuple(attributeSet.size());
        for (int i = 0; i < attributeSet.size(); i++) {
            if (attributeSet.get(i) instanceof ContinuousAttribute) {
                tuple.add(new ContinuousItem(attributeSet.get(i), (Double)
                        data.get(index).get(attributeSet.get(i).getIndex())), i);
            } else if (attributeSet.get(i) instanceof DiscreteAttribute) {
                tuple.add(new DiscreteItem((DiscreteAttribute) attributeSet.get(i), (String)
                        data.get(index).get(attributeSet.get(i).getIndex())), i);
            }
        }
        return tuple;
    }

    /**
     * Esegue il passo 1 dell'algoritmo KMeans. Sceglie k centroidi in modo casuale i quali devono essere diversi
     * @param k il numero di centroidi da generare
     * @return un array di k int che rappresentano gli indici di riga delle transazioni scelte come centroidi
     * @throws OutOfRangeSampleSize se k non è compreso tra 1 e il numero di transazioni
     */
    public int[] sampling(int k) throws OutOfRangeSampleSize{
        if (k <= 0 || k > data.size())
            throw new OutOfRangeSampleSize("Non ci sono centroidi a sufficienza poichè il numero di tuple distinte e': " + data.size() + ".");
        int centroidIndexes[]=new int[k];
        //choose k random different centroids in data.
        Random rand=new Random();
        rand.setSeed(System.currentTimeMillis());
        for (int i=0;i<k;i++){
            boolean found=false;
            int c;
            do
            {
                found=false;
                c=rand.nextInt(getNumberOfExamples());
                // verify that centroid[c] is not equal to a centroid already stored in CentroidIndexes
                for(int j=0;j<i;j++)
                    if(compare(centroidIndexes[j],c)){
                        found=true;
                        break;
                    }
            }
            while(found);
            centroidIndexes[i]=c;
        }
        return centroidIndexes;
    }


    /**
     * Verifica se due transazioni sono uguali
     * @param i indice della prima transazione
     * @param j indice della seconda transazione
     * @return true se le due transazioni sono uguali, false altrimenti
     */
    private boolean compare(int i, int j) {
        for (int k = 0; k < attributeSet.size(); k++)
            if (!(data.get(i).get(k).equals(data.get(j).get(k))))
                return false;
        return true;
    }

    /**
     * Calcola il centroide rispetto ad un attributo, dato un Set di indici di riga
     * @param idList    il Set di indici da considerare
     * @param attribute l'attributo sul cui calcolare il centroide
     * @return il valore del centroide rispetto ad attribute
     */
    Object computePrototype(Set<Integer> idList, Attribute attribute) {
        if (attribute instanceof ContinuousAttribute)
            return computePrototype(idList, (ContinuousAttribute) attribute);
        else
            return computePrototype(idList, (DiscreteAttribute) attribute);
    }

    /**
     * Determina il valore che occorre più frequentemente per un DiscreteAttribute nel sottoinsieme di dati individuato da idList
     * @param idList    il Set di indici da considerare
     * @param attribute il DiscreteAttribute sul cui calcolare il centroide
     * @return il valore del centroide rispetto ad attribute
     */
    private String computePrototype(Set<Integer> idList, DiscreteAttribute attribute) {
        Iterator<String> it = attribute.iterator();
        String mostFrequent = it.next();
        String tmp_string = " ";
        int tmp;
        int maxFrequency = attribute.frequency(this, idList, mostFrequent);
        while (it.hasNext()) {
            tmp_string = it.next();
            tmp = attribute.frequency(this, idList, tmp_string);

            if (tmp > maxFrequency) {
                maxFrequency = tmp;
                mostFrequent = tmp_string;
            }
        }
        return mostFrequent;
    }

    /**
     * Determina il valore che occorre più frequentemente per un ContinuousAttribute nel sottoinsieme di dati individuato da idList
     * @param idList    il Set di indici da considerare
     * @param attribute il ContinuousAttribute sul cui calcolare il centroide
     * @return il valore del centroide rispetto ad attribute
     */
   private Double computePrototype(Set<Integer> idList, ContinuousAttribute attribute){
        double somma=0;
        for(Integer i : idList){
            somma+=(double)data.get(i).get(attribute.getIndex());
        }
        return somma/idList.size();
    }

}