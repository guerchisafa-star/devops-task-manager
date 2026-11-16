# Task Manager — API REST Spring Boot

Application de gestion des tâches développée avec Spring Boot 3, Spring Data JPA et PostgreSQL.

---

## Prérequis

| Outil | Version |
|---|---|
| Java | 17+ |
| Maven | 3.8+ |
| PostgreSQL | 14+ |

---

## Configuration de la base de données

Créer la base dans PostgreSQL :

```sql
CREATE DATABASE taskmanager;
```

Les identifiants par défaut dans `application.properties` :

```
spring.datasource.url=jdbc:postgresql://localhost:5432/taskmanager
spring.datasource.username=postgres
spring.datasource.password=postgres
```

Modifiez ces valeurs selon votre environnement.

---

## Lancer l'application

```bash
mvn spring-boot:run
```

L'API est accessible sur `http://localhost:8080`.

---

## Lancer les tests

```bash
mvn test
```

Les tests utilisent H2 en mémoire — aucune connexion PostgreSQL n'est nécessaire.

---

## Endpoints

| Méthode | URL | Description |
|---|---|---|
| POST | `/api/tasks` | Créer une tâche |
| GET | `/api/tasks` | Lister toutes les tâches |
| GET | `/api/tasks/{id}` | Récupérer une tâche par ID |
| PUT | `/api/tasks/{id}` | Modifier une tâche |
| DELETE | `/api/tasks/{id}` | Supprimer une tâche |

---

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

---

## Structure du projet

```
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

---

## Tests Postman

Importer le fichier `postman/TaskManager.postman_collection.json` dans Postman.
