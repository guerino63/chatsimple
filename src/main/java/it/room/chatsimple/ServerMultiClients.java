/*
 * Uso dei Threads per gestire piu' di un Client
 */
package it.room.chatsimple;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ma
 */
public class ServerMultiClients {

    //Variabili di classe - private(Solo la classe Server puo' vedere queste classi)
    private final String NOME_UTENTE;  //Nome dell' utente Server
    private final int PORTA;          //porta del server

    /**
     * Costruttore 1 - Le variabili di classe avranno valori di default
     */
    public ServerMultiClients() {
        NOME_UTENTE = "Server di Mario Rossi";
        PORTA = 6868;
    }

    /**
     * Costruttore N°2 - Il costruttore ha come parametri nomeUtente e porta Se
     * usiamo questa istanza possamo quindi cambiare alcune variabili di classe.
     *
     * @param nomeUtente
     * @param porta
     */
    public ServerMultiClients(String nomeUtente, int porta) {
        this.NOME_UTENTE = nomeUtente;
        this.PORTA = porta;
    }

    public void init() {

        print("**********************************");
        print("**         S E R V E R          **");
        print("**********************************");

        //Crea l' istanza della classe ServerSocket
        //La classe ServerSocket si mette in ascolto all' indirizzo IP attuale sulla porta
        //definita nella variabile di classe porta
        //L' indirizzo IP e' quello che vi assegna il vostro provider(TIM-VODAFONE...etc) 
        //Quando vi collegate alla rete.
        //Per scoprirlo usate uno dei tanti siti che vi ritorna il vostro IP
        //@see http://www.ilmioindirizzoip.it/
        ServerSocket sv;
        try {
            sv = new ServerSocket(PORTA);
        } catch (IOException ex) {
            Logger.getLogger(ServerMultiClients.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        final int MAX_CLIENTS = 3;
        for (int maxClientBeforeExit = MAX_CLIENTS; maxClientBeforeExit > 0; maxClientBeforeExit--) {
            print("Attendo connessione al client["+(MAX_CLIENTS-maxClientBeforeExit+1)+"]...");
            Socket accept;
            try {
                accept = sv.accept(); //Da qui il programma non si schioda finche non sentiamo 'bussare' un Client
            } catch (IOException ex) {
                Logger.getLogger(ServerMultiClients.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }
            print(String.format("Connessione ad un client effettuata! [Client=%s]", sv.getLocalSocketAddress().toString()));
            AscoltaClient ascoltaClient = new AscoltaClient(accept);
            ascoltaClient.start();
        }
    }

    class AscoltaClient extends Thread {

        Socket accept;

        public AscoltaClient(Socket sock) {
            accept = sock;
        }

        @Override
        public void run() {
            //Se siamo qui, un Client si è connesso...
            //Creo istanze delle classi per flussi di spedizione-ricezione dati attraverso il socket
            DataOutputStream spedisci;
            try {
                spedisci = new DataOutputStream(accept.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(ServerMultiClients.class.getName()).log(Level.SEVERE, null, ex);
                spedisci = null;
            }
            DataInputStream ricevi;
            try {
                ricevi = new DataInputStream(accept.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ServerMultiClients.class.getName()).log(Level.SEVERE, null, ex);
                ricevi = null;
            }

            if (spedisci == null || ricevi == null) {
                return;
            }

            //Spediamo messaggio di benvenuto al Client...
            String ilServerDice = spedisciString("Benvenuto!");
            try {
                spedisci.writeUTF(ilServerDice);
            } catch (IOException ex) {
                Logger.getLogger(ServerMultiClients.class.getName()).log(Level.SEVERE, null, ex);
            }
            printEcho("Benvenuto!");

            /*
        Loop di spedizione messaggi verso Client.
        Si esce quando si riceve dal Client il messaggio "esco"
             */
            while (true) {
                //Attendiamo messaggio da Client
                String readUTF = null;
                try {
                    readUTF = ricevi.readUTF();
                } catch (IOException ex) {
                    Logger.getLogger(ServerMultiClients.class.getName()).log(Level.SEVERE, null, ex);
                }
                print(readUTF); //Print output del messaggio ricevuto dal Client
                
                if(readUTF==null) //Inaccettabile ritorno di null, usciamo dal server
                    return;
                //Estrapoliamo il nome del Client
                int n1 = readUTF.indexOf('[');
                int n2 = readUTF.indexOf(']');
                String nomeDelClient;
                if(n1 != -1 && n2 != -1){
                    n2++;   //Per prendere anche il carattere ']'
                    nomeDelClient = readUTF.substring(n1,n2);
                } else {
                    nomeDelClient = "---";
                }
                //Qui c'e' qualche difetto volutamente introdotto dal vostro insegnante, vediamo chi lo scopre per primo...
                if (readUTF.toLowerCase().contains("esco")) { //Se il messaggio è "esco", terminiamo il Server uscendo dal loop
                    break;
                }
                //Richiesta input di un messaggio da pedire al Client
                String readLine = JOptionPane.showInputDialog(null, "Messaggio da spedire al Client"+nomeDelClient+"...", "SERVER["+NOME_UTENTE+"]", JOptionPane.QUESTION_MESSAGE);
                ilServerDice = spedisciString(readLine);
                try {
                    spedisci.writeUTF(ilServerDice);
                } catch (IOException ex) {
                    Logger.getLogger(ServerMultiClients.class.getName()).log(Level.SEVERE, null, ex);
                }
                printEcho(readLine);
            }

            try {
                //Chiudiamo i canali dei sockets
                spedisci.close();
                ricevi.close();
                accept.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerMultiClients.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Aggiungiamo in testa al parametro st, il nomeUtente e la dicitura:"
     * dice:";
     *
     * @param st
     * @return
     */
    private String spedisciString(String st) {
        String stDiRitorno = String.format("[%s] dice:%s", NOME_UTENTE, st);
        return stDiRitorno;
    }

    /**
     * Aggiungiamo in testa al parametro st la Stringa "ServerMultiClients:"
     *
     * @param st
     */
    private void print(String st) {
        System.out.println("ServerMultiClient:" + st);
    }

    /**
     * Aggiungiamo in testa al parametro st una tabulazione(\t) per identificare
     * meglio il messaggio spedito al Client.
     *
     * @param st
     */
    private void printEcho(String st) {
        System.out.println("\t>>:" + st);
    }

    /**
     * Punto di start della classe Server
     * @param args 
     */
    public static void main(String[] args) {
        ServerMultiClients server = new ServerMultiClients();
        server.init();
    }
}
