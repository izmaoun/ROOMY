package com.roomy.database;

import com.roomy.entities.Admin;
import com.roomy.entities.Hotelier;
import com.roomy.Dao.AdminDAO;
import com.roomy.Dao.HotelierDAO;
import org.mindrot.jbcrypt.BCrypt;

public class DataInit {
    public static void insertDefaultAdminAndHotelier() {
        AdminDAO adminDAO = new AdminDAO();
        HotelierDAO hotelierDAO = new HotelierDAO();

        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword(BCrypt.hashpw("adminpass", BCrypt.gensalt()));
        adminDAO.signup(admin);

        Hotelier hotelier = new Hotelier();
        hotelier.setUsername("hotelier");
        hotelier.setPassword(BCrypt.hashpw("hotelierpass", BCrypt.gensalt()));
        hotelierDAO.signup(hotelier);
    }

    public static void main(String[] args) {
        insertDefaultAdminAndHotelier();
    }
}
