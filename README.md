# Job Matcher Back

Backend Spring Boot d’une application de **matching entre un profil candidat et des offres d’emploi**.

L’application s’appuie sur les APIs **France Travail** pour :
- rechercher des offres d’emploi,
- récupérer le détail d’une offre,
- charger le référentiel **ROME des compétences**,
- comparer les compétences d’un profil avec celles demandées par une offre,
- exposer des indicateurs de synthèse pour un tableau de bord.

## Sommaire
- [1. Description générale](#1-description-générale)
- [2. Fonctionnalités principales](#2-fonctionnalités-principales)
- [3. Stack technique et versions compatibles](#3-stack-technique-et-versions-compatibles)
- [4. Prérequis](#4-prérequis)
- [5. Démarrage rapide](#5-démarrage-rapide)
- [6. Configuration Vault en mode dev](#6-configuration-vault-en-mode-dev)
- [7. Configuration applicative](#7-configuration-applicative)
- [8. Modèle de données](#8-modèle-de-données)
- [9. APIs exposées](#9-apis-exposées)
- [10. Exemples de payloads](#10-exemples-de-payloads)
- [11. Données d’initialisation](#11-données-dinitialisation)
- [12. Build, tests et exécution](#12-build-tests-et-exécution)
- [13. Documentation et observabilité](#13-documentation-et-observabilité)
- [14. Points d’attention](#14-points-dattention)

## 1. Description générale

Ce projet expose une API REST permettant de :
- gérer un **profil utilisateur** (informations personnelles + compétences),
- rechercher des **offres d’emploi**,
- calculer un **score de correspondance** entre un profil et une offre,
- gérer une liste d’**offres favorites**,
- alimenter un **dashboard** avec des indicateurs de matching.

Le backend conserve les données locales utiles au profil et aux favoris en base, et récupère les offres/compétences depuis les services externes de **France Travail**.

## 2. Fonctionnalités principales

### Gestion du profil
- consultation d’un profil utilisateur,
- mise à jour des données personnelles,
- synchronisation des compétences utilisateur,
- validation des compétences mises à jour contre le référentiel **ROME**.

### Recherche d’offres
- recherche des offres par mot-clé,
- récupération du détail d’une offre à partir de son identifiant France Travail,
- transformation des réponses France Travail vers des DTOs internes.

### Matching profil / offre
- comparaison des compétences du profil avec celles demandées par une offre,
- calcul d’un score en pourcentage,
- restitution des compétences trouvées et manquantes.

### Offres favorites
- ajout d’une offre aux favoris,
- suppression d’un favori,
- récupération des favoris d’un utilisateur.

### Dashboard
- calcul du match moyen sur les offres analysées,
- nombre d’offres analysées,
- nombre d’offres favorites,
- top 3 des compétences les plus souvent manquantes.

## 3. Stack technique et versions compatibles

Versions observées dans `pom.xml` et la configuration du projet :

| Élément | Version / information |
|---|---|
| Java | **17** |
| Maven | Wrapper fourni via `mvnw` / `mvnw.cmd` |
| Spring Boot | **4.1.0** |
| Spring Cloud | **2025.1.3** |
| Spring Data JPA | inclus |
| Spring Web MVC | inclus |
| Validation Jakarta | incluse |
| Spring Cloud Vault Config | inclus |
| Spring Cloud OpenFeign | inclus |
| SpringDoc OpenAPI | **3.1.0** |
| Flyway | inclus |
| H2 Database | incluse |
| Cache | Caffeine |
| Mapping | MapStruct **1.7.0.Beta1** |
| Lombok | **1.18.30** |
| Tests d’API externes | WireMock **2.35.2** |

### Compatibilité recommandée
- **JDK 17**
- **Docker Desktop** si vous lancez Vault en conteneur
- **Vault en mode dev** avec :
  - URL `http://localhost:8200`
  - authentification par token
  - token `dev-root-token`
  - moteur KV v2 sur le backend `secret`

### Compatibilité validée
- configuration de compilation Maven ciblée en **Java 17**,
- suite de tests exécutée avec succès dans ce workspace via `./mvnw test`,
- exécution constatée dans l'environnement local avec **Java 21** tout en conservant une cible de compilation **17**.

> Le projet est configuré pour le profil `dev` avec **H2 en mémoire**. Aucun SGBD externe n’est nécessaire pour démarrer localement.

## 4. Prérequis

Avant de démarrer, vérifier :
- Java 17 installé,
- Docker installé si vous souhaitez exécuter Vault en conteneur,
- accès réseau aux APIs **France Travail**,
- identifiants OAuth2 France Travail :
  - `api.offredemploi.client_id`
  - `api.offredemploi.client_secret`

## 5. Démarrage rapide

### Étape 1 — Lancer Vault en mode dev
Voir la section [Configuration Vault en mode dev](#6-configuration-vault-en-mode-dev).

### Étape 2 — Injecter les secrets France Travail dans Vault
Les secrets attendus sont :
- `api.offredemploi.client_id`
- `api.offredemploi.client_secret`

### Étape 3 — Lancer l’application
Sous Windows PowerShell :

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--spring.profiles.active=dev"
```

L’application démarre par défaut sur :
- `http://localhost:8080`

### Étape 4 — Vérifier le démarrage
Endpoints utiles :
- Swagger UI : `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON : `http://localhost:8080/v3/api-docs`
- Actuator Health : `http://localhost:8080/actuator/health`

## 6. Configuration Vault en mode dev

Le profil `dev` contient les propriétés suivantes :
- `spring.cloud.vault.enabled=true`
- `spring.config.import=optional:vault://`
- `spring.cloud.vault.uri=http://localhost:8200`
- `spring.cloud.vault.authentication=TOKEN`
- `spring.cloud.vault.token=dev-root-token`
- backend KV : `secret`
- contexte par défaut : `recherche-offre-api`

### Option A — Démarrer Vault avec Docker

```powershell
docker run --cap-add=IPC_LOCK -e VAULT_DEV_ROOT_TOKEN_ID=dev-root-token -p 8200:8200 --name job-matcher-vault hashicorp/vault:latest
```

Puis injecter les secrets :

```powershell
docker exec job-matcher-vault sh -c "vault kv put secret/recherche-offre-api api.offredemploi.client_id='votre-client-id' api.offredemploi.client_secret='votre-client-secret'"
```

### Option B — Démarrer Vault avec le binaire local

```powershell
vault server -dev -dev-root-token-id=dev-root-token
```

Puis :

```powershell
$env:VAULT_ADDR = "http://127.0.0.1:8200"
$env:VAULT_TOKEN = "dev-root-token"
vault kv put secret/recherche-offre-api api.offredemploi.client_id="votre-client-id" api.offredemploi.client_secret="votre-client-secret"
```

### Important

L’import Vault est **optionnel** (`optional:vault://`). Cela signifie que l’application peut démarrer même si Vault n’est pas disponible.

En revanche, **les appels aux APIs France Travail nécessitent les secrets**. Sans eux :
- les endpoints de recherche d’offres,
- de détail d’offre,
- de référentiel ROME,
- de matching,
- et le dashboard
ne fonctionneront pas correctement.

## 7. Configuration applicative

### Profil Spring
Le profil local principal est :
- `dev`

### Base de données en développement
Le profil `dev` utilise :
- `jdbc:h2:mem:testdb`
- driver `org.h2.Driver`
- utilisateur `sa`
- pas de mot de passe

### Propriétés métier importantes

| Propriété | Description |
|---|---|
| `api.france-travail.auth.base-url` | URL d’authentification France Travail |
| `offre-emploi.api.base-url` | URL de base des APIs offres / ROME |
| `offre-emploi.api.scope` | scopes OAuth2 demandés |
| `offre-emploi.api.auth-cache-ttl-seconds` | durée de vie du token en cache |
| `offre-emploi.api.rome-cache-ttl-hours` | durée de vie du référentiel ROME en cache |

### Cache
Deux caches Caffeine sont configurés :
- cache du token d’authentification France Travail,
- cache du référentiel de compétences ROME.

## 8. Modèle de données

Le schéma est initialisé par Flyway via `src/main/resources/db/migration/V1__initier_schema.sql`.

### Vue d’ensemble

```text
USERS 1 --- n USER_SKILL n --- 1 SKILL
USERS 1 --- n SAVED_OFFER
```

### Tables principales

#### `USERS`
Stocke le profil utilisateur.

| Colonne | Type | Description |
|---|---|---|
| `ID` | `BIGINT` | identifiant technique |
| `FIRST_NAME` | `VARCHAR(100)` | prénom |
| `LAST_NAME` | `VARCHAR(100)` | nom |
| `EMAIL` | `VARCHAR(255)` | email unique |
| `LOCATION` | `VARCHAR(120)` | localisation |
| `ZIP_CODE` | `VARCHAR(10)` | code postal |
| `YEARS_EXPERIENCE` | `INT` | années d’expérience |

#### `SKILL`
Stocke les compétences connues localement.

| Colonne | Type | Description |
|---|---|---|
| `ID` | `BIGINT` | identifiant technique |
| `CODE` | `VARCHAR(120)` | code de compétence, unique |
| `LIBELLE` | `VARCHAR(120)` | libellé |

#### `USER_SKILL`
Table de jointure entre utilisateur et compétences.

| Colonne | Type | Description |
|---|---|---|
| `USER_ID` | `BIGINT` | FK vers `USERS.ID` |
| `SKILL_ID` | `BIGINT` | FK vers `SKILL.ID` |

#### `SAVED_OFFER`
Stocke les offres favorites d’un utilisateur.

| Colonne | Type | Description |
|---|---|---|
| `ID` | `BIGINT` | identifiant technique du favori |
| `USER_ID` | `BIGINT` | FK vers `USERS.ID` |
| `OFFER_ID` | `VARCHAR(100)` | identifiant France Travail de l’offre |
| `CREATED_AT` | `TIMESTAMP` | date d’ajout |

### Données non persistées
Les offres d’emploi et le référentiel ROME sont récupérés à la volée depuis France Travail puis exposés via des DTOs.

### DTOs principaux

#### `ProfilDto`
Représente le profil utilisateur exposé par l’API.
- `id`
- `prenom`
- `nom`
- `email`
- `localisation`
- `codePostal`
- `anneeExperience`
- `competences: List<SkillDto>`

#### `SkillDto`
- `id`
- `code`
- `libelle`

#### `RechercheOffreDto`
- `id` : identifiant local si favori
- `identifiantFt`
- `intituleOffre`
- `lieuTravail`
- `codePostal`
- `salaire`
- `competences`

#### `RechercheOffreDetailsDto`
Hérite de `RechercheOffreDto` et ajoute :
- `description`
- `typeContratLibelle`
- `natureContrat`
- `experienceLibelle`
- `dureeTravail`

#### `RapportCorrespondanceDto`
- `score`
- `competencesTrouvees`
- `competencesManquantes`

#### `DashboardDto`
- `matchMoyen`
- `nombreOffreAnalysees`
- `nombreOffreFavories`
- `competencesADevelopper`

### Règles métier notables
- le profil est mis à jour via `PUT /api/profil/{userId}` ;
- les compétences soumises sont validées contre le référentiel **ROME** ;
- une compétence inconnue du référentiel provoque une erreur `400` ;
- le score de matching est calculé de la manière suivante :

```text
score = nb_competences_trouvees * 100 / (nb_competences_trouvees + nb_competences_manquantes)
```

## 9. APIs exposées

Base path principale : `/api`

| Méthode | Endpoint | Description |
|---|---|---|
| `GET` | `/api/profil/{userId}` | récupérer le profil utilisateur |
| `PUT` | `/api/profil/{userId}` | mettre à jour le profil et ses compétences |
| `GET` | `/api/offres?query={motCle}` | rechercher des offres par mot-clé |
| `GET` | `/api/offres/{id}` | récupérer le détail d’une offre France Travail |
| `GET` | `/api/offres/favorites/user/{userId}` | lister les offres favorites d’un utilisateur |
| `POST` | `/api/offres/favorites/{offerId}/user/{userId}` | ajouter une offre aux favoris |
| `DELETE` | `/api/offres/favorites/{id}/user/{userId}` | supprimer un favori |
| `GET` | `/api/profil/{profilId}/offre/{offreId}/matching` | calculer le matching entre un profil et une offre |
| `GET` | `/api/dashboard/user/{userId}` | récupérer les indicateurs de dashboard |
| `GET` | `/api/rome/competences` | récupérer le référentiel ROME mis en cache |

### Codes de réponse usuels
- `200 OK` : succès
- `400 Bad Request` : paramètres invalides ou compétence inconnue
- `404 Not Found` : utilisateur ou ressource non trouvée

### Format d’erreur
Les erreurs sont exposées sous un format proche de :

```json
{
  "timestamp": "2026-08-31T10:15:30Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Le code de competence est obligatoire",
  "path": "/api/profil/1",
  "details": {
    "codePostal": "Le code postal doit contenir 5 chiffres"
  }
}
```

## 10. Exemples de payloads

### Récupérer un profil

```http
GET /api/profil/1
```

Exemple de réponse :

```json
{
  "anneeExperience": 6,
  "codePostal": "13100",
  "competences": [
    {
      "id": 1,
      "code": "109870",
      "libelle": "Java"
    },
    {
      "id": 2,
      "code": "113274",
      "libelle": "Framework Spring"
    }
  ],
  "email": "abdelhamid.ait-taleb@yahoo.fr",
  "id": 1,
  "localisation": "Aix-en-provence",
  "nom": "AIT TALEB",
  "prenom": "Abdelhamid"
}
```

### Mettre à jour un profil

```http
PUT /api/profil/1
Content-Type: application/json
```

Payload d’exemple :

```json
{
  "prenom": "NouveauPrenom",
  "nom": "NouveauNom",
  "email": "abdel@gmail.com",
  "localisation": "NouveauLieu",
  "codePostal": "75000",
  "anneeExperience": 5,
  "competences": [
    {
      "code": "519057",
      "libelle": "NouvelleCompetence"
    },
    {
      "code": "519073",
      "libelle": "NouvelleCompetence2"
    }
  ]
}
```

### Rechercher des offres

```http
GET /api/offres?query=serveur
```

### Consulter le détail d’une offre

```http
GET /api/offres/212TTQH
```

Exemple de réponse :

```json
{
  "codePostal": "65100",
  "competences": [
    {
      "id": null,
      "code": "100341",
      "libelle": "Procédures d'encaissement"
    }
  ],
  "description": "Description de l'offre...",
  "dureeTravail": "35H/semaine",
  "experienceLibelle": "Débutant accepté",
  "id": null,
  "identifiantFt": "212TTQH",
  "intituleOffre": "Vendeur / Vendeuse (H/F)",
  "lieuTravail": "65 - Lourdes",
  "natureContrat": null,
  "salaire": "Horaire de 12.31 Euros à 12.32 Euros sur 12.0 mois",
  "typeContratLibelle": "SAI"
}
```

### Ajouter une offre en favori

```http
POST /api/offres/favorites/213CMRK/user/1
```

Réponse : identifiant technique du favori créé ou existant.

### Calculer un matching

```http
GET /api/profil/1/offre/212TTQH/matching
```

Exemple de réponse :

```json
{
  "competencesManquantes": [
    {
      "id": null,
      "code": "100341",
      "libelle": "Procédures d'encaissement"
    }
  ],
  "competencesTrouvees": [],
  "score": 0
}
```

### Charger le dashboard

```http
GET /api/dashboard/user/1
```

Exemple de réponse :

```json
{
  "competencesADevelopper": [
    {
      "id": null,
      "code": "300282",
      "libelle": "Entretenir, nettoyer un espace, un lieu, un local"
    }
  ],
  "matchMoyen": 0,
  "nombreOffreAnalysees": 130,
  "nombreOffreFavories": 1
}
```

## 11. Données d’initialisation

La migration initiale crée :
- 1 utilisateur de démonstration,
- 6 compétences,
- les associations utilisateur/compétences.

Utilisateur initial :
- `id = 1`
- prénom : `Abdelhamid`
- nom : `AIT TALEB`
- localisation : `Aix-en-provence`
- code postal : `13100`
- expérience : `6 ans`

Cela permet de tester immédiatement les endpoints de profil, matching, dashboard et favoris.

## 12. Build, tests et exécution

### Lancer les tests

```powershell
.\mvnw.cmd test
```

### Lancer la vérification complète

```powershell
.\mvnw.cmd verify
```

### Construire le projet

```powershell
.\mvnw.cmd clean package
```

### Construire sans tests

```powershell
.\mvnw.cmd clean package -DskipTests
```

### Exécuter le JAR généré

```powershell
java -jar .\target\job-matcher-back-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
```

## 13. Documentation et observabilité

### Swagger / OpenAPI
- UI : `http://localhost:8080/swagger-ui.html`
- Spécification : `http://localhost:8080/v3/api-docs`

### Actuator
En `dev`, tous les endpoints d’Actuator sont exposés :
- `http://localhost:8080/actuator`
- `http://localhost:8080/actuator/health`

## 14. Points d’attention

- Les offres d’emploi et le référentiel ROME dépendent des APIs France Travail.
- Sans secrets Vault valides, l’application peut démarrer mais les endpoints métier dépendants de France Travail échoueront.
- En profil `dev`, certaines configurations Feign désactivent la vérification SSL stricte pour faciliter les appels en local.
- La base utilisée localement est **H2 en mémoire** : les données sont perdues à l’arrêt de l’application.

---

## Commandes utiles récapitulatives

```powershell
# 1. Lancer Vault en dev
docker run --cap-add=IPC_LOCK -e VAULT_DEV_ROOT_TOKEN_ID=dev-root-token -p 8200:8200 --name job-matcher-vault hashicorp/vault:latest

# 2. Injecter les secrets
docker exec job-matcher-vault sh -c "vault kv put secret/recherche-offre-api api.offredemploi.client_id='votre-client-id' api.offredemploi.client_secret='votre-client-secret'"

# 3. Lancer les tests
.\mvnw.cmd test

# 4. Démarrer l'application
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--spring.profiles.active=dev"
```

