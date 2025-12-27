# Flux de Réservation ROOMY

## Fonctionnalités Implémentées

### FLUX 1 : Utilisateur NON connecté
1. **Clic "Réserver"** → Redirige vers page inscription
2. **Crée compte** → Redirige AUTO vers formulaire réservation pour CET hôtel
3. **Remplit formulaire** → Réservation créée

### FLUX 2 : Utilisateur DÉJÀ connecté
1. **Clic "Réserver"** → Formulaire réservation DIRECT
2. **Remplit** → Réservation liée à son compte

## Fichiers Créés/Modifiés

### Nouveaux Fichiers
- `ReservationDAO.java` - Gestion des réservations en base
- `BookingController.java` - Contrôleur du formulaire de réservation
- `BookingContext.java` - Contexte de réservation pour redirection auto
- `booking_form.fxml` - Interface du formulaire de réservation
- `reservations_table.sql` - Script de création de la table
- `BookingFlowTest.java` - Tests des flux

### Fichiers Modifiés
- `HotelDetailsController.java` - Ajout de la logique des flux
- `SignupController.java` - Redirection automatique après inscription
- `Chambre.java` - Ajout des getters/setters manquants

## Utilisation

### Pour tester FLUX 1 (Non connecté)
1. Ouvrir la page des détails d'un hôtel
2. Cliquer sur "Réserver" (sans être connecté)
3. Remplir le formulaire d'inscription
4. Après inscription → Redirection automatique vers réservation

### Pour tester FLUX 2 (Connecté)
1. Se connecter d'abord
2. Ouvrir la page des détails d'un hôtel
3. Cliquer sur "Réserver"
4. Accès direct au formulaire de réservation

## Base de Données

Exécuter le script `reservations_table.sql` pour créer la table des réservations :

```sql
CREATE TABLE reservations (
    id_reservation INT AUTO_INCREMENT PRIMARY KEY,
    id_client INT NOT NULL,
    id_chambre INT NOT NULL,
    date_reservation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_debut_sejour DATETIME NOT NULL,
    date_fin_sejour DATETIME NOT NULL,
    nombre_personnes INT NOT NULL,
    montant_total DECIMAL(10,2) NOT NULL,
    statut ENUM('EN_ATTENTE', 'CONFIRMEE', 'ANNULEE', 'TERMINEE') DEFAULT 'EN_ATTENTE',
    FOREIGN KEY (id_client) REFERENCES clients(id_client),
    FOREIGN KEY (id_chambre) REFERENCES chambres(id_chambre)
);
```

## Architecture

### Session Management
- `Session.java` - Gestion de l'état de connexion
- `BookingContext.java` - Stockage temporaire du contexte de réservation

### Flux de Données
1. **Sélection hôtel/chambre** → Stockage dans BookingContext
2. **Vérification connexion** → Redirection selon l'état
3. **Inscription** → Auto-connexion + redirection vers réservation
4. **Réservation** → Création en base + nettoyage du contexte

## Points Clés

- **Redirection automatique** après inscription vers la réservation en cours
- **Gestion d'état** avec BookingContext pour maintenir le contexte
- **Validation complète** des données de réservation
- **Interface utilisateur** intuitive avec feedback visuel
- **Sécurité** : vérification de connexion avant réservation