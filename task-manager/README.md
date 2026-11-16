# Task Manager — API REST Spring Boot

Application de gestion des tâches développée avec Spring Boot 3, Spring Data JPA et PostgreSQL.

## Prérequis

| Outil | Version |
|---|---|
| Java | 17+ |
| Maven | 3.8+ |
| PostgreSQL | 14+ |

## Configuration de la base de données

Créer la base de données dans PostgreSQL :

```sql
CREATE DATABASE taskmanager;
```

Configurer les variables d'environnement dans PowerShell :

```powershell
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="votre_mot_de_passe"
```

La configuration PostgreSQL est définie dans `application.properties`. Le mot de passe est récupéré depuis la variable `DB_PASSWORD`.

## Lancer l'application

Ouvrir un terminal dans le dossier `task-manager` (celui qui contient `pom.xml`), puis exécuter :

```bash
mvn spring-boot:run
```

L'API est accessible à l'adresse :

http://localhost:8081

## Lancer les tests

Depuis le dossier `task-manager` :

```bash
mvn test
```

Les tests utilisent H2 en mémoire. Aucune connexion PostgreSQL n'est nécessaire.

## Endpoints REST

| Méthode | URL | Description |
|---|---|---|
| POST | `/api/tasks` | Créer une tâche |
| GET | `/api/tasks` | Lister toutes les tâches |
| GET | `/api/tasks/{id}` | Récupérer une tâche par ID |
| PUT | `/api/tasks/{id}` | Modifier une tâche |
| DELETE | `/api/tasks/{id}` | Supprimer une tâche |

## Exemple de payload (POST / PUT)

```json
{
  "title": "Rédiger le rapport",
  "description": "Rapport mensuel de novembre",
  "dueDate": "2025-11-30",
  "priority": "HIGH",
  "status": "TODO"
}
```

Valeurs acceptées :

- **priority** : `LOW`, `MEDIUM`, `HIGH`
- **status** : `TODO`, `IN_PROGRESS`, `DONE`

## Structure du projet

```text
src/main/java/com/taskmanager/taskmanager/
├── TaskManagerApplication.java
├── controller/
│   └── TaskController.java
├── service/
│   ├── TaskService.java
│   └── TaskMapper.java
├── repository/
│   └── TaskRepository.java
├── model/
│   ├── Task.java
│   ├── Priority.java
│   └── Status.java
├── dto/
│   ├── TaskRequestDTO.java
│   └── TaskResponseDTO.java
└── exception/
    ├── TaskNotFoundException.java
    └── GlobalExceptionHandler.java
```

## Tests Postman

Importer le fichier `postman/TaskManager.postman_collection.json` dans Postman pour tester les endpoints REST.