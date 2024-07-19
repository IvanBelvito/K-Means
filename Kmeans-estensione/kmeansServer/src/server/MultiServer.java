package server;

import java.io.IOException;
import java.net.*;

/**
 * classe che rappresenta un server multi-client che accetta connessioni da più client contemporaneamente.
 */
public class MultiServer {

    /**
     * Numero di porta utilizzato dal server per mettersi in ascolto.
     */
    private int PORT = 8080;

    /**
     * Crea una nuova istanza di MultiServer con la porta specificata.
     * @param port La porta su cui il server ascolta le connessioni dei client.
     */
    public MultiServer(int port){
        PORT = port;
        this.run();
    }

    /**
     * Avvia il server e accetta le connessioni dai client.
     */
    void run(){
        ServerSocket s = null;
        try{
            s = new ServerSocket(PORT);
            try{
                while(true) {
                    Socket socket = s.accept();
                    try{
                        new ServerOneClient(socket);
                    }catch(IOException e){
                        socket.close();
                    }
                }
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }finally {
            try{
                s.close();
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Main utilizzato per creare un'istanza di Multiserver utilizzando la porta 8080 per mettere il server in ascolto
     * @param args
     */
    public static void main(String[] args){
        MultiServer multiServer = new MultiServer(8080);
    }
}
