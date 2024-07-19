package mining;
import data.Data;
import data.OutOfRangeSampleSize;
import data.Tuple;

import java.io.Serializable;

/**
 *  Classe che modella un insieme di Cluster.
 *  Implementa l'interfaccia Serializable per permettere il salvataggio su file
 */
public class ClusterSet implements Serializable {

    /**
     * Array di Cluster
     */
    private Cluster C[];

    /**
     * Indice dell'ultima posizione di C libera
     */
    private int i=0;

    /**
     * Costruttore della classe ClusterSet
     * @param k il numero di cluster contenuti nel Set
     */
    ClusterSet(int k){
        C = new Cluster[k];
    }

    /**
     * Aggiunge un cluster all'insieme
     * @param c il Cluster da aggiungere
     */
   private void add(Cluster c) {
        C[i] = c;
        i++;
   }

    /**
     * Sceglie i centroidi dal dataset e inizializza i cluster nel ClusterSet
     * @param data il dataset
     * @throws OutOfRangeSampleSize
     *
     */
    void initializeCentroids(Data data) throws OutOfRangeSampleSize {
        int centroidIndexes[]=data.sampling(C.length);
        for(int i=0;i<centroidIndexes.length;i++)
        {
            Tuple centroidI=data.getItemSet(centroidIndexes[i]);
            add(new Cluster(centroidI));
        }
    }

    /**
     * Restituisce il cluster più vicino ad una tupla
     * @param tuple la tupla
     * @return il cluster più vicino
     */
    Cluster nearestCluster(Tuple tuple) {
        Cluster clusterPiuVicino=C[0];
        double minDistanza=tuple.getDistance(C[0].getCentroid()), currDistanza=0;
        for(int i=0; i<C.length; i++) {
            currDistanza=tuple.getDistance(C[i].getCentroid());
            if(currDistanza<minDistanza) {
                clusterPiuVicino=C[i];
                minDistanza=currDistanza;
            }
        }
        return clusterPiuVicino;
    }

    /**
     * Restituisce il cluster che contiene una certa transazione, dato l'indice di questa
     * @param id l'indice della transazione
     * @return il cluster che contiene la transazione, o null se non è contenuta in nessun cluster
     */
    Cluster currentCluster(int id) {
        for(int i=0; i<C.length; i++) {
            if(C[i].contain(id)){
                return C[i];
            }
        }
        return null;
    }

    /**
     * Aggiorna i centroidi dei cluster
     * @param data il dataset
     */
    void updateCentroids(Data data) {
        for(int i=0;i<C.length;i++) {
            C[i].computeCentroid(data);
        }
    }

    /**
     * Restituisce una stringa fatta da ciascun centroide dell’insieme dei cluster.
     * @return la stringa richiesta
     */
    public String toString() {
        String str = "";
        for (int i = 0; i < C.length; i++) {
            str += i + ": " + C[i] + "\n";
        }
        return str;
    }

    /**
     * Restituisce una stringa che descriva lo stato di ciascun cluster in C.
     * @param data il dataset
     * @return la stringa richiesta
     */
    public String toString(Data data ){
        String str="";
        for(int i=0;i<C.length;i++){
            if (C[i]!=null){
                str+=i+":"+C[i].toString(data)+"\n";
            }
        }
        return str;
    }
}
