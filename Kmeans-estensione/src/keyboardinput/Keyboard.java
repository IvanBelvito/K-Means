//********************************************************************
//  Keyboard.java       Author: Lewis and Loftus
//
//  Facilitates keyboard input by abstracting details about input
//  parsing, conversions, and exception handling.
//********************************************************************

package keyboardinput;

import java.io.*;
import java.util.*;

/**
 * Classe utilizzata per leggere un input da tastiera.
 */
public class Keyboard {

    /**
     * ************* Error Handling Section **************************
     */
    private static boolean printErrors = true;

    /**
     * contatore degli errori
     */
    private static int errorCount = 0;

    /**
     * Restituisce il conteggio attuale degli errori.
     *
     * @return il conteggio degli errori.
     */
    public static int getErrorCount() {
        return errorCount;
    }

    /**
     * Reimposta il conteggio attuale degli errori a zero.
     * @param count il numero di errori da impostare.
     */
    public static void resetErrorCount(int count) {
        errorCount = 0;
    }

    /**
     * Restituisce un valore booleano che indica se gli errori di input vengono
     * attualmente stampati su standard output.
     *
     * @return `true` se gli errori vengono stampati, altrimenti `false`.
     */
    public static boolean getPrintErrors() {
        return printErrors;
    }

    /**
     * Imposta un valore booleano che indica se gli errori di input devono essere
     * stampati su standard output.
     *
     * @param flag `true` per stampare gli errori, `false` per non stamparli.
     */
    public static void setPrintErrors(boolean flag) {
        printErrors = flag;
    }

    /**
     * Incrementa il conteggio degli errori e stampa il messaggio di errore se
     * appropriato.
     *
     * @param str il messaggio di errore da stampare.
     */
    private static void error(String str) {
        errorCount++;
        if (printErrors)
            System.out.println(str);
    }

    // ************* Tokenized Input Stream Section ******************

    /**
     * Token corrente da analizzare.
     */
    private static String current_token = null;

    /**
     * Lettore di token.
     */
    private static StringTokenizer reader;

    /**
     * Lettore di input.
     */
    private static BufferedReader in = new BufferedReader(
            new InputStreamReader(System.in));

    /**
     * Restituisce il prossimo token di input, assumendo che possa essere su
     * linee di input successive.
     *
     * @return il prossimo token di input.
     */
    private static String getNextToken() {
        return getNextToken(true);
    }

    /**
     * Restituisce il prossimo token di input, che potrebbe già essere stato
     * letto.
     *
     * @param skip indica se dovrebbero essere saltate le linee di input successive.
     * @return il prossimo token di input.
     */
    private static String getNextToken(boolean skip) {
        String token;

        if (current_token == null)
            token = getNextInputToken(skip);
        else {
            token = current_token;
            current_token = null;
        }

        return token;
    }

    /**
     * Restituisce il token successivo dall'input, che può provenire dalla linea di
     * input corrente o da una successiva. Il parametro determina se devono essere
     * usate le linee successive.
     *
     * @param skip indica se devono essere saltate le linee di input successive
     * @return il prossimo token
     */
    private static String getNextInputToken(boolean skip) {
        final String delimiters = " \t\n\r\f";
        String token = null;

        try {
            if (reader == null)
                reader = new StringTokenizer(in.readLine(), delimiters, true);

            while (token == null || ((delimiters.indexOf(token) >= 0) && skip)) {
                while (!reader.hasMoreTokens())
                    reader = new StringTokenizer(in.readLine(), delimiters,
                            true);

                token = reader.nextToken();
            }
        } catch (Exception exception) {
            token = null;
        }

        return token;
    }

    /**
     * Verifica se non ci sono più token da leggere in input
     *
     * @return `true` se non ci sono più token da leggere, altrimenti `false`.
     */
    public static boolean endOfLine() {
        return !reader.hasMoreTokens();
    }

    // ************* Reading Section *********************************

    /**
     * Legge una stringa in input
     *
     * @return la stringa letta in input.
     */
    public static String readString() {
        String str;

        try {
            str = getNextToken(false);
            while (!endOfLine()) {
                str = str + getNextToken(false);
            }
        } catch (Exception exception) {
            //error("Error reading String data, null value returned.");
            str = null;
        }
        return str;
    }

    /**
     * Legge una parola (una sottostringa delimitata da spazi) in input
     *
     * @return la parola letta in input.
     */
    public static String readWord() {
        String token;
        try {
            token = getNextToken();
        } catch (Exception exception) {
            error("Error reading String data, null value returned.");
            token = null;
        }
        return token;
    }

    /**
     * Legge un valore booleano in input.
     *
     * @return il booleano letto in input.
     */
    public static boolean readBoolean() {
        String token = getNextToken();
        boolean bool;
        try {
            if (token.toLowerCase().equals("true"))
                bool = true;
            else if (token.toLowerCase().equals("false"))
                bool = false;
            else {
                error("Error reading boolean data, false value returned.");
                bool = false;
            }
        } catch (Exception exception) {
            error("Error reading boolean data, false value returned.");
            bool = false;
        }
        return bool;
    }

    /**
     * Legge un carattere in input.
     *
     * @return il carattere letto in input.
     */
    public static char readChar() {
        String token = getNextToken(false);
        char value;
        try {
            if (token.length() > 1) {
                current_token = token.substring(1, token.length());
            } else
                current_token = null;
            value = token.charAt(0);
        } catch (Exception exception) {
            error("Error reading char data, MIN_VALUE value returned.");
            value = Character.MIN_VALUE;
        }

        return value;
    }

    /**
     * Legge un int in input.
     *
     * @return int letto in input.
     */
    public static int readInt() {
        String token = getNextToken();
        int value;
        try {
            value = Integer.parseInt(token);
        } catch (Exception exception) {
            //error("Error reading int data, MIN_VALUE value returned.");
            value = Integer.MIN_VALUE;
        }
        return value;
    }

    /**
     * Legge un long in input.
     *
     * @return il long letto in input.
     */
    public static long readLong() {
        String token = getNextToken();
        long value;
        try {
            value = Long.parseLong(token);
        } catch (Exception exception) {
            error("Error reading long data, MIN_VALUE value returned.");
            value = Long.MIN_VALUE;
        }
        return value;
    }

    /**
     * Legge un float in input.
     *
     * @return il float letto in input.
     */
    public static float readFloat() {
        String token = getNextToken();
        float value;
        try {
            value = (new Float(token)).floatValue();
        } catch (Exception exception) {
            error("Error reading float data, NaN value returned.");
            value = Float.NaN;
        }
        return value;
    }

    /**
     * Legge un double in input.
     *
     * @return il double letto in input.
     */
    public static double readDouble() {
        String token = getNextToken();
        double value;
        try {
            value = (new Double(token)).doubleValue();
        } catch (Exception exception) {
            error("Error reading double data, NaN value returned.");
            value = Double.NaN;
        }
        return value;
    }
}
