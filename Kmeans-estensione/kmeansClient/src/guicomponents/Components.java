package guicomponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Questa classe rappresenta un insieme di componenti grafiche e stili per l'applicazione Kmeans.
 * Fornisce metodi per impostare stili per: bottoni, etichette e aree di testo; contiene anche un messaggio di benvenuto.
 */
public class Components {

    /**
     * messaggio di benvenuto nell'applicazione.
     */
    private final String welcomeMessage;

    /**
     * font personalizato usato per le scritte.
     */
    public Font kmeansFont;

    /**
     * Crea un nuovo oggetto Components e inizializza il messaggio di benvenuto e lo stile del font.
     */
    public Components() {
        welcomeMessage= "Benvenuta/o nell'applicazione Kmeans.\n"
                + "Seleziona 'Carica da File' per caricare i cluster da un file già esistente.\n"
                + "In alternativa, seleziona 'Carica da Database' per caricare i cluster da un database.\n"
                + "Questi saranno successivamente memorizzati su un file e potrai visionarli tramite il primo pulsante.";
        kmeansFont = new Font("Verdana", Font.BOLD, 12);
    }


    /**
     * Mostra un messaggio di benvenuto in una finestra di dialogo.
     * Utilizza la classe Timer per impostare un timer il quale definisce dopo quanti millisecondi verrà visualizzato il messaggio
     */
    public void WelcomeMessage(){
        // Imposta il timer per visualizzare il messaggio di benvenuto dopo un ritardo
        Timer timer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mostra il messaggio di benvenuto
                JOptionPane.showMessageDialog(null, welcomeMessage, "KMEANS", JOptionPane.INFORMATION_MESSAGE);
                ((Timer) e.getSource()).stop();
            }
        });

        // Avvia il timer dopo un ritardo
        timer.setInitialDelay(500); // Ritardo
        timer.setRepeats(false); // Il timer viene eseguito solo una volta
        timer.start();

    }

    /**
     * Restituisce un JButton con uno stile predefinito per "Carica da File".
     * @return loadFromFileButtonStyle con stile predefinito.
     */
    public JButton setloadFromFileButtonStyle(){
        JButton loadFromFileButtonStyle = new JButton("Carica da File");
        loadFromFileButtonStyle.setFocusPainted(false);  //usato per risolvere un problema all'apertura dell'app: il button era cerchiato come se fosse premuto
        loadFromFileButtonStyle.setBackground(Color.BLACK);
        loadFromFileButtonStyle.setForeground(Color.YELLOW);
        loadFromFileButtonStyle.setFont(kmeansFont);
        return loadFromFileButtonStyle;
    }

    /**
     * Restituisce un JButton con uno stile predefinito per "Carica da DB".
     * @return loadFromDBButtonStyle con stile predefinito.
     */
    public JButton setloadFromDBButtonStyle(){
        JButton loadFromDBButtonStyle = new JButton("Carica da DB");
        loadFromDBButtonStyle.setFocusPainted(false);
        loadFromDBButtonStyle.setBackground(Color.BLACK);
        loadFromDBButtonStyle.setForeground(Color.YELLOW);
        loadFromDBButtonStyle.setFont(kmeansFont);
        return loadFromDBButtonStyle;
    }

    /**
     * Restituisce un JButton con uno stile predefinito per "Help".
     * @return helpButtonStyle con stile predefinito.
     */
    public JButton setHelpButtonStyle(){
        JButton helpButtonStyle = new JButton("Help");
        helpButtonStyle.setFocusPainted(false);
        helpButtonStyle.setBackground(Color.BLACK);
        helpButtonStyle.setForeground(Color.GREEN);
        helpButtonStyle.setFont(kmeansFont);
        return helpButtonStyle;
    }


    /**
     * Restituisce una JLabel con uno stile predefinito per "Risultato:".
     * @return resultLableStyle con stile predefinito.
     */
    public JLabel setResultLableStyle(){
        JLabel resultLableStyle = new JLabel("Risultato:");
        resultLableStyle.setFont(kmeansFont);
        resultLableStyle.setForeground(Color.YELLOW);
        resultLableStyle.setBackground(Color.BLACK);
        resultLableStyle.setOpaque(true);
        return resultLableStyle;
    }

    /**
     * Restituisce una JTextArea con uno stile predefinito per l'area di testo della console.
     * @return consoleTextAreaStyle con stile predefinito.
     */
    public JTextArea setConsoleTextAreaStyle(){
        JTextArea consoleTextAreaStyle = new JTextArea();
        consoleTextAreaStyle.setEditable(false);
        consoleTextAreaStyle.setFont(kmeansFont);
        consoleTextAreaStyle.setBackground(Color.LIGHT_GRAY);
        consoleTextAreaStyle.setForeground(Color.BLACK);
        return consoleTextAreaStyle;
    }

}
