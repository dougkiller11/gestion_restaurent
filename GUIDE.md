# Guide rapide du projet (JavaFX + Maven)

## Lancer l’application
- Prérequis : JDK 21, Maven, MySQL en local.
- Base : `restau_db` auto-créée via JDBC (`createDatabaseIfNotExist=true`).
- Identifiants MySQL par défaut : user `root`, mdp `root` (modifiables dans `src/main/java/restau/dao/DBConnection.java`).
- Commande : `mvn clean javafx:run` (profil par défaut, plugin javafx-maven-plugin 0.0.8).

## Structure du code
- Entrée : `restau.app.Main` (ouvre `LoginWindow`).
- UI (scènes JavaFX, style Arc Burger) : `restau.ui.*`
  - `LoginWindow` : connexion (employé ou client), liens inscription / mot de passe oublié.
  - `RegistrationWindow` : création de compte client.
  - `MainWindow` : menu principal + carte latérale (Profil, Dashboard, Paramètres, Déconnexion).
  - `PlatsWindow` : ajout de plat (nom, catégorie, prix, image URL, description, dispo).
  - `ClientsWindow` : CRUD clients (nom, prénom, email, téléphone, adresse, mot de passe).
  - `DashboardWindow` : cartes de synthèse + trending items.
  - `ProfileWindow` : affichage/édition (nom, prénom, email, téléphone, adresse) via modales dédiées.
  - `BoutiqueWindow` : vue client, liste des plats disponibles et description ; accès après login client.
- DAO (accès BDD) : `restau.dao.*`
  - `DBConnection` : connexion MySQL, création/migration des tables au premier accès, utilisateur admin par défaut.
  - `AuthDAO` : login employés/clients, inscription client.
  - `ClientDAO`, `PlatDAO`, `ProfileDAO`, `CommandeDAO`, `PaimentDAO` : opérations CRUD/lecture.
- Modèles : `restau.model.*` (`Client`, `Plat`, etc.).
- Modules : `module-info.java` expose les packages `restau.*` et requiert JavaFX + `java.sql`.

## Base de données (MySQL)
- URL : `jdbc:mysql://127.0.0.1:3306/restau_db?createDatabaseIfNotExist=true&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true`
- Tables créées automatiquement (si absentes) :
  - `employes`, `clients`, `plats`, `commandes`, `commande_plats`, `paiments`, `ingredients`, `user_profile`.
  - Colonnes ajoutées à chaud si manquantes (ex. `phone`, `address`, `image_url`, `description`).
- Données par défaut :
  - Admin employé : `root` / `root` (actif).
  - Profil admin (`user_profile`) prérempli (id=1).

## Navigation / rôles
- Connexion :
  - Employé actif -> `MainWindow` (role depuis `employes`).
  - Client -> `BoutiqueWindow` (sélection de plats).
- Boutons principaux (`MainWindow`) :
  - Plats -> `PlatsWindow`
  - Clients -> `ClientsWindow`
  - Dashboard -> `DashboardWindow`
  - Profil -> `ProfileWindow`
  - Se déconnecter -> `LoginWindow`

## Styles et mises en page
- Thème sombre + accents dorés, fonds images Unsplash (peuvent échouer hors ligne).
- Toutes les fenêtres sont redimensionnables avec tailles min cohérentes.
- Formulaires nettoyés après sauvegarde (plats, clients).

## Points de vigilance
- Eclipse : définir **uniquement** `src/main/java` en Source Folder, puis Maven > Update Project pour éviter les faux “package not found”.
- Warnings Maven restants :
  - Encoding : ajouter dans `pom.xml`  
    `project.build.sourceEncoding` et `project.reporting.outputEncoding` à `UTF-8`.
  - Ressources : créer `src/main/resources` (même vide) pour supprimer le “skip non existing resourceDirectory”.
  - Images distantes : héberger en local (`src/main/resources/images/...`) si l’URL Unsplash est bloquée.
- MySQL : si identifiants/port changent, ajuster `DBConnection.URL/USER/PASSWORD`. Assurer les droits sur `restau_db`.

## Tests rapides
- Démarrer MySQL, vérifier `root/root`.
- `mvn clean javafx:run`
  - Tester login admin `root` / `root`.
  - Créer un client via Inscription, puis vérifier affichage dans `ClientsWindow`.
  - Ajouter un plat via `PlatsWindow`.

## Où modifier quoi
- Changer styles / tailles : fichiers `restau.ui.*` (CSS inline).
- Logique BDD : `restau.dao.*`.
- Modèles : `restau.model.*`.
- Point d’entrée / navigation initiale : `restau.app.Main` et `restau.ui.MainWindow`.

## Détail par fichier (rapide)
- `src/main/java/restau/app/Main.java` : lance JavaFX et ouvre `LoginWindow`.
- `src/main/java/restau/ui/LoginWindow.java` : authentifie via `AuthDAO`; route client -> `BoutiqueWindow`, employé -> `MainWindow`.
- `src/main/java/restau/ui/RegistrationWindow.java` : inscription client via `AuthDAO.registerClient`.
- `src/main/java/restau/ui/MainWindow.java` : hub admin avec menu latéral (Profil/Dashboard/Paramètres/Logout) et cartes Plats/Commandes/Clients.
- `src/main/java/restau/ui/PlatsWindow.java` : formulaire ajout plat (nom, catégorie, prix, image URL, description, dispo) -> `PlatDAO.addPlat`.
- `src/main/java/restau/ui/ClientsWindow.java` : table + formulaire CRUD clients (nom, prénom, email, phone, adresse, password) -> `ClientDAO`.
- `src/main/java/restau/ui/DashboardWindow.java` : cartes stats mock + trending list, menu latéral.
- `src/main/java/restau/ui/ProfileWindow.java` : affiche/sauvegarde profil admin via `ProfileDAO`, modales d’édition champ par champ.
- `src/main/java/restau/ui/BoutiqueWindow.java` : liste des plats disponibles (`PlatDAO.listAvailable`), affiche description, bouton déconnexion.
- `src/main/java/restau/dao/DBConnection.java` : URL/identifiants MySQL, création/migration tables, admin par défaut.
- `src/main/java/restau/dao/AuthDAO.java` : login employé actif / client, inscription client.
- `src/main/java/restau/dao/ClientDAO.java` : CRUD clients (inclut phone, address).
- `src/main/java/restau/dao/PlatDAO.java` : ajout plat, liste des plats disponibles (nom/catégorie/prix/dispo/image/description).
- `src/main/java/restau/dao/ProfileDAO.java` : charge/enregistre profil admin (user_profile id=1).
- `src/main/java/restau/model/Client.java`, `Plat.java`, `Commande.java`, `Paiment.java` : POJOs métier.

