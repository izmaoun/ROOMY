package com.roomy.service;
import com.roomy.Dao.ClientDAO;
import com.roomy.Dao.HotelierDAO;
import com.roomy.Dao.AdminDAO;
import com.roomy.entities.Client;
import com.roomy.entities.Hotelier;
import com.roomy.entities.Admin;


import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private final ClientDAO clientDAO = new ClientDAO();
    private final HotelierDAO hotelierDAO = new HotelierDAO();
    private final AdminDAO adminDAO = new AdminDAO();

    public String authenticate(String username, String password) {

        // Check Client
        Client cl = clientDAO.findByEmail(username);
        if (cl != null && BCrypt.checkpw(password, cl.getPassword())) {
            return "CLIENT";
        }

        // Check Hotelier
        Hotelier h = hotelierDAO.findByEmail(username);
        if (h != null && BCrypt.checkpw(password, h.getPassword())) {
            return "HOTELIER";
        }

        // Check Admin
        Admin a = adminDAO.find(username);
        if (a != null && BCrypt.checkpw(password, a.getPassword())) {
            return "ADMIN";
        }

        return "INVALID";
    }
}
