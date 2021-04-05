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

    public Client() {
        nomeUtente = "Luisa Bianchi";
        ip = "127.0.0.1";           //localhost
        porta = 6868;
    }

    public Client(String nomeUtente, String ip, int porta) {
        this.nomeUtente = nomeUtente;
        this.ip = ip;
        this.porta = porta;
    }
    
    public void init() throws IOException{
        
        print("**********************************");
        print("**         C L I E N T          **");
        print("**********************************");
        /*
        Cerchiamo un server attivo. Il programma rimane in attesa finche' non troviamo
        un Server disponibile all' indirizzo ip:porta
         */
        print("Provo a connettermi con un server all' indirizzo: " + ip + ":" + porta);
        Socket clientSocket = new Socket(ip, porta);    //Proviamo a cercare un Server attivo
        print("Connessione al server effettuata!");
        
        //Istanze delle classe per flussi di spedizione-ricezione dati attraverso il socket
        DataOutputStream spedisci = new DataOutputStream(clientSocket.getOutputStream());
        DataInputStream ricevi = new DataInputStream(clientSocket.getInputStream());

        while(true){
            //Attendiamo messaggio da Server
            String readUTF = ricevi.readUTF();
            print(readUTF); //Print output del messaggio ricevuto dal Server
            String readLine = JOptionPane.showInputDialog(null, "Messaggio da spedire al Server...","CLIENT",JOptionPane.QUESTION_MESSAGE);
            String ilClientDice = spedisciString(readLine);
            spedisci.writeUTF(ilClientDice);
            printEcho(readLine);
            if(readLine.toLowerCase().contains("esci")){ //Se il messaggio e' "esci", terminiamo il Server uscendo dal loop
                break;
            }
            
        }
        
        //Chiudi i flussi
        spedisci.close();
        ricevi.close();
        clientSocket.close();
    }
    

    private String spedisciString(String st){
        String stDiRitorno = String.format("[%s] dice:%s",nomeUtente,st);
        return stDiRitorno;
    }
    private void print(String st){
        System.out.println("Client:"+st);
    }
    private void printEcho(String st){
        System.out.println("\t>>:"+st);
    }    
    
    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.init();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
