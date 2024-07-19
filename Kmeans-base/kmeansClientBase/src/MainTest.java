import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import exception.ServerException;
import keyboardinput.Keyboard;



public class MainTest {
    /**
     * ObjectOutputStream utilizzato per comunicare con il server con operazioni in scrittura
     */
    private ObjectOutputStream out;

    /**
     * ObjectInputStream utilizzato per comunicare con il server con operazioni in lettura
     */
    private ObjectInputStream in;

    /**
     * @param ip  L'indirizzo IP del server.
     * @param port Il numero di porta a cui connettersi.
     * @throws IOException Se si verifica un errore di I/O durante la creazione del socket.
     */
    public MainTest(String ip, int port) throws IOException{
        InetAddress addr = InetAddress.getByName(ip); //ip
        System.out.println("addr = " + addr);
        Socket socket = new Socket(addr, port); //Port
        System.out.println(socket);

        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());	; // stream con richieste del client
    }

    /**
     * Visualizza un menu e restituisce la scelta dell'utente.
     * @return la scelta dell'utente (un intero tra 1 o 2).
     */
    private int menu() {
        int answer=0;
        String input=" ";
        System.out.println("\nScegli una opzione: ");
        System.out.println("(1) Carica Cluster da File.");
        System.out.println("(2) Carica Dati.");
        do {
            System.out.print("\nInserisci una risposta valida (1/2): ");
            input = Keyboard.readString().trim();;  // Leggi l'input come stringa
            if (input.equals("1")) {
                answer=1;
            }else if(input.equals("2")){
                answer=2;
            }else{
                System.out.println("Input non valido. Riprova.");
            }
        } while (answer<1||answer>2);
        return answer;
    }

    /**
     * Riceve dal server i cluster memorizzati su un file il cui nome è inserito dall'utente come "nome tabella" + "numero cluster"
     * @return cluster letti da un file già memorizzato restituiti come una stringa o null se l'input non è valido o si verifica un errore.
     * @throws SocketException    Eccezione lanciata in caso di problemi di socket.
     * @throws ServerException    Eccezione che rappresenta un errore lato server.
     * @throws IOException        Eccezione lanciata in caso di problemi di I/O.
     * @throws ClassNotFoundException Eccezione lanciata se il server invia una classe non trovata durante la deserializzazione.
     */
    private String learningFromFile() throws SocketException, ServerException,IOException,ClassNotFoundException{
        out.writeObject(3);
        System.out.println("Ricerca un file inserendo... ");
        System.out.print("Nome tabella: ");
        String tabName=Keyboard.readString().trim();
        out.writeObject(tabName);
        System.out.print("Numero cluster: ");
        int k=Keyboard.readInt();
        out.writeObject(k);
        String result = (String)in.readObject();
        if(result.equals("OK"))
            return (String)in.readObject();
        else throw new ServerException(result);
    }

    /**
     * Invia una richiesta al server per memorizzare una tabella da un database.
     * @throws SocketException    Eccezione lanciata in caso di problemi di socket.
     * @throws ServerException    Eccezione che rappresenta un errore lato server.
     * @throws IOException        Eccezione lanciata in caso di problemi di I/O.
     * @throws ClassNotFoundException Eccezione lanciata se il server invia una classe non trovata durante la deserializzazione.
     */
    private void storeTableFromDb() throws SocketException,ServerException,IOException,ClassNotFoundException{
        out.writeObject(0);
        System.out.print("Nome tabella: ");
        String tabName=Keyboard.readString().trim();
        out.writeObject(tabName);
        String result = (String)in.readObject();
        if(!result.equals("OK"))
            throw new ServerException(result);

    }

    /**
     * Riceve dal server i cluster memorizzati su una tabella del databse.
     * @return stringa che descrive i cluster o null se l'input non è valido o si verifica un errore.
     * @throws SocketException    Eccezione lanciata in caso di problemi di socket.
     * @throws ServerException    Eccezione che rappresenta un errore lato server.
     * @throws IOException        Eccezione lanciata in caso di problemi di I/O.
     * @throws ClassNotFoundException Eccezione lanciata se il server invia una classe non trovata durante la deserializzazione.
     */
    private String learningFromDbTable() throws SocketException,ServerException,IOException,ClassNotFoundException{
        out.writeObject(1);
        System.out.print("Numero di cluster: ");
        int k=Keyboard.readInt();
        out.writeObject(k);
        String result = (String)in.readObject();
        if(result.equals("OK")){
            //System.out.println("Clustering output: "+in.readObject());
            return (String)in.readObject();
        }
        else throw new ServerException(result);
    }


    /**
     * Invia una richiesta al server per salvare i cluster correnti in un file.
     * @throws SocketException    Eccezione lanciata in caso di problemi di socket.
     * @throws ServerException    Eccezione che rappresenta un errore lato server.
     * @throws IOException        Eccezione lanciata in caso di problemi di I/O.
     * @throws ClassNotFoundException Eccezione lanciata se il server invia una classe non trovata durante la deserializzazione.
     */
    private void storeClusterInFile() throws SocketException,ServerException,IOException,ClassNotFoundException{
        out.writeObject(2);
        String result = (String)in.readObject();
        if(!result.equals("OK"))
            throw new ServerException(result);
    }

    /**
     * Il metodo che avvia l'applicazione lato client.
     * @param args gli argomenti della riga di comando (indirizzo IP e numero di porta).
     */
    public static void main(String[] args) {
        String ip = null;
        int port = 0;
        try {
            if (args.length < 2) {
                throw new ArrayIndexOutOfBoundsException();
            }
            ip = args[0];
            port = Integer.parseInt(args[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Inserire indirizzo IP e numero di porta.");
            return;
        } catch (NumberFormatException e) {
            System.out.println("Il numero di porta non è valido.");
            return;
        }
        MainTest main=null;
        try{
            main=new MainTest(ip,port);
        }
        catch (IOException e){
            System.out.println(e);
            return;
        }

        do{
            int menuAnswer=main.menu();
            switch(menuAnswer)
            {
                case 1:     //carica cluster da file
                    try {
                        String kmeans=main.learningFromFile();
                        System.out.println(kmeans);
                    }
                    catch (SocketException e) {
                        System.out.println(e);
                        return;
                    }
                    catch (FileNotFoundException e) {
                        System.out.println(e);
                        return ;
                    } catch (IOException e) {
                        System.out.println(e);
                        return;
                    } catch (ClassNotFoundException e) {
                        System.out.println(e);
                        return;
                    }
                    catch (ServerException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2: // learning from db

                    while(true){
                        try{
                            main.storeTableFromDb();
                            break; //esce fuori dal while
                        }

                        catch (SocketException e) {
                            System.out.println(e);
                            return;
                        }
                        catch (FileNotFoundException e) {
                            System.out.println(e);
                            return;

                        } catch (IOException e) {
                            System.out.println(e);
                            return;
                        } catch (ClassNotFoundException e) {
                            System.out.println(e);
                            return;
                        }
                        catch (ServerException e) {
                            System.out.println(e.getMessage());
                        }
                    }

                    String answer="y";
                    do{
                        try
                        {
                            String clusterSet=main.learningFromDbTable();
                            System.out.println(clusterSet);

                            main.storeClusterInFile();

                        }
                        catch (SocketException e) {
                            System.out.println(e);
                            return;
                        }
                        catch (FileNotFoundException e) {
                            System.out.println(e);
                            return;
                        }
                        catch (ClassNotFoundException e) {
                            System.out.println(e);
                            return;
                        }catch (IOException e) {
                            System.out.println(e);
                            return;
                        }
                        catch (ServerException e) {
                            System.out.println(e.getMessage());
                        }
                        do {
                            System.out.print("Vuoi ripetere l'esecuzione? (y/n) ");
                            answer = Keyboard.readString().trim(); // Usa trim per rimuovere spazi extra
                        } while (!(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n")));

                    } while(answer.equalsIgnoreCase("y"));
                    break; //fine case 2
                default:
                    System.out.println("Opzione non valida!");
            }
            String answer=" ";
            do {
                System.out.print("Vuoi scegliere una nuova operazione da menu?(y/n) ");
                answer = Keyboard.readString().trim();
            } while (!(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n")));

            if(answer.equalsIgnoreCase("n")){
                break;
            }
        }
        while(true);
    }
}



