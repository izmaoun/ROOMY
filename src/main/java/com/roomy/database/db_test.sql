
-- 1. D'abord, insérer un hotelier (nécessaire pour la clé étrangère)
INSERT INTO hoteliers (nom_etablissement, nom_gerant, prenom_gerant, ville, email_gerant, telephone, password, ice, statut_verification)
VALUES
('LuxeStay Hotels', 'Dupont', 'Jean', 'Paris', 'jean@luxestay.com', '0612345678', '$2a$10$hashedpassword', 'ICE123456789', 'verifie'),
('Beach Resorts', 'Martin', 'Sophie', 'Marseille', 'sophie@beachresorts.com', '0623456789', '$2a$10$hashedpassword', 'ICE987654321', 'verifie'),
('Alpine Retreats', 'Bernard', 'Luc', 'Lyon', 'luc@alpineretreats.com', '0634567890', '$2a$10$hashedpassword', 'ICE456789123', 'verifie');

-- 2. Insérer des adresses
INSERT INTO adresses (rue, ville, codepostal, pays) VALUES
-- Paris
('123 Avenue des Champs-Élysées', 'Paris', '75008', 'France'),
('56 Rue de Rivoli', 'Paris', '75004', 'France'),
('789 Boulevard Saint-Germain', 'Paris', '75006', 'France'),
-- Lyon
('45 Rue du Commerce', 'Lyon', '69002', 'France'),
('12 Place Bellecour', 'Lyon', '69002', 'France'),
-- Marseille
('78 Boulevard de la Mer', 'Marseille', '13008', 'France'),
('34 Corniche Kennedy', 'Marseille', '13007', 'France'),
-- Autres
('Calle Gran Vía 1', 'Madrid', '28013', 'Espagne'),
('Via Condotti 10', 'Rome', '00187', 'Italie');

-- 3. Insérer des hôtels
INSERT INTO hotels (nom_hotel, id_adresse, etoiles, id_hotelier) VALUES
-- Hôtels Paris (5 étoiles)
('Hôtel Plaza Paris', 1, 5, 1),
('Le Royal Palace', 2, 5, 1),
('Grand Hôtel Saint-Germain', 3, 4, 1),
-- Hôtels Lyon
('Hôtel Central Lyon', 4, 3, 3),
('Bellecour Suites', 5, 4, 3),
-- Hôtels Marseille
('Résidence Beach Marseille', 6, 4, 2),
('Hôtel Corniche Luxury', 7, 5, 2),
-- Hôtels internationaux
('Madrid Royal Suites', 8, 4, 1),
('Rome Luxury Hotel', 9, 5, 1);

-- 4. Insérer des services pour les hôtels
INSERT INTO hotel_services (id_hotel, service_name) VALUES
-- Hôtel 1 (Plaza Paris)
(1, 'Wi-Fi'), (1, 'Swimming Pool'), (1, 'Spa'), (1, 'Restaurant'), (1, 'Room Service'),
(1, 'Concierge'), (1, 'Parking'), (1, 'Business Center'),
-- Hôtel 2 (Royal Palace)
(2, 'Wi-Fi'), (2, 'Spa'), (2, 'Restaurant'), (2, 'Bar'), (2, 'Fitness Center'),
-- Hôtel 3 (Saint-Germain)
(3, 'Wi-Fi'), (3, 'Breakfast'), (3, 'Parking'), (3, 'Terrace'),
-- Hôtel 4 (Central Lyon)
(4, 'Wi-Fi'), (4, 'Parking'), (4, 'Breakfast'),
-- Hôtel 5 (Bellecour Suites)
(5, 'Wi-Fi'), (5, 'Swimming Pool'), (5, 'Spa'), (5, 'Restaurant'),
-- Hôtel 6 (Beach Marseille)
(6, 'Wi-Fi'), (6, 'Swimming Pool'), (6, 'Beach Access'), (6, 'Restaurant'), (6, 'Water Sports'),
-- Hôtel 7 (Corniche Luxury)
(7, 'Wi-Fi'), (7, 'Infinity Pool'), (7, 'Spa'), (7, 'Fine Dining'), (7, 'Helipad'),
-- Hôtel 8 (Madrid)
(8, 'Wi-Fi'), (8, 'Swimming Pool'), (8, 'Tapas Bar'), (8, 'Flamenco Shows'),
-- Hôtel 9 (Rome)
(9, 'Wi-Fi'), (9, 'Historic Tours'), (9, 'Roman Spa'), (9, 'Rooftop Restaurant');

