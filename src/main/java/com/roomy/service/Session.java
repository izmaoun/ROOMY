package com.roomy.service;

import com.roomy.entities.Client;
import com.roomy.entities.Hotelier;
import com.roomy.entities.Admin;

public class Session {
    private static Client currentClient;
    private static Hotelier currentHotelier;
    private static Admin currentAdmin;

    public static void setCurrentClient(Client client) {
        currentClient = client;
    }

    public static Client getCurrentClient() {
        return currentClient;
    }

    public static void setCurrentHotelier(Hotelier hotelier) {
        currentHotelier = hotelier;
    }

    public static Hotelier getCurrentHotelier() {
        return currentHotelier;
    }

    public static void setCurrentAdmin(Admin admin) {
        currentAdmin = admin;
    }

    public static Admin getCurrentAdmin() {
        return currentAdmin;
    }

    public static boolean isLoggedIn() {
        return currentClient != null || currentHotelier != null || currentAdmin != null;
    }

    public static void logout() {
        currentClient = null;
        currentHotelier = null;
        currentAdmin = null;
    }
}