-- Script SQL pour créer la table des réservations
-- À exécuter si la table n'existe pas déjà

CREATE TABLE IF NOT EXISTS reservations (
    id_reservation INT AUTO_INCREMENT PRIMARY KEY,
    id_client INT NOT NULL,
    id_chambre INT NOT NULL,
    date_reservation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_debut_sejour DATETIME NOT NULL,
    date_fin_sejour DATETIME NOT NULL,
    nombre_personnes INT NOT NULL,
    montant_total DECIMAL(10,2) NOT NULL,
    statut ENUM('EN_ATTENTE', 'CONFIRMEE', 'ANNULEE', 'TERMINEE') DEFAULT 'EN_ATTENTE',
    
    FOREIGN KEY (id_client) REFERENCES clients(id_client) ON DELETE CASCADE,
    FOREIGN KEY (id_chambre) REFERENCES chambres(id_chambre) ON DELETE CASCADE,
    
    INDEX idx_client (id_client),
    INDEX idx_chambre (id_chambre),
    INDEX idx_dates (date_debut_sejour, date_fin_sejour),
    INDEX idx_statut (statut)
);