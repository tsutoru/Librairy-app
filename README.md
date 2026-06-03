# Library-App

## Description du projet

**Library-App** est une application de gestion de librairie permettant d'administrer efficacement les livres, les ventes, les arrivages et les réservations. Son objectif principal est de faciliter la gestion du stock, le suivi des ventes ainsi que l'analyse des performances commerciales de la librairie.

L'application permet également de catégoriser les livres afin de simplifier leur recherche et leur organisation.

---

# Objectifs

* Gérer le catalogue des livres.
* Gérer les auteurs.
* Suivre les ventes réalisées.
* Enregistrer les nouveaux arrivages.
* Gérer les exemplaires (copies) disponibles pour chaque livre.
* Gérer les clients et utilisateurs.
* Organiser les livres par catégories.
* Permettre la réservation de livres.
* Produire des statistiques et des rapports de performance.

---

# Classes Principales

## 1. Book (Livre)

Représente un livre dans la librairie.

### Attributs

| Attribut    | Type      | Description                        |
|-------------|-----------|------------------------------------|
| id          | Int       | Identifiant unique                 |
| title       | String    | Titre du livre                     |
| description | String    | Description du livre               |
| price       | Double    | Prix unitaire                      |
| publishDate | LocalDate | Date de publication                |
| isbn        | String    | Code ISBN unique                   |
| categoryId  | Int       | Référence vers la catégorie        |

>**Remarque :** Un livre peut avoir **plusieurs auteurs** (co-auteurs). La relation `Book ↔ Author` est donc **Many-to-Many** (table de jointure `book_author`).

---

## 2. Author (Auteur)

Représente l'auteur d'un ou plusieurs livres.

### Attributs

| Attribut    | Type   | Description              |
|-------------|--------|--------------------------|
| id          | Int    | Identifiant unique       |
| firstName   | String | Prénom                   |
| lastName    | String | Nom de famille           |
| biography   | String | Biographie               |
| nationality | String | Nationalité              |

---

## 3. Category (Catégorie)

Permet de regrouper les livres selon leur domaine.

### Attributs

| Attribut    | Type   | Description                   |
|-------------|--------|-------------------------------|
| id          | Int    | Identifiant unique            |
| name        | String | Nom de la catégorie           |
| description | String | Description de la catégorie   |

### Exemples de catégories

* Science Fiction
* Romance
* Informatique
* Histoire
* Développement Personnel

---

## 4. BookCopy (Exemplaire)

Représente une copie physique d'un livre disponible en stock.

> 

### Attributs

| Attribut | Type | Description                        |
|----------|------|------------------------------------|
| id       | int  | Identifiant unique                 |
| bookId   | int  | Référence vers le livre            |
| status   | Enum | Statut de l'exemplaire             |

### Statuts possibles (Enum)

```java
public enum CopyStatus {
    AVAILABLE,   
    RESERVED,    
    SOLD,        
}
```

---

## 5. Arrival (Arrivage)

Permet d'enregistrer les nouveaux exemplaires entrant dans le stock.

### Attributs

| Attribut    | Type      | Description                            |
|-------------|-----------|----------------------------------------|
| id          | int       | Identifiant unique                     |
| arrivalDate | LocalDate | Date de l'arrivage                     |
| quantity    | Integer   | Nombre d'exemplaires reçus             |
| supplier    | String    | Nom du fournisseur                     |
| bookId      | Long      | Référence vers le livre concerné       |


---

## 6. Sale (Vente)

Représente une vente effectuée dans la librairie.

### Attributs

| Attribut    | Type      | Description                                      |
|-------------|-----------|--------------------------------------------------|
| id          | int       | Identifiant unique                               |
| saleDate    | LocalDate | Date de la vente                                 |
| quantity    | Integer   | Nombre d'exemplaires vendus                      |
| totalAmount | Double    | Montant total de la vente                        |
| customerId  | Long      | Référence vers le client                         |
| copyBookId  | Long      | Référence vers l'exemplaire vendu *(corrigé)*    |


---

## 7. Customer (Client)

Représente une personne qui réserve ou achète un livre.

### Attributs

| Attribut  | Type   | Description              |
|-----------|--------|--------------------------|
| id        | int    | Identifiant unique       |
| firstName | String | Prénom                   |
| lastName  | String | Nom de famille           |
| email     | String | Adresse e-mail           |
| phone     | String | Numéro de téléphone      |
| address   | String | Adresse postale          |

---

## 8. Reservation (Réservation)

Permet à un client de réserver un exemplaire de livre.

### Attributs

| Attribut        | Type      | Description                              |
|-----------------|-----------|------------------------------------------|
| id              | int       | Identifiant unique                       |
| reservationDate | LocalDate | Date de la réservation                   |
| expirationDate  | LocalDate | Date d'expiration de la réservation      |
| status          | Enum      | Statut de la réservation                 |
| customerId      | Long      | Référence vers le client                 |
| copyBookId      | Long      | Référence vers l'exemplaire réservé      |

### Statuts possibles (Enum)

```java
public enum ReservationStatus {
    PENDING,     
    CONFIRMED,   
    CANCELLED,   
    CONVERTED,   
    EXPIRED      
}
```

