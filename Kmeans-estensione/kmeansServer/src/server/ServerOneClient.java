package server;

import java.net.Socket;
import java.io.*;
import data.OutOfRangeSampleSize;
import mining.KMeansMiner;
import data.Data;

/**
 * Classe che rappresenta un gestore di connessione per un singolo client che si connette al server
 * Ogni istanza di questa classe gestisce la comunicazione con un client
 */
public class ServerOneClient extends Thread{

    /**
     * Il socket utilizzato per la comunicazione con il client.
     */
    private Socket socket;

    /**
     * Stream per la lettura degli oggetti dal client.
     */
    private ObjectInputStream in;

    /**
     * Stream per la scrittura degli oggetti verso il client.
     */
    private ObjectOutputStream out;

    /**
     * Oggetto KMeansMiner utilizzato per le operazioni di clustering.
     */
    private KMeansMiner kmeans;

    /**
     * Crea una nuova istanza di ServerOneClient per gestire la comunicazione con un client.
     * @param s Il socket utilizzato per la comunicazione con il client.
     * @throws IOException Se si verifica un errore durante l'inizializzazione della comunicazione.
     */
    public ServerOneClient(Socket s) throws IOException{
        try{
            this.socket = s;
            this.in = new ObjectInputStream(this.socket.getInputStream());
            this.out = new ObjectOutputStream(this.socket.getOutputStream());
            this.start();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * Avvia il gestore della connessione per il client.
     */
    public void run(){
        String fileName = "";
        String path = "salvataggi/";
        String table = "";
        Data data = null;
        Integer nCluster = null;
        kmeans= null;

        try {
            while(true) {
                int choice = (Integer)in.readObject();
                switch (choice) {
                    case -1:
                        out.reset();
                        continue;
                    case 0:
                        try {
                            table = (String)in.readObject();
                            data = new Data(table);
                            out.writeObject("OK");
                        } catch (Exception e) {
                            if(e.getMessage().contains("this.data")){
                                out.writeObject("Tabella non trovata.");
                            }else{
                                out.writeObject("Si è verificato un errore mentre il server si stava connettendo al database. Ritenta la connessione.");
                            }
                        }
                        break;
                    case 1: //leggi da db
                        try {
                            nCluster = (Integer)in.readObject();
                            kmeans = new KMeansMiner(nCluster);
                            int numIter = kmeans.kmeans(data);
                            out.writeObject("OK");
                            out.writeObject("Numero di iterazioni: " + numIter + "\n" + kmeans.getC().toString(data));
                        } catch (OutOfRangeSampleSize e) {
                            out.writeObject(e.getMessage());
                        } catch (NegativeArraySizeException e){
                            out.writeObject("Numero cluster non valido, inserisci un numero positivo.");
                        } catch (NullPointerException e){
                            out.writeObject("Numero cluster non valido.");
                        }
                        break;
                    case 2:
                        try {
                            fileName = path + table + nCluster + "k.dat";
                            System.out.println(fileName);
                            kmeans.salva(fileName);
                            out.writeObject("OK");
                        } catch (IOException e) {
                            out.writeObject("Errore col file.");
                        }  catch (Exception e) {
                            if(e.getMessage().contains("this.data")) {
                                out.writeObject("Tabella non trovata.");
                            }}

                        break;
                    case 3:
                        try{
                            table = ((String)in.readObject());
                            data = new Data(table);
                            nCluster = (Integer)in.readObject();
                            fileName = path + table + nCluster + "k.dat";
                            System.out.println(fileName);
                            kmeans = new KMeansMiner(fileName);
                            out.writeObject("OK");
                            out.writeObject(kmeans.getC().toString(data));
                        }catch (FileNotFoundException e) {
                            out.writeObject("File non trovato.");
                        }catch (Exception e) {
                            if(e.getMessage().contains("this.data")){
                                out.writeObject("File non trovato.");
                            }else{
                                out.writeObject("Si è verificato un errore mentre il server si stava connettendo al database.");
                            }
                        }

                        break;
                    default:
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }finally {
            try{
                socket.close();
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
    }

}
