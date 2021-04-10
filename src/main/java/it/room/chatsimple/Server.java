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
 * IL SERVER
 * Classe che 'ascolta ed aspetta' che in rete una classe Client chieda di essere ascoltata.
 * L' ascolto in rete si effettua usando un 
 * indirizzo IP(indirizzo internet) ed una porta(meglio se compresa tra 6000 e 65000) che 
 * Server e Client conoscono e condividono
 * per scoprire il vostro IP, potete usare uno dei tanti siti disponibili su internet
 * //@see http://www.ilmioindirizzoip.it/
 * 
 * @author ma
 */
public class Server {
    
    //Variabili di classe - private(Solo la classe Server puo' vedere queste classi)
    private String nomeUtente;  //Nome dell' utente Server
    private int porta;          //porta del server

    /**
     * Costruttore 1 - Le variabili di classe avranno valori di default
     */
    public Server() {
        nomeUtente = "Server di Mario Rossi";
        porta = 6868;
    }

    /**
     * Costruttore N°2 - Il costruttore ha come parametri nomeUtente e porta
     * Se usiamo questa istanza possamo quindi cambiare alcune variabili di classe.
     * @param nomeUtente
     * @param porta 
     */
    public Server(String nomeUtente, int porta) {
        this.nomeUtente = nomeUtente;
        this.porta = porta;
    }
    
    public void init() throws IOException{
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
        ServerSocket sv = new ServerSocket(porta);
        
        print("Attendo connessione al client...");        
        Socket accept = sv.accept();    //Da qui il programma non si schioda finche non sentiamo 'bussare' un Client
        print(String.format("Connessione ad un client effettuata! [Client=%s]",sv.getLocalSocketAddress().toString()));

        //Se siamo qui, un Client si è connesso...
        //Creo istanze delle classi per flussi di spedizione-ricezione dati attraverso il socket
        DataOutputStream spedisci = new DataOutputStream(accept.getOutputStream());
        DataInputStream ricevi = new DataInputStream(accept.getInputStream());
        
        //Spediamo messaggio di benvenuto al Client...
        String ilServerDice = spedisciString("Benvenuto!");
        spedisci.writeUTF(ilServerDice);
        printEcho("Benvenuto!");
        
        /*
        Loop di spedizione messaggi verso Client.
        Si esce quando si riceve dal Client il messaggio "esco"
        */
        while(true){ 
            //Attendiamo messaggio da Client
            String readUTF = ricevi.readUTF();
            print(readUTF); //Print output del messaggio ricevuto dal Client
            //Qui c'e' un difetto volutamente introdotto dal vostro insegnante, vediamo chi lo scopre per primo...
            if(readUTF.toLowerCase().contains("esco")){ //Se il messaggio è "esco", terminiamo il Server uscendo dal loop
                break;
            }
            //Richiesta input di un messaggio da pedire al Client
            String readLine = JOptionPane.showInputDialog(null, "Messaggio da spedire al Client...","SERVER",JOptionPane.QUESTION_MESSAGE);
            ilServerDice = spedisciString(readLine);
            spedisci.writeUTF(ilServerDice);
            printEcho(readLine);
        }
        
        //Chiudiamo i canali dei sockets
        spedisci.close();
        ricevi.close();
        accept.close();
        sv.close();
    }
    
    /**
     * Aggiungiamo in testa al parametro st, il nomeUtente e la dicitura:" dice:";
     * @param st
     * @return 
     */
    private String spedisciString(String st){
        String stDiRitorno = String.format("[%s] dice:%s",nomeUtente,st);
        return stDiRitorno;
    }
    /**
     * Aggiungiamo in testa al parametro st la Stringa "Server:"
     * @param st 
     */
    private void print(String st){
        System.out.println("Server:"+st);
    }
    /**
     * Aggiungiamo in testa al parametro st una tabulazione(\t) per identificare meglio 
     * il messaggio spedito al Client.
     * @param st 
     */
    private void printEcho(String st){
        System.out.println("\t>>:"+st);
    }
    
    
    
    /**
     * Punto di start della classe Server
     * @param args 
     */
    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.init();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