---

# Relations entre les entités

```
Book ──────────────────────< BookCopy
  │ (Many-to-Many)               │
Author                      ┌────┴────┐
                           Sale   Arrival
                            │
                         Customer

Book >──── Category
BookCopy ──1,1──> Reservation >──── Customer
```

| Relation                  | Cardinalité            |
|---------------------------|------------------------|
| Book ↔ Author             | Many-to-Many           |
| Book → Category           | Many-to-One            |
| Book → BookCopy           | One-to-Many            |
| BookCopy → Arrival        | Many-to-One            |
| BookCopy → Sale           | Many-to-One            |
| BookCopy → Reservation    | One-to-One             |
| Customer → Sale           | One-to-Many            |
| Customer → Reservation    | One-to-Many            |

---

# Fonctionnalités Principales

## Gestion des Livres

* Ajouter un livre.
* Modifier un livre.
* Supprimer un livre.
* Consulter un livre.
* Rechercher un livre (par titre, auteur, catégorie, ISBN).
* Consulter les exemplaires disponibles.

---

## Gestion des Stocks

### Nombre total de livres

* Nombre total de livres dans le catalogue.
* Nombre total d'exemplaires par livre.
* Nombre d'exemplaires disponibles par livre.

### Livres par catégorie

* Nombre de livres dans chaque catégorie.
* Pourcentage que représente chaque catégorie.

### Livres par auteur

* Nombre de livres écrits par chaque auteur.
* Nombre d'exemplaires disponibles par auteur.

---

## Gestion des Prix

* Afficher le prix de chaque livre.
* Consulter la valeur totale du stock.
* Calculer les revenus générés par les ventes.

---

## Gestion des Arrivages

* Enregistrer les nouveaux arrivages.
* Conserver l'historique des arrivages.
* Afficher les quantités reçues par période.
* Mettre à jour automatiquement le stock lors d'un arrivage.

---

## Gestion des Ventes

* Enregistrer les ventes.
* Afficher les ventes du jour.
* Afficher les ventes du mois.
* Afficher les ventes de l'année.
* Produire un bilan des ventes.
* Mettre à jour automatiquement le statut du `BookCopy` lors d'une vente (`SOLD`).

---

## Gestion des Réservations

* Réserver un exemplaire pour un client.
* Annuler une réservation.
* Consulter les réservations en cours.
* Vérifier la disponibilité d'un exemplaire.
* Transformer une réservation en achat (statut → `CONVERTED`).
* Expirer automatiquement les réservations dépassées.

---

# Statistiques et Analyses

## Auteur le plus performant

* L'auteur le plus vendu.
* L'auteur générant le plus de revenus.
* Le pourcentage de revenus généré par cet auteur.

### Exemple

> **Auteur : John Doe**
> - Revenus du jour : 150 $
> - Revenus du mois : 3 200 $
> - Part du chiffre d'affaires : 35 %

---

## Catégorie la plus rentable

* La catégorie la plus vendue.
* Les revenus générés par catégorie.
* Le pourcentage du chiffre d'affaires par catégorie.

### Exemple

> **Catégorie : Informatique**
> - Revenus du jour : 500 $
> - Revenus du mois : 8 000 $
> - Part du chiffre d'affaires : 42 %

---

## Livre le plus performant

* Le livre le plus vendu.
* Le revenu généré par ce livre.
* Son évolution dans le temps.

---

# Tableau de Bord (Dashboard)

| Indicateur                  | Description                              |
|-----------------------------|------------------------------------------|
| Nombre total de livres      | Catalogue complet                        |
| Nombre total d'exemplaires  | Tous statuts confondus                   |
| Exemplaires disponibles     | Statut `AVAILABLE` uniquement            |
| Ventes aujourd'hui          | Nombre de ventes du jour                 |
| Revenus du jour             | Total des ventes du jour                 |
| Revenus du mois             | Total des ventes du mois en cours        |
| Auteur le plus populaire    | Basé sur les ventes                      |
| Catégorie la plus rentable  | Basé sur les revenus                     |
| Livre le plus vendu         | Basé sur le nombre d'exemplaires vendus  |
| Réservations en cours       | Statuts `PENDING` et `CONFIRMED`         |

---

# Stack Technique

| Couche           | Technologie                  |
|------------------|------------------------------|
| Framework        | Spring Boot                  |
| ORM              | Spring Data JPA / Hibernate  |
| Base de données  | PostgreSQL (prod) / H2 (dev) |
| Sécurité         | Spring Security + JWT        |
| Documentation    | Swagger / OpenAPI 3          |
| Tests            | JUnit 5 + Mockito            |
| Build            | Maven                        |
| Initialisation   | Poja                         |

---

# Résultat Attendu

**Library-App** doit offrir une solution complète de gestion de librairie permettant :

* Une gestion efficace du catalogue de livres et de leurs exemplaires.
* Un suivi précis des stocks en temps réel.
* Une gestion simplifiée des ventes et arrivages.
* Une analyse statistique des performances commerciales.
* Une meilleure organisation par catégories et auteurs.
* Une gestion fluide des réservations clients.
* Une aide à la prise de décision grâce aux indicateurs de performance du tableau de bord.