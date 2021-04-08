package it.room.chatsimple;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * to run: prima di inserire MANIFEST
 * Maven:
 * java -cp target/chatsimple-1.0-SNAPSHOT.jar it.room.chatsimple.Inizio
 *
 * to run: dopo aver inserito il MANIFEST
 * Maven:
 * java -jar chatsimple-1.0-SNAPSHOT.jar
 * 
 * @author ma
 */
public class Inizio {

    public static void main(String[] args) {
        Object[] selectionValues = {"Server", "Client"};
        Object initialSelection = selectionValues[0];
        Object selection = JOptionPane.showInputDialog(null, "Scegli Server o Client.\nSe il Server non è attivo l' esecuzione di Client genererà un errore.",
                "Scelta esecuzione Server o Client", JOptionPane.QUESTION_MESSAGE, null, selectionValues, initialSelection);
        
        //Se viene premuto il tasto Annulla o si chiude la finestra con l' icona X
        if(selection==null){
            System.err.println("Esco...");
            System.exit(0);
        }
                
        //istanzio la classe scelta e la eseguo attraverso il suo metodo init();
        //Riporto in commento la possibilità di istanziare le classi direttamente da metodo statico main()...
        if(((String)selection).compareTo((String)selectionValues[0]) == 0){
//            Server.main(null);
            Server server = new Server("Luisa",6666);   //Server Luisa su porta 6666
            try {
                server.init();
            } catch (IOException ex) {
                Logger.getLogger(Inizio.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(-1); //errore di inizializzazione del server, usciamo dal programma con -1
            }
        } else {
//            Client.main(null);
            Client client = new Client("Mariano", "127.0.0.1", 6666); //Client Mariano su localhost:6666
            try {
                client.init();
            } catch (IOException ex) {
                Logger.getLogger(Inizio.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(-2); //errore di inizializzazione del server, usciamo dal programma con -2                
            }
        }        
    }
}
