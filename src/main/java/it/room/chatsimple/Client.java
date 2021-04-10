package it.room.chatsimple;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ma
 */
public class Client {

    String nomeUtente;  //Nome dell' utente
    String ip;          //Indirizzo di rete
    int porta;          //porta

    /**
     * Costruttore N° 1 - senza parametri, pone a default le variabili di classe
     */
    public Client() {
        nomeUtente = "Luisa Bianchi";
        ip = "127.0.0.1";           //localhost
        porta = 6868;
    }

    /**
     * Costruttore N° 2 - Parametrizzata, la sua istanza permette di specificare
     * tutti i parametri di connessione al Server
     *
     * @param nomeUtente
     * @param ip
     * @param porta
     */
    public Client(String nomeUtente, String ip, int porta) {
        this.nomeUtente = nomeUtente;
        this.ip = ip;
        this.porta = porta;
    }
    public Client(String nomeUtente) {
        this(nomeUtente,"127.0.0.1",6868);
    }

    public void init() throws IOException {

        print("**********************************");
        print("**         C L I E N T          **");
        print("**********************************");
        /*
        Cerchiamo un server attivo all' indirizzo ip:porta
        Se non lo trova, il metodo uscira' generando una eccezione di IO(Connessione rifiutata (Connection refused)).
        Il che costringerà a rieseguire il metodo di nuovo.
        Prima però assicuratevi che un Server sia attivo sul IP:porta, altrimenti uscirà di nuovo con errore IO
         */
        print("Provo a connettermi con un server all' indirizzo: " + ip + ":" + porta);
        Socket clientSocket = new Socket(ip, porta);    //Proviamo a cercare un Server attivo
        print("Connessione al server effettuata!");

        //Istanze delle classe per flussi di spedizione-ricezione dati attraverso il socket
        DataOutputStream spedisci = new DataOutputStream(clientSocket.getOutputStream());
        DataInputStream ricevi = new DataInputStream(clientSocket.getInputStream());

        /*
        Loop di digitazione messaggi verso il Server
        Non si esce da qui, finche' non si digita esce
         */
        while (true) {
            //Attendiamo messaggio da Server
            String readUTF = ricevi.readUTF();
            print(readUTF); //Print output del messaggio ricevuto dal Server
            //Richiesta input di un messaggio attraverso una Dialog Box
            String readLine = JOptionPane.showInputDialog(null, "Messaggio da spedire al Server...", "CLIENT["+nomeUtente+"]", JOptionPane.QUESTION_MESSAGE);
            String ilClientDice = spedisciString(readLine);
            spedisci.writeUTF(ilClientDice);
            printEcho(readLine);
            //Qui c'e' un difetto volutamente introdotto dal vostro insegnante, vediamo chi lo scopre per primo...
            if (readLine.toLowerCase().contains("esco")) { //Se il messaggio e' "esco", terminiamo il Server uscendo dal loop
                break;
            }

        }

        //Chiudi i flussi
        spedisci.close();
        ricevi.close();
        clientSocket.close();
    }

    /**
     * Aggiungiamo in testa al parametro st, il nomeUtente e la dicitura:"
     * dice:";
     *
     * @param st
     * @return
     */
    private String spedisciString(String st) {
        String stDiRitorno = String.format("[%s] dice:%s", nomeUtente, st);
        return stDiRitorno;
    }

    /**
     * Aggiungiamo in testa al parametro st la Stringa "Client:"
     * @param st 
     */    
    private void print(String st) {
        System.out.println("Client:" + st);
    }

    /**
     * Aggiungiamo in testa al parametro st una tabulazione(\t) per identificare meglio 
     * il messaggio spedito al Server.
     * @param st 
     */
    private void printEcho(String st) {
        System.out.println("\t>>:" + st);
    }

    
    /**
     * Punto di start della classe Client
     * @param args 
     */
    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.init();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
