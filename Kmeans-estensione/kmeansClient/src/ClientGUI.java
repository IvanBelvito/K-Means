import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import exception.ServerException;
import guicomponents.Components;

/**
 * La classe ClientGUI rappresenta l'interfaccia grafica per l'applicazione Kmeans.
 * Fornisce una finestra GUI in cui gli utenti possono interagire con il server per eseguire operazioni
 * come il caricamento dei dati dai file o dai database, l'esecuzione dell'algoritmo Kmeans e la visualizzazione dei risultati.
 */
public class ClientGUI extends JFrame {

    /**
     * ObjectOutputStream utilizzato per comunicare con il server con operazioni in scrittura
     */
    private ObjectOutputStream out;

    /**
     * ObjectInputStream utilizzato per comunicare con il server con operazioni in lettura
     */
    private ObjectInputStream in;

    /**
     * label utilizzata nell'interfaccia grafica per introdurre alla sezione in cui compare il risultato di un operazione
     */
    private JLabel resultLabel;

    /**
     * area in cui è possibile visualizzare il risultato ottenuto da un'operazione
     */
    private JTextArea consoleTextArea;

    /**
     * Booleano utilizzato per comprendere se, l'input inserito dall'utente è corretto e si può proseguire con un operazione;
     * in caso di operazione annullata passa a false
     */
    private boolean inputValid;

    /**
     * classe utilizzata per caricare gli stili dei componenti del pannello grafico e il messaggio di benvenuto all'utente
     */
    private Components components;


    /**
     * Costruisce un'istanza di ClientGUI con l'indirizzo IP del server e la porta specificati.
     * @param ip   L'indirizzo IP del server.
     * @param port La porta del server.
     * @throws IOException Eccezione lanciata in caso di problemi di I/O.
     */
    public ClientGUI(String ip, int port) throws IOException {
        components = new Components();
        setTitle("Kmeans");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        Dimension minimumSize = new Dimension(800, 600); // Cambia queste dimensioni secondo le tue esigenze
        setMinimumSize(minimumSize);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.GRAY);
        buttonPanel.setLayout(new FlowLayout());

        JButton loadFromFileButton = components.setloadFromFileButtonStyle();
        JButton loadFromDBButton = components.setloadFromDBButtonStyle();
        JButton infoButton = components.setHelpButtonStyle();

