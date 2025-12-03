# PokeAPI Testing Project 

Projet de tests complet combinant **Selenium**, **Postman/Newman** et **JMeter** sur l'API [PokeAPI](https://pokeapi.co/).

```

## Types de tests

### 1. Tests UI - Selenium WebDriver
Tests de l'interface web de documentation PokeAPI :
- Chargement de la page d'accueil
- Présence des éléments interactifs
- Navigation vers la documentation
- Affichage des exemples JSON

**Exécution locale :**
```bash
mvn test -Dtest=selenium.PokeAPIWebTest
```

### 2. Tests API - Postman/Newman + REST Assured

#### Newman (Postman CLI)
Collection de 9 requêtes testant :
- GET /pokemon (liste)
- GET /pokemon/pikachu
- GET /pokemon/1 (Bulbasaur)
- GET /pokemon/fakemonster (404)
- GET /type/electric
- GET /type (tous les types)
- GET /ability/static
- GET /generation/1

**Exécution locale :**
```bash
npm install -g newman
newman run postman/PokeAPI_Collection.json -e postman/PokeAPI_Environment.json
```

#### REST Assured (Java)
8 tests API en Java :
- GET Pokemon par nom et ID
- Vérification des types et abilities
- Tests de statut 404
- Validation des stats de Pikachu

**Exécution locale :**
```bash
mvn test -Dtest=api.PokeAPITest
```

### 3. Tests de Performance - JMeter

3 Thread Groups :
| Thread Group | Users | Loops | Endpoint |
|--------------|-------|-------|----------|
| TG1 - Pokemon List | 5 | 3 | GET /pokemon?limit=20 |
| TG2 - Pokemon Details | 10 | 2 | GET /pokemon/pikachu |
| TG3 - CSV Data Driven | 3 | 2 | GET /pokemon/{id}, GET /type/{type} |

**Exécution locale :**
```bash
jmeter -n -t jmeter/pokeapi_performance_test.jmx -l results.jtl -e -o report
```

## Pipeline GitLab CI/CD

Le pipeline comporte 4 stages :

```
build → test-api → test-ui → test-performance
```

| Stage | Job | Description |
|-------|-----|-------------|
| build | build | Compilation Maven |
| test-api | test_api_newman | Tests Newman (Postman) |
| test-api | test_api_java | Tests REST Assured |
| test-ui | test_ui_selenium | Tests Selenium + Chrome |
| test-performance | test_performance_jmeter | Tests JMeter |

## Rapports

Après exécution du pipeline, les artifacts incluent :
- **Newman** : `results/newman-report.html`
- **Surefire** : `target/surefire-reports/*.xml`
- **JMeter** : `jmeter/report/index.html`