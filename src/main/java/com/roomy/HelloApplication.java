package com.roomy;

public class HelloApplication {
    public static void main(String[] args) {
        // Délègue au HelloApplication réel situé dans com.roomy.service
        // a
        com.roomy.service.HelloApplication.main(args);
    }
}

// aaaa