// java
package com.roomy.database;

import com.roomy.Dao.AdminDAO;
import com.roomy.entities.Admin;
import org.mindrot.jbcrypt.BCrypt;

public class DataInit {

    private static final AdminDAO adminDAO = new AdminDAO();

    public static void init() {
        String adminEmail = "admin1@roomy.com";
        // adapte le check selon la méthode disponible dans AdminDAO (findByEmail / findByUsername)
        if (adminDAO.findByEmail(adminEmail) == null) {
            Admin admin = new Admin();
            admin.setUsername("admin1");
            admin.setEmail(adminEmail);
            admin.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
            // utiliser la méthode create (alias de signup) disponible dans AdminDAO
            adminDAO.create(admin);
            System.out.println("Admin créé : " + adminEmail + " / 123456");
        } else {
            System.out.println("Admin déjà présent : " + adminEmail);
        }
    }

    public static void main(String[] args) {
        init();
    }
}
