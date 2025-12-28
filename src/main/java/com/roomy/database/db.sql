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

CREATE TABLE adresses (
                          id_adresse INT AUTO_INCREMENT PRIMARY KEY,
                          rue VARCHAR(255),
                          ville VARCHAR(100),
                          codepostal VARCHAR(20),
                          pays VARCHAR(50)
);

CREATE TABLE hotels (
                        id_hotel INT AUTO_INCREMENT PRIMARY KEY,
                        nom_hotel VARCHAR(100) NOT NULL,
                        id_adresse INT,
                        etoiles INT,
                        id_hotelier INT,
                        FOREIGN KEY (id_adresse) REFERENCES adresses(id_adresse),
                        FOREIGN KEY (id_hotelier) REFERENCES hoteliers(id_hotelier)
);

CREATE TABLE chambres (
                          id_chambre INT AUTO_INCREMENT PRIMARY KEY,
                          num_chambre INT NOT NULL,
                          type ENUM('Simple', 'Double', 'Suite', 'Familiale') NOT NULL,
                          prix_nuit DECIMAL(10,2) NOT NULL,
                          capacity INT NOT NULL,
                          surface INT,
                          statut ENUM('disponible', 'en_maintenance', 'hors_service', 'en_netoyage', 'occupee') DEFAULT 'disponible',
                          description TEXT,
                          id_hotel INT NOT NULL,
                          FOREIGN KEY (id_hotel) REFERENCES hotels(id_hotel)
);

CREATE TABLE hotel_images (
                              id_image INT AUTO_INCREMENT PRIMARY KEY,
                              id_hotel INT NOT NULL,
                              url VARCHAR(500) NOT NULL,
                              description VARCHAR(255),
                              FOREIGN KEY (id_hotel) REFERENCES hotels(id_hotel)
                                  ON DELETE CASCADE
);

CREATE TABLE chambre_images (
                                id_image INT AUTO_INCREMENT PRIMARY KEY,
                                id_chambre INT NOT NULL,
                                url VARCHAR(500) NOT NULL,
                                description VARCHAR(255),
                                FOREIGN KEY (id_chambre) REFERENCES chambres(id_chambre)
                                    ON DELETE CASCADE
);

CREATE TABLE reservations (
    id_reservation INT AUTO_INCREMENT PRIMARY KEY,
    id_client INT NOT NULL,
    id_chambre INT,
    date_reservation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_debut_sejour DATETIME NOT NULL,
    date_fin_sejour DATETIME NOT NULL,
    nombre_personnes INT NOT NULL,
    montant_total DECIMAL(10,2) NOT NULL,
    statut ENUM('CONFIRMEE','ANNULEE','EN_ATTENTE','TERMINEE') DEFAULT 'EN_ATTENTE',
    INDEX idx_reservations_client (id_client),
    INDEX idx_reservations_chambre (id_chambre),
    CONSTRAINT fk_reservations_client FOREIGN KEY (id_client) REFERENCES clients(id_client) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_reservations_chambre FOREIGN KEY (id_chambre) REFERENCES chambres(id_chambre) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
