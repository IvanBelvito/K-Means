package mining;

import data.Data;
import data.OutOfRangeSampleSize;

import java.io.*;

/**
 * Classe che implementa l'algoritmo di clustering K-means
 */
public class KMeansMiner implements Serializable {

    /**
     * Oggetto che contiene l'insieme dei cluster
     */
    private ClusterSet C;

    /**
     * Costruttore della classe KMeansMiner
     * @param k il numero di cluster da creare
     */
   public KMeansMiner(int k){
        this.C = new ClusterSet(k);

   }

    /**
     * Costruttore della classe KMeansMiner. Carica l'insieme dei cluster da file.
     * @param fileName il path del file dal quale caricare il clusterset
     * @throws IOException se si verifica un errore durante la lettura dal file
     * @throws ClassNotFoundException se il ClassLoader non riesce a trovare la classe ClusterSet
     * @throws FileNotFoundException se il file specificato non viene trovato
     */
    public KMeansMiner(String fileName) throws IOException, ClassNotFoundException, FileNotFoundException{
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        C = (ClusterSet) ois.readObject();
        fis.close();
        ois.close();
    }

    /**
     * Salva l'insieme dei cluster su file
     * @param fileName il path del file su cui salvare il clusterset
     * @throws IOException se si verifica un errore durante la scrittura del file
     * @throws FileNotFoundException se il file specificato non viene trovato
     */
    public void salva(String fileName) throws FileNotFoundException, IOException{
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(C);
        fos.close();
        oos.close();
    }

    /**
     * Restituisce l'insieme dei cluster
     * @return l'insieme dei cluster
     */
    public ClusterSet getC(){
        return C;
    }

    /**
     * Esegue l'algoritmo K-means
     * @param data il dataset su cui eseguire l'algoritmo
     * @return il numero di iterazioni necessarie per raggiungere la convergenza
     * @throws OutOfRangeSampleSize lanciata se il numero di cluster e' maggiore del numero di transazioni nel dataset
     */
    public int kmeans(Data data) throws OutOfRangeSampleSize{
        int numberOfIterations=0;
        //STEP 1
        C.initializeCentroids(data);
        boolean changedCluster=false;
        do{
            numberOfIterations++;
                //STEP 2
            changedCluster=false;
            for(int i=0;i<data.getNumberOfExamples();i++){
                Cluster nearestCluster = C.nearestCluster(data.getItemSet(i));  //cluster di C + vicino all'oggetto tuple generato
                // da getItemset
                Cluster oldCluster=C.currentCluster(i); //ritorna il cluster di C che possiede la tupla identificata da i,
                //se non eisste viene null
                boolean currentChange=nearestCluster.addData(i);    //da addData torna true se aggiunge id all'arrayset
                //nearest cluster è gia elemento di C, quindi se addData è true ha aggiornato una sua tupla
                if(currentChange) //***
                    changedCluster=true;
                //rimuovo la tupla dal vecchio cluster
                if(currentChange && oldCluster!=null)   //se è null, significa che il cluster restituito da current non ha la tupla da togliere
                //il nodo va rimosso dal suo vecchio cluster
                    oldCluster.removeTuple(i);
            }
            //*** se la tupla restituita ha modificato nearestcluster (se addData ha restituito true) significa che il clsuter più vicino
            //alla tupla è nearest, quindi si aggiunge li e si toglie da oldcluster. altrimenti significa che la tupla è gia nel cluster
            //più vicino (oldcluster se è diverso da null. se è null sigifica che la tupla non appartiene a nessun cluster)
            //STEP 3
            C.updateCentroids(data);
        }
        while(changedCluster); //finche nessuna tupla cambia clsuter, ripeti
        return numberOfIterations;
    }
}
