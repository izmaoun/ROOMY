CREATE TABLE clients (
                         id_client INT AUTO_INCREMENT PRIMARY KEY,
                         nom VARCHAR(100) NOT NULL,
                         prenom VARCHAR(100) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         telephone VARCHAR(20),
                         password VARCHAR(255) NOT NULL, -- hash BCrypt
                         date_inscription TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         est_bloque BOOLEAN DEFAULT FALSE

);

CREATE TABLE hoteliers (
                           id_hotelier INT AUTO_INCREMENT PRIMARY KEY,
                           nom_etablissement VARCHAR(100) NOT NULL,
                           nom_gerant VARCHAR(100) NOT NULL,
                           prenom_gerant VARCHAR(100) NOT NULL,
                           ville VARCHAR(100) NOT NULL,
                           email_gerant VARCHAR(255) NOT NULL UNIQUE,
                           telephone VARCHAR(20), -- Ajouté
                           password VARCHAR(255) NOT NULL,
                           ice VARCHAR(50) NOT NULL UNIQUE,
                           date_inscription TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           statut_verification ENUM('en_attente', 'verifie', 'rejete') DEFAULT 'en_attente'
);
CREATE TABLE administrateurs (
                             id_admin INT AUTO_INCREMENT PRIMARY KEY,
                             username VARCHAR(100) UNIQUE NOT NULL,
                             password VARCHAR(255) NOT NULL,
                             email VARCHAR(255) NOT NULL UNIQUE
);
