package it.room.chatsimple;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ma
 */
public class ClientTest2 extends Client{

    public ClientTest2() {
        super("Utente Client-2");
    }
    
    public static void main(String[] args) {
        ClientTest2 clientTest2 = new ClientTest2();
        try {
            clientTest2.init();
        } catch (IOException ex) {
            Logger.getLogger(ClientTest2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
