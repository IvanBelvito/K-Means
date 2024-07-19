package mining;

import data.Tuple;
import data.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe che modella un Cluster
 */
public class Cluster implements Serializable {
    /**
     * Centroide del cluster
     */
    private Tuple centroid; //array di Item (coppia attributo (discreteAttribute) - valore assunto)

    /**
     * Insieme degli indici (dal dataset) di transazioni che appartengono al cluster
     */
    private final Set<Integer> clusteredData; //segmento di quell'array

    /**
     * Costruttore della classe Cluster
     * @param centroid il centroide del cluster da costruire
     */
    Cluster(Tuple centroid){
        this.centroid=centroid;
        clusteredData=new HashSet<Integer>();

    }


    /**
     * Restituisce la Tupla che corrisponde al centroide del cluster
     * @return il centroide del cluster
     */
    Tuple getCentroid(){
        return centroid;
    }

    /**
     * Calcola il centroide del cluster
     * @param data il dataset
     */
    void computeCentroid(Data data){
        for(int i=0;i<centroid.getLength();i++){
            centroid.get(i).update(data,clusteredData);
        }
    }

    /**
     * Aggiunge una transazione al cluster
     * @param id l'id (dal dataset) della transazione da aggiungere
     * @return true se la transazione e cambiata di cluster, false altrimenti
     */
    boolean addData(int id){
        return clusteredData.add(id);
    }

    /**
     * Verifica se una transazione e clusterizzata nell'array corrente
     * @param id l'id della transazione da verificare
     * @return true se la transazione e clusterizzata nell'array corrente, false altrimenti
     */
    boolean contain(int id){
        return clusteredData.contains(id);
    }

    /**
     * Rimuove una transazione dal cluster (ha cambiato cluster)
     * @param id l'id della transazione da rimuovere
     */
    void removeTuple(int id){
        clusteredData.remove(id);
    }

    /**
     * Crea una rappresentazione in String del cluster, evidenziandone il centroide
     * @return la rappresentazione in String del cluster
     */
    public String toString(){
        String str="Centroid=(";
        for(int i=0;i<centroid.getLength();i++)
            str+=centroid.get(i);
        str+=")";
        return str;
    }

    /**
     * Crea una rappresentazione in String del cluster, includendo, oltre al centroide, anche le transazioni che vi appartengono
     * @param data il dataset
     * @return la rappresentazione in String del cluster
     */

    public String toString(Data data) {
        String str = "Centroid=(";
        for (int i = 0; i < centroid.getLength(); i++)
            str += centroid.get(i) + " ";
        str += ")\nExamples:\n";
        for (int i:clusteredData) {
            str += "[";
            for (int j = 0; j < data.getNumberOfAttributeSet(); j++)
                str += data.getAttributeValue((Integer) i, j) + " ";
            str += "] dist=" + getCentroid().getDistance(data.getItemSet(i)) + "\n";
        }
        str += "AvgDistance=" + getCentroid().avgDistance(data, clusteredData) + "\n";
        return str;

    }


}

