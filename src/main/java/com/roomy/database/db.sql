CREATE TABLE clients (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         username VARCHAR(100) NOT NULL,
                        -- prenom VARCHAR(100) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         telephone VARCHAR(20),
                         password VARCHAR(255) NOT NULL -- hash BCrypt
);

CREATE TABLE hoteliers (
                           id INT AUTO_INCREMENT PRIMARY KEY,
    -- Informations de l'hôtel
                           username VARCHAR(255) NOT NULL,
                           ville VARCHAR(100) NOT NULL,
                           email VARCHAR(255) NOT NULL UNIQUE,
                           password VARCHAR(255) NOT NULL,
                           ice VARCHAR(50) NOT NULL UNIQUE
);
CREATE TABLE administrateurs (
                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                 username VARCHAR(100) UNIQUE NOT NULL,
                                 password VARCHAR(255) NOT NULL
);