-- 5. Insérer des images d'hôtels (URLs Unsplash)
INSERT INTO hotel_images (id_hotel, url, description) VALUES
-- Plaza Paris
(1, 'https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=600', 'Vue extérieure Hôtel Plaza Paris'),
(1, 'https://images.unsplash.com/photo-1611892440504-42a792e24d32?w=600', 'Lobby luxueux'),
-- Royal Palace
(2, 'https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=600', 'Façade du Royal Palace'),
-- Saint-Germain
(3, 'https://images.unsplash.com/photo-1590490360182-c33d57733427?w=600', 'Chambre double'),
-- Central Lyon
(4, 'https://images.unsplash.com/photo-1566073771259-6a8506099945?w=600', 'Hôtel Central Lyon'),
-- Bellecour Suites
(5, 'https://images.unsplash.com/photo-1586375300773-8384e3e4916f?w=600', 'Suite avec vue'),
-- Beach Marseille
(6, 'https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=600', 'Piscine face à la mer'),
-- Corniche Luxury
(7, 'https://images.unsplash.com/photo-1542314831-068cd1dbfeeb?w=600', 'Infinity pool'),
-- Madrid
(8, 'https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=600', 'Terrasse Madrid'),
-- Rome
(9, 'https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=600', 'Vue sur Rome');

-- 6. Insérer des chambres
INSERT INTO chambres (num_chambre, type, prix_nuit, capacity, surface, statut, description, id_hotel) VALUES
-- Hôtel 1 (Plaza Paris)
(101, 'Suite', 450.00, 2, 60, 'disponible', 'Suite Deluxe avec vue Tour Eiffel', 1),
(102, 'Double', 280.00, 2, 35, 'disponible', 'Chambre Double Supérieure', 1),
(103, 'Suite', 650.00, 4, 80, 'disponible', 'Suite Présidentielle', 1),
-- Hôtel 2 (Royal Palace)
(201, 'Double', 320.00, 2, 30, 'disponible', 'Chambre Deluxe', 2),
(202, 'Simple', 190.00, 1, 25, 'disponible', 'Chambre Simple', 2),
-- Hôtel 3 (Saint-Germain)
(301, 'Double', 220.00, 2, 32, 'disponible', 'Chambre Standard', 3),
(302, 'Familiale', 350.00, 4, 50, 'disponible', 'Suite Familiale', 3),
-- Hôtel 4 (Central Lyon)
(401, 'Double', 89.00, 2, 25, 'disponible', 'Chambre Économique', 4),
-- Hôtel 5 (Bellecour Suites)
(501, 'Suite', 180.00, 2, 40, 'disponible', 'Suite Junior', 5),
-- Hôtel 6 (Beach Marseille)
(601, 'Double', 120.00, 2, 30, 'disponible', 'Chambre avec vue mer', 6),
(602, 'Familiale', 200.00, 4, 55, 'disponible', 'Bungalow Familial', 6),
-- Hôtel 7 (Corniche Luxury)
(701, 'Suite', 550.00, 2, 65, 'disponible', 'Suite Corniche', 7),
-- Hôtel 8 (Madrid)
(801, 'Double', 150.00, 2, 28, 'disponible', 'Chambre Double', 8),
-- Hôtel 9 (Rome)
(901, 'Suite', 480.00, 2, 55, 'disponible', 'Suite Romaine', 9);