        buttonPanel.add(loadFromFileButton);
        buttonPanel.add(loadFromDBButton);
        buttonPanel.add(infoButton);

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());

        resultLabel = components.setResultLableStyle();
        resultPanel.add(resultLabel, BorderLayout.NORTH);

        consoleTextArea = components.setConsoleTextAreaStyle();
        JScrollPane scrollPane = new JScrollPane(consoleTextArea);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);

        InetAddress addr = InetAddress.getByName(ip);
        Socket socket = new Socket(addr, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consoleTextArea.setText("Seleziona 'Carica da File' per caricare i cluster da un file già esistente.\n" +
                        "Dovrai inserire il nome del file e il numero di cluster per avviare la ricerca, per esempio playtennis7.\n" +
                        "In alternativa, seleziona 'Carica da Database' per caricare i cluster da un database.\n" +
                        "Dovrai inserire il numero di cluster da generare, per esempio 5.\n" +
                        "Questi saranno successivamente memorizzati su un file e potrai visionarli tramite il primo pulsante.");

            }
        });

        loadFromFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consoleTextArea.setText("");
                try {
                    String kmeans = learningFromFile();
                    if (kmeans != null) {
                        resultLabel.setText("Risultato:");
                        consoleTextArea.append(kmeans + "\n");
                    }
                } catch (SocketException ex) {
                    consoleTextArea.append(ex.getMessage() + "\n");
                } catch (FileNotFoundException ex) {
                    consoleTextArea.append(ex.getMessage() + "\n");
                } catch (IOException ex) {
                    consoleTextArea.append(ex.getMessage() + "\n");
                } catch (ClassNotFoundException ex) {
                    consoleTextArea.append(ex.getMessage() + "\n");
                } catch (ServerException ex) {
                    consoleTextArea.append( ex.getMessage() + "\n");
                }
            }
        });

        loadFromDBButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                consoleTextArea.setText("");
                try {
                    storeTableFromDb();
                    String result = learningFromDbTable();
                    if (result != null) {
                        resultLabel.setText("Risultato:");
                        consoleTextArea.append(result + "\n");
                        storeClusterInFile();
                    }
                } catch (SocketException ex) {
                    consoleTextArea.append(ex.getMessage() + "\n");
                } catch (FileNotFoundException ex) {
                    consoleTextArea.append(ex.getMessage() + "\n");
                } catch (IOException ex) {
                    consoleTextArea.append(ex.getMessage() + "\n");
                } catch (ClassNotFoundException ex) {
                    consoleTextArea.append(ex.getMessage() + "\n");
                } catch (ServerException ex) {
                    consoleTextArea.append(ex.getMessage() + "\n");
                }
            }
        });

        setVisible(true);
    }

    /**
     * Riceve dal server i cluster memorizzati su un file il cui nome è inserito dall'utente come "nome tabella" + "numero cluster"
     * @return cluster letti da un file già memorizzato restituiti come una stringa o null se l'input non è valido o si verifica un errore.
     * @throws SocketException    Eccezione lanciata in caso di problemi di socket.
     * @throws ServerException    Eccezione che rappresenta un errore lato server.
     * @throws IOException        Eccezione lanciata in caso di problemi di I/O.
     * @throws ClassNotFoundException Eccezione lanciata se il server invia una classe non trovata durante la deserializzazione.
     */
    private String learningFromFile() throws SocketException, ServerException, IOException, ClassNotFoundException {
        String tabName;
        tabName = JOptionPane.showInputDialog("Nome tabella: ");
        if (tabName == null || tabName.isEmpty()) {
            out.writeObject(-1);
            inputValid = false;
            return null;
        } else {
            inputValid = true;
        }
        if (inputValid) {   //se true
            int k;
            String kInput;
            kInput = JOptionPane.showInputDialog("Numero cluster: ");
            if (kInput == null || kInput.isEmpty()) {
                out.writeObject(-1);
                inputValid = false;
                return null;
            }
            out.writeObject(3);
            out.writeObject(tabName);
            try {
                k = Integer.parseInt(kInput);
            } catch (NumberFormatException e) {
                k = -1;
            }
            out.writeObject(k);
            String result = (String) in.readObject();
            inputValid = false;
            if (result.equals("OK")) {
                out.reset();
                return (String) in.readObject();
            } else {
                out.reset();
                throw new ServerException(result);
            }
        } else return null;
    }

    /**
     * Riceve dal server i cluster memorizzati su una tabella del databse.
     * @return stringa che descrive i cluster o null se l'input non è valido o si verifica un errore.
     * @throws SocketException    Eccezione lanciata in caso di problemi di socket.
     * @throws ServerException    Eccezione che rappresenta un errore lato server.
     * @throws IOException        Eccezione lanciata in caso di problemi di I/O.
     * @throws ClassNotFoundException Eccezione lanciata se il server invia una classe non trovata durante la deserializzazione.
     */
    private String learningFromDbTable() throws SocketException, ServerException, IOException, ClassNotFoundException {
        int k;
        boolean inputValido = false;
        String kInput = null;
        if (inputValid == true) {
            while (!inputValido) {
                kInput = JOptionPane.showInputDialog("Numero cluster: ");
                if (kInput == null || kInput.isEmpty()) {
                    out.writeObject(-1);
                    inputValid = false;
                    consoleTextArea.setText("");
                    return null;
                }
                try {
                    k = Integer.parseInt(kInput);
                    inputValido = true; // L'input è valido se la conversione in int ha successo
                } catch (NumberFormatException e) {
                    // L'input non è un numero intero valido, riprova
                    consoleTextArea.append("Inserisci un numero intero valido.");
                }
            }
            out.writeObject(1);
            k = Integer.parseInt(kInput);
            out.writeObject(k);
            String result = (String) in.readObject();
            inputValid = false;
            if (result.equals("OK")) {
                //System.out.println("Clustering output: "+in.readObject());
                out.reset();
                return (String) in.readObject();
            } else {
                out.reset();
                throw new ServerException(result);
            }

        } else return null;
    }

    /**
     * Invia una richiesta al server per memorizzare una tabella da un database.
     * @throws SocketException    Eccezione lanciata in caso di problemi di socket.
     * @throws ServerException    Eccezione che rappresenta un errore lato server.
     * @throws IOException        Eccezione lanciata in caso di problemi di I/O.
     * @throws ClassNotFoundException Eccezione lanciata se il server invia una classe non trovata durante la deserializzazione.
     */
    private void storeTableFromDb() throws SocketException,ServerException,IOException,ClassNotFoundException{
        String tabName;
        tabName = JOptionPane.showInputDialog("Nome tabella: ");

        if (tabName == null || tabName.isEmpty()) {
            out.writeObject(-1);
        }else {
            inputValid=true;
            out.writeObject(0);
            out.writeObject(tabName);
            String result = (String) in.readObject();
            if (!result.equals("OK"))
                throw new ServerException(result);
        }
        out.reset();
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
        out.reset();
    }

    /**
     * Main per l'esecuzione dell'applicazione client.
     * @param args Gli argomenti della riga di comando, che includono l'indirizzo IP del server e la porta.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                if (args.length < 2) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                ClientGUI clientGUI = new ClientGUI(args[0], Integer.parseInt(args[1]));
                clientGUI.components.WelcomeMessage();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Inserire indirizzo IP e numero di porta.");
            } catch (NumberFormatException e) {
                System.out.println("Il numero di porta non è valido.");
            }
        });
    }
}

