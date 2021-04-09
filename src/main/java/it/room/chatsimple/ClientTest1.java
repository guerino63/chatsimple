package it.room.chatsimple;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ma
 */
public class ClientTest1 extends Client{

    public ClientTest1() {
        super();
        nomeUtente = "Utente Client-1";
    }
    
    public static void main(String[] args) {
        ClientTest1 clientTest1 = new ClientTest1();
        try {
            clientTest1.init();
        } catch (IOException ex) {
            Logger.getLogger(ClientTest1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
