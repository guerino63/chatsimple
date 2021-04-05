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
public class Server {
    
    String nomeUtente;  //Nome dell' utente Server
    int porta;          //porta del server

    /**
     * Costruttore 1 - variabili di classe di default
     */
    public Server() {
        nomeUtente = "Mario Rossi";
        porta = 6868;
    }

    /**
     * Costruttore N°2 - 
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
        ServerSocket sv = new ServerSocket(porta);
        
        print("Attendo connessione al client...");        
        Socket accept = sv.accept();
        print(String.format("Connessione ad un client effettuata! [Client=%s]",sv.getLocalSocketAddress().toString()));

        //Istanze delle classe per flussi di spedizione-ricezione dati attraverso il socket
        DataOutputStream spedisci = new DataOutputStream(accept.getOutputStream());
        DataInputStream ricevi = new DataInputStream(accept.getInputStream());
        
        //Spedisci messaggio di benvenuto al Client...
        String ilServerDice = spedisciString("Benvenuto!");
        spedisci.writeUTF(ilServerDice);
        printEcho("Benvenuto!");
        while(true){
            //Attendiamo messaggio da Client
            String readUTF = ricevi.readUTF();
            print(readUTF); //Print output del messaggio ricevuto dal Client
            if(readUTF.toLowerCase().contains("esci")){ //Se il messaggio è "esci", terminiamo il Server uscendo dal loop
                break;
            }
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
    private String spedisciString(String st){
        String stDiRitorno = String.format("[%s] dice:%s",nomeUtente,st);
        return stDiRitorno;
    }
    private void print(String st){
        System.out.println("Server:"+st);
    }
    private void printEcho(String st){
        System.out.println("\t>>:"+st);
    }
    
    
    
    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.init();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