-- 7. Insérer des images de chambres (optionnel)
INSERT INTO chambre_images (id_chambre, url, description) VALUES
(1, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=400', 'Suite Deluxe'),
(2, 'https://images.unsplash.com/photo-1586105251261-72a756497a11?w=400', 'Chambre Double'),
(3, 'https://images.unsplash.com/photo-1566665797739-1674de7a421a?w=400', 'Suite Présidentielle'),
(4, 'https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=400', 'Chambre Deluxe'),
(5, 'https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?w=400', 'Chambre Simple');

-- 8. Optionnel: Insérer un client de test pour les réservations
INSERT INTO clients (nom, prenom, email, telephone, password) VALUES
('Test', 'User', 'test@user.com', '0600000000', '$2a$10$hashedpassword');

-- 9. Optionnel: Insérer un admin de test
INSERT INTO administrateurs (username, password, email) VALUES
('admin', '$2a$10$hashedpassword', 'admin@roomy.com');


-- ============================================
-- DONNÉES SUPPLEMENTAIRES POUR TESTS DE FILTRES
-- ============================================

-- 1. AJOUTER PLUS D'HOTELIERS (si besoin)
INSERT INTO hoteliers (nom_etablissement, nom_gerant, prenom_gerant, ville, email_gerant, telephone, password, ice, statut_verification)
VALUES
('Alpine Luxury Group', 'Schmidt', 'Hans', 'Zurich', 'hans@alpine.com', '+41123456789', '$2a$10$...', 'CH123456789', 'verifie'),
('Mediterranean Resorts', 'Garcia', 'Maria', 'Barcelone', 'maria@medresorts.com', '+34912345678', '$2a$10$...', 'ES987654321', 'verifie'),
('Asian Hospitality', 'Tanaka', 'Kenji', 'Tokyo', 'kenji@asianhotels.com', '+81312345678', '$2a$10$...', 'JP123456789', 'verifie');

-- 2. AJOUTER PLUS D'ADRESSES
INSERT INTO adresses (rue, ville, codepostal, pays) VALUES
-- Varier les pays et villes
('Bahnhofstrasse 10', 'Zurich', '8001', 'Suisse'),
('Passeig de Gràcia 38', 'Barcelone', '08007', 'Espagne'),
('Ginza 5-5-5', 'Tokyo', '104-0061', 'Japon'),
('Kurfürstendamm 101', 'Berlin', '10711', 'Allemagne'),
('Via della Spiga 1', 'Milan', '20121', 'Italie'),
('New Bond Street 25', 'Londres', 'W1S 2RL', 'Royaume-Uni'),
('5th Avenue 500', 'New York', '10018', 'États-Unis'),
('Jumeirah Beach Road', 'Dubai', '00000', 'Émirats Arabes Unis'),
('Orchard Road 333', 'Singapour', '238867', 'Singapour'),
('Shibuya Crossing 2-1', 'Tokyo', '150-0002', 'Japon'),
('Myeongdong-gil 66', 'Séoul', '04536', 'Corée du Sud'),
('Colaba Causeway 7', 'Mumbai', '400005', 'Inde');

-- 3. AJOUTER PLUS D'HÔTELS AVEC VARIÉTÉ DE PRIX ET ÉTOILES
INSERT INTO hotels (nom_hotel, id_adresse, etoiles, id_hotelier) VALUES
-- Hôtels économiques (1-2 étoiles)
('Zurich Budget Hotel', 10, 2, 4),
('Berlin Backpackers', 12, 1, 1),
('Tokyo Capsule Hotel', 13, 1, 5),
-- Hôtels milieu de gamme (3 étoiles)
('Barcelona City Hotel', 11, 3, 4),
('Milan Design Hotel', 13, 3, 1),
('London Business Hotel', 14, 3, 1),
-- Hôtels haut de gamme (4-5 étoiles)
('New York Luxury Towers', 15, 5, 2),
('Dubai Palace Hotel', 16, 5, 3),
('Singapore Marina Suites', 17, 4, 5),
('Tokyo Skyline Hotel', 18, 4, 5),
('Seoul Heritage Inn', 19, 4, 4),
('Mumbai Royal Palace', 20, 5, 3);

-- 4. AJOUTER DES SERVICES VARIÉS POUR TESTER LES FILTRES
INSERT INTO hotel_services (id_hotel, service_name) VALUES
-- Pour hôtels économiques - services basiques
(10, 'Wi-Fi'), (10, 'Breakfast'),
(11, 'Wi-Fi'), (11, 'Laundry'), (11, 'Shared Kitchen'),
(12, 'Wi-Fi'), (12, '24/7 Reception'),
-- Pour hôtels milieu de gamme
(13, 'Wi-Fi'), (13, 'Breakfast'), (13, 'Parking'), (13, 'Gym'),
(14, 'Wi-Fi'), (14, 'Restaurant'), (14, 'Bar'), (14, 'Business Center'),
(15, 'Wi-Fi'), (15, 'Breakfast'), (15, 'Conference Rooms'),
-- Pour hôtels de luxe - tous les services
(16, 'Wi-Fi'), (16, 'Spa'), (16, 'Swimming Pool'), (16, 'Restaurant'),
(16, 'Bar'), (16, 'Gym'), (16, 'Concierge'), (16, 'Valet Parking'),
(16, 'Room Service 24/7'), (16, 'Helipad'), (16, 'Limo Service'),
-- Dubai Palace - ultra luxe
(17, 'Wi-Fi'), (17, 'Private Beach'), (17, 'Butler Service'), (17, 'Private Pool'),
(17, 'Yacht Rental'), (17, 'Helicopter Tours'), (17, 'Personal Chef'),
-- Singapore - services business
(18, 'Wi-Fi'), (18, 'Business Center'), (18, 'Conference Rooms'),
(18, 'Gym'), (18, 'Restaurant'), (18, 'Airport Shuttle'),
-- Tokyo - services technologiques
(19, 'Wi-Fi High Speed'), (19, 'Robot Butler'), (19, 'Smart Rooms'),
(19, 'VR Entertainment'), (19, 'Gaming Console'),
-- Séoul - services culturels
(20, 'Wi-Fi'), (20, 'Korean Spa'), (20, 'Traditional Tea Ceremony'),
(20, 'K-Pop Dance Classes'), (20, 'Korean Cooking Classes'),
-- Mumbai - services royaux
(21, 'Wi-Fi'), (21, 'Ayurvedic Spa'), (21, 'Elephant Rides'),
(21, 'Bollywood Dance Shows'), (21, 'Private Yoga Instructor');

-- 5. AJOUTER DES CHAMBRES AVEC GRANDE VARIÉTÉ DE PRIX
INSERT INTO chambres (num_chambre, type, prix_nuit, capacity, surface, statut, description, id_hotel) VALUES
-- Hôtels économiques (prix bas)
(1001, 'Simple', 45.00, 1, 15, 'disponible', 'Chambre simple basique', 10),
(1002, 'Simple', 50.00, 1, 16, 'disponible', 'Chambre avec petit bureau', 10),
(1101, 'Simple', 35.00, 1, 12, 'disponible', 'Lit en capsule', 12),
(1102, 'Simple', 40.00, 1, 14, 'disponible', 'Capsule deluxe', 12),
-- Hôtels milieu de gamme (prix moyens)
(1301, 'Double', 89.00, 2, 25, 'disponible', 'Chambre double standard', 13),
(1302, 'Double', 110.00, 2, 28, 'disponible', 'Chambre double vue ville', 13),
(1401, 'Double', 95.00, 2, 26, 'disponible', 'Chambre design moderne', 14),
(1402, 'Suite', 150.00, 2, 40, 'disponible', 'Suite junior design', 14),
-- Hôtels de luxe (prix élevés)
(1601, 'Suite', 450.00, 2, 60, 'disponible', 'Suite vue Central Park', 16),
(1602, 'Suite', 650.00, 2, 80, 'disponible', 'Penthouse Suite', 16),
(1603, 'Suite', 850.00, 4, 100, 'disponible', 'Royal Suite', 16),
-- Dubai Palace (prix très élevés)
(1701, 'Suite', 1200.00, 2, 90, 'disponible', 'Suite Burj View', 17),
(1702, 'Suite', 2500.00, 4, 150, 'disponible', 'Presidential Suite', 17),
(1703, 'Suite', 5000.00, 6, 250, 'disponible', 'Royal Palace Suite', 17),
-- Singapore (prix business)
(1801, 'Double', 180.00, 2, 30, 'disponible', 'Executive Room', 18),
(1802, 'Suite', 280.00, 2, 50, 'disponible', 'Marina Bay Suite', 18),
-- Tokyo (prix technologie)
(1901, 'Double', 120.00, 2, 25, 'disponible', 'Smart Room', 19),
(1902, 'Suite', 220.00, 2, 45, 'disponible', 'Tech Suite', 19),
-- Séoul (prix culture)
(2001, 'Double', 100.00, 2, 28, 'disponible', 'Hanok Style Room', 20),
(2002, 'Suite', 180.00, 4, 55, 'disponible', 'Traditional Suite', 20),
-- Mumbai (prix royal)
(2101, 'Suite', 300.00, 2, 65, 'disponible', 'Maharaja Suite', 21),
(2102, 'Suite', 450.00, 4, 85, 'disponible', 'Royal Palace Room', 21);

-- 6. AJOUTER DES IMAGES POUR LES NOUVEAUX HÔTELS
INSERT INTO hotel_images (id_hotel, url, description) VALUES
-- Zurich Budget
(10, 'https://images.unsplash.com/photo-1555854877-bab0e564b8d5?w=600', 'Chambre économique Zurich'),
-- Berlin Backpackers
(11, 'https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=600', 'Auberge de jeunesse Berlin'),
-- Tokyo Capsule
(12, 'https://images.unsplash.com/photo-1582719508461-905c673771fd?w=600', 'Capsule hotel Tokyo'),
-- Barcelona
(13, 'https://images.unsplash.com/photo-1551882547-ff40c63fe5fa?w=600', 'Hôtel Barcelone'),
-- New York Luxury
(16, 'https://images.unsplash.com/photo-1564501049412-61c2a3083791?w=600', 'Vue skyline New York'),
-- Dubai Palace
(17, 'https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=600', 'Piscine infinity Dubai'),
-- Singapore
(18, 'https://images.unsplash.com/photo-1566073771259-6a8506099945?w=600', 'Marina Bay Suites'),
-- Tokyo Skyline
(19, 'https://images.unsplash.com/photo-1540959733332-eab4deabeeaf?w=600', 'Vue sur Tokyo'),
-- Seoul
(20, 'https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=600', 'Chambre traditionnelle coréenne'),
-- Mumbai
(21, 'https://images.unsplash.com/photo-1566665797739-1674de7a421a?w=600', 'Suite royale Mumbai');

-- 7. AJOUTER PLUS DE TYPES DE CHAMBRES POUR VARIÉTÉ
INSERT INTO chambres (num_chambre, type, prix_nuit, capacity, surface, statut, description, id_hotel) VALUES
-- Ajouter plus de types dans les hôtels existants
-- Hôtel 1 (Plaza Paris)
(104, 'Simple', 150.00, 1, 20, 'disponible', 'Chambre simple économique', 1),
(105, 'Double', 320.00, 2, 38, 'disponible', 'Chambre vue jardin', 1),
-- Hôtel 6 (Beach Marseille)
(603, 'Suite', 280.00, 2, 60, 'disponible', 'Suite avec Jacuzzi', 6),
(604, 'Simple', 85.00, 1, 22, 'disponible', 'Chambre simple vue mer', 6),
-- Hôtel 8 (Madrid)
(802, 'Suite', 220.00, 2, 45, 'disponible', 'Suite Terrasse', 8),
(803, 'Familiale', 190.00, 4, 48, 'disponible', 'Chambre familiale', 8);


-- Barcelona City Hotel
UPDATE hotels SET description = 'About this property: Barcelona City Hotel offers a vibrant urban experience in Spain''s most dynamic city. Accommodation Features: Modern rooms with Catalan design elements, private balconies with city views, and soundproof windows. Amenities and Facilities: Rooftop terrace with plunge pool, tapas bar, business corner, bike rental service. Location and Attractions: In Eixample district, near Sagrada Familia, Park Güell, and Las Ramblas. Perfect for exploring Gaudí''s architectural wonders.' WHERE nom_hotel = 'Barcelona City Hotel';

-- Bellecour Suites
UPDATE hotels SET description = 'About this property: Bellecour Suites provides elegant accommodation in Lyon''s historic Presqu''île district. Accommodation Features: Spacious suites with kitchenettes, high ceilings, and Bellecour Square views. Amenities and Facilities: Concierge service, private parking, laundry facilities, complimentary breakfast buffet. Location and Attractions: On Place Bellecour, Europe''s largest pedestrian square, near Vieux Lyon, Basilica of Notre-Dame de Fourvière, and silk museums.' WHERE nom_hotel = 'Bellecour Suites';

-- Berlin Backpackers
UPDATE hotels SET description = 'About this property: Berlin Backpackers offers budget-friendly accommodation for travelers exploring Germany''s capital. Accommodation Features: Dormitory-style rooms with secure lockers, private pods, and communal lounge areas. Amenities and Facilities: Shared kitchen, laundry room, travel desk, free city maps, board games. Location and Attractions: In Mitte district, close to Berlin Wall East Side Gallery, Alexanderplatz, and Museum Island. Ideal for solo travelers and groups.' WHERE nom_hotel = 'Berlin Backpackers';

-- Dubai Palace Hotel
UPDATE hotels SET description = 'About this property: Dubai Palace Hotel redefines luxury with Arabian-inspired opulence and modern comforts. Accommodation Features: Palace suites with gold-plated fixtures, private butler service, and panoramic Burj Khalifa views. Amenities and Facilities: Private beach access, infinity pools, 24-karat gold spa, helipad, Rolls-Royce chauffeur service. Location and Attractions: On Jumeirah Beach Road, near Burj Al Arab, Dubai Mall, and Palm Jumeirah. Experience ultimate Arabian luxury.' WHERE nom_hotel = 'Dubai Palace Hotel';

-- Grand Hôtel Saint-Germain
UPDATE hotels SET description = 'About this property: Grand Hôtel Saint-Germain offers a sophisticated 4-star stay in Paris'' literary and artistic district. Accommodation Features: Art Deco-inspired rooms with hardwood floors, French windows opening to balconies, and dedicated workspace. Amenities and Facilities: Rooftop terrace with panoramic city views, wine bar featuring local vintages, meeting rooms for business events, complimentary bicycle rental for exploring. Location and Attractions: Located in the historic Latin Quarter, steps from Sorbonne University, Notre-Dame Cathedral, and the iconic Saint-Germain-des-Prés cafés frequented by Hemingway and Picasso.' WHERE nom_hotel = 'Grand Hôtel Saint-Germain';

-- Hôtel Central Lyon
UPDATE hotels SET description = 'About this property: Hôtel Central Lyon provides convenient and comfortable accommodation in the heart of Lyon''s business district. Accommodation Features: Practical rooms with work desks, blackout curtains, and efficient layout for business travelers. Amenities and Facilities: Conference facilities, high-speed internet, express check-in/out, breakfast room. Location and Attractions: Near Part-Dieu business center, Lyon Opera House, and TGV train station. Perfect for corporate travelers and short stays.' WHERE nom_hotel = 'Hôtel Central Lyon';

-- Hôtel Corniche Luxury
UPDATE hotels SET description = 'About this property: Hôtel Corniche Luxury offers exclusive cliffside accommodation with Mediterranean panoramas. Accommodation Features: Villas with infinity pools, direct sea access, and sunset views from private terraces. Amenities and Facilities: Private yacht dock, seawater thalassotherapy center, Michelin-starred seafood restaurant, helicopter transfers. Location and Attractions: On Corniche Kennedy, overlooking Marseille''s calanques, near Frioul Islands and Château d''If. Ultimate coastal retreat.' WHERE nom_hotel = 'Hôtel Corniche Luxury';

-- Hôtel Plaza Paris
UPDATE hotels SET description = 'About this property: Hôtel Plaza Paris epitomizes Parisian luxury with breathtaking Eiffel Tower views from its prestigious Champs-Élysées address. Accommodation Features: Lavish suites featuring marble bathrooms, private balconies with iconic tower vistas, and king-size beds with Egyptian cotton linens. Amenities and Facilities: Indoor heated pool with skylight, three-Michelin-star restaurant by celebrity chef, 24/7 personalized concierge, Guerlain spa with hammam and treatment rooms. Location and Attractions: Situated on the world-famous Avenue des Champs-Élysées, moments from luxury boutiques, Arc de Triomphe, and Seine River dinner cruises.' WHERE nom_hotel = 'Hôtel Plaza Paris';

-- Le Royal Palace
UPDATE hotels SET description = 'About this property: Le Royal Palace is a historic 5-star palace hotel preserving Parisian grandeur in the heart of the city. Accommodation Features: Opulent rooms adorned with silk draperies, antique furniture pieces, and crystal chandeliers reflecting royal heritage. Amenities and Facilities: Grand ballroom for weddings and galas, intimate private dining rooms, wood-paneled library bar with rare cognacs, state-of-the-art fitness center with personal trainers. Location and Attractions: Nestled on Rue de Rivoli, walking distance to Louvre Museum masterpieces, Tuileries Garden, and the elegant shops of Palais Royal.' WHERE nom_hotel = 'Le Royal Palace';

-- London Business Hotel
UPDATE hotels SET description = 'About this property: London Business Hotel caters specifically to corporate travelers in the City of London financial district. Accommodation Features: Efficient rooms with ergonomic workstations, multiple power outlets, and enhanced soundproofing for uninterrupted work. Amenities and Facilities: Business center with printing services, video conference facilities, express laundry, complimentary newspapers. Location and Attractions: In the Square Mile, near Bank of England, Lloyd''s Building, and St. Paul''s Cathedral. Designed for productive business trips.' WHERE nom_hotel = 'London Business Hotel';

-- Madrid Royal Suites
UPDATE hotels SET description = 'About this property: Madrid Royal Suites offers luxurious urban living in Spain''s vibrant capital, combining traditional elegance with modern amenities. Accommodation Features: Spacious suites with separate living areas, fully-equipped kitchenettes, and panoramic city views from floor-to-ceiling windows. Amenities and Facilities: Rooftop pool with skyline vistas, authentic tapas bar showcasing Spanish cuisine, comprehensive business center, valet parking service. Location and Attractions: On the iconic Gran Vía, moments from Royal Palace exhibitions, world-renowned Prado Museum, and the serene Retiro Park. Perfect for cultural enthusiasts and business travelers alike.' WHERE nom_hotel = 'Madrid Royal Suites';

-- Milan Design Hotel
UPDATE hotels SET description = 'About this property: Milan Design Hotel celebrates Italian creativity in the world''s fashion capital. Accommodation Features: Minimalist rooms curated by local designers, custom furniture pieces, and rotating art installations. Amenities and Facilities: Design library, rooftop cocktail bar, pop-up fashion shows, bicycle rental with designer helmets. Location and Attractions: In Brera design district, near Duomo di Milano, Galleria Vittorio Emanuele II, and Last Supper reservation access.' WHERE nom_hotel = 'Milan Design Hotel';

-- Mumbai Royal Palace
UPDATE hotels SET description = 'About this property: Mumbai Royal Palace offers regal Indian hospitality with modern luxury in India''s financial capital. Accommodation Features: Palace-style suites with hand-carved furniture, silk textiles, and private sit-out areas. Amenities and Facilities: Ayurvedic spa with traditional treatments, Bollywood dance classes, private cinema screening room, elephant-themed afternoon tea. Location and Attractions: On Colaba Causeway, near Gateway of India, Taj Mahal Palace Hotel, and Elephanta Caves ferry.' WHERE nom_hotel = 'Mumbai Royal Palace';

-- New York Luxury Towers
UPDATE hotels SET description = 'About this property: New York Luxury Towers provides sky-high sophistication in Manhattan''s iconic skyline. Accommodation Features: Penthouse suites with floor-to-ceiling windows, private terraces, and panoramic Central Park or Hudson River views. Amenities and Facilities: Rooftop infinity pool, celebrity chef restaurant, 24-hour personal concierge, private helipad for city tours. Location and Attractions: On Fifth Avenue, overlooking Central Park, near Metropolitan Museum of Art, Broadway theaters, and luxury shopping.' WHERE nom_hotel = 'New York Luxury Towers';

-- Résidence Beach Marseille
UPDATE hotels SET description = 'About this property: Résidence Beach Marseille delivers authentic Mediterranean seaside living with direct beach access and Provençal charm. Accommodation Features: Bright, airy rooms decorated in nautical blue and white, most with private balconies offering uninterrupted sea views. Amenities and Facilities: Direct private beach with loungers, infinity pool blending with the horizon, seafood restaurant specializing in bouillabaisse, comprehensive water sports equipment rental. Location and Attractions: Situated on the scenic Corniche Kennedy, minutes from Calanques National Park hiking trails, the historic Old Port of Marseille with its fish market, and the panoramic Basilique Notre-Dame de la Garde.' WHERE nom_hotel = 'Résidence Beach Marseille';

-- Rome Luxury Hotel
UPDATE hotels SET description = 'About this property: Rome Luxury Hotel seamlessly blends ancient Roman heritage with contemporary five-star luxury steps from historic landmarks. Accommodation Features: Rooms inspired by Roman architecture with terra-cotta floors, fresco-style wall art, and bathrooms resembling ancient Roman baths. Amenities and Facilities: Rooftop restaurant with direct Colosseum views, authentic Roman spa featuring thermal baths, dedicated guided tour desk for private Vatican visits. Location and Attractions: On exclusive Via Condotti, moments from Spanish Steps fashion boutiques, Trevi Fountain wishes, and Vatican City spiritual sites.' WHERE nom_hotel = 'Rome Luxury Hotel';

-- Seoul Heritage Inn
UPDATE hotels SET description = 'About this property: Seoul Heritage Inn preserves traditional Korean hanok architecture with modern comforts. Accommodation Features: Ondol-heated floors, paper lantern lighting, and courtyard gardens following feng shui principles. Amenities and Facilities: Korean tea ceremony room, hanbok rental experience, traditional music performances, kimchi-making workshops. Location and Attractions: In Bukchon Hanok Village, near Gyeongbokgung Palace, Insadong antique shops, and N Seoul Tower.' WHERE nom_hotel = 'Seoul Heritage Inn';

-- Singapore Marina Suites
UPDATE hotels SET description = 'About this property: Singapore Marina Suites offers waterfront luxury with iconic Marina Bay views. Accommodation Features: High-rise suites with smart room technology, panoramic bay windows, and integrated work-living spaces. Amenities and Facilities: Infinity pool overlooking Gardens by the Bay, business club lounge, direct MRT access, complimentary smartphone for local use. Location and Attractions: At Marina Bay, near Singapore Flyer, ArtScience Museum, and world''s highest rooftop bar.' WHERE nom_hotel = 'Singapore Marina Suites';

-- Tokyo Capsule Hotel
UPDATE hotels SET description = 'About this property: Tokyo Capsule Hotel provides unique futuristic accommodation for the modern minimalist traveler. Accommodation Features: Compact capsule pods with individual climate control, personal entertainment systems, and privacy screens. Amenities and Facilities: Communal onsens (hot springs), manga library, vending machine gallery, 24-hour ramen counter. Location and Attractions: In Shinjuku district, near Tokyo Metropolitan Building, Kabukicho entertainment area, and robot restaurant shows.' WHERE nom_hotel = 'Tokyo Capsule Hotel';

-- Tokyo Skyline Hotel
UPDATE hotels SET description = 'About this property: Tokyo Skyline Hotel showcases cutting-edge technology with breathtaking Tokyo Tower views. Accommodation Features: Smart rooms with voice-controlled amenities, robot butler service, and virtual reality entertainment systems. Amenities and Facilities: Robot-staffed restaurant, tech concierge, VR gaming lounge, high-speed shinkansen booking service. Location and Attractions: In Minato ward, near Tokyo Tower, Roppongi nightlife, and teamLab Borderless digital art museum.' WHERE nom_hotel = 'Tokyo Skyline Hotel';

-- Zurich Budget Hotel
UPDATE hotels SET description = 'About this property: Zurich Budget Hotel offers affordable Swiss accommodation without compromising on quality and cleanliness. Accommodation Features: Compact yet functional rooms with alpine-inspired decor, efficient storage solutions, and blackout curtains. Amenities and Facilities: Shared kitchenette, luggage storage, free Swiss chocolate welcome, public transport pass included. Location and Attractions: Near Zurich Hauptbahnhof, Bahnhofstrasse shopping, Lake Zurich promenade, and Swiss National Museum.' WHERE nom_hotel = 'Zurich Budget Hotel';
