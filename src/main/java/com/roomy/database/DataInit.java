package com.roomy.database;

import com.roomy.Dao.ClientDAO;
import com.roomy.Dao.HotelierDAO;
import com.roomy.entities.Client;
import com.roomy.entities.Hotelier;
import org.mindrot.jbcrypt.BCrypt;

public class DataInit {

    private static final ClientDAO clientDAO = new ClientDAO();
    private static final HotelierDAO hotelierDAO = new HotelierDAO();

    public static void init() {
        // Client de test
        if (clientDAO.findByEmail("admin@roomy.com") == null) {
            Client adminClient = new Client();
            adminClient.setNom("Admin");
//            adminClient.setPrenom("Client");
            adminClient.setEmail("admin@roomy.com");
            adminClient.setTelephone("0600000000");
            adminClient.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
            clientDAO.signup(adminClient);
            System.out.println("Client admin créé : admin@roomy.com / 123456");
        }

        // Hôtelier de test
        if (hotelierDAO.findByEmail("hotel@roomy.com") == null) {
            Hotelier hotelier = new Hotelier();
            hotelier.setNom("Hôtel Casablanca Palace");     // nom de l'hôtel
            hotelier.setVille("Casablanca");
            hotelier.setEmail("hotel@roomy.com");
            hotelier.setPassword(BCrypt.hashpw("hotel123", BCrypt.gensalt()));
            hotelier.setIce("A123BC456");

            hotelierDAO.signup(hotelier);
            System.out.println("Hôtelier créé : hotel@roomy.com / hotel123");
        }
    }
}