# README - Projet de Tests Automatisés

## 📋 Description du Projet

Ce projet implémente une suite complète de tests automatisés comprenant :
- **Tests Selenium** : Tests fonctionnels de l'interface utilisateur
- **Tests Postman** : Tests d'API et d'intégration
- **Tests JMeter** : Tests de performance et de charge
- **Tests Chaos Toolkit** : Tests de fiabilité et de sécurité

## 🏗️ Structure du Projet

```
projettest/
├── selenium/           # Tests UI avec Selenium WebDriver
│   ├── src/test/java/
│   │   ├── LoginSuccess.java
│   │   └── LoginAdvanced.java
│   ├── pom.xml
│   └── testng.xml
├── postman/           # Tests API
│   └── collection.json
├── jmeter/            # Tests de performance
│   └── td_performance_test.jmx
├── chaos/             # Tests de fiabilité
│   ├── http_test.json
│   ├── cpu_test.json
│   └── auth_test.json
├── reports/           # Rapports générés
└── .gitlab-ci.yml     # Pipeline CI/CD
```

## 🚀 Prérequis

### Tests Selenium
- Java 17+
- Maven 3.9+
- Chrome/ChromeDriver (géré automatiquement par WebDriverManager)

### Tests Postman
- Node.js 14+
- Newman CLI : `npm install -g newman newman-reporter-htmlextra`

### Tests JMeter
- JMeter 5.7.1+
- Java 8+

### Tests Chaos
- Python 3.11+
- Chaos Toolkit : `pip install chaostoolkit chaostoolkit-http psutil`

## 📦 Installation

### 1. Cloner le projet
```powershell
cd "c:\Users\baude\OneDrive\Documents\cesi cours\projettest"
```

### 2. Installer les dépendances Selenium
```powershell
cd selenium
mvn clean install
```

### 3. Installer Newman (Postman CLI)
```powershell
npm install -g newman newman-reporter-htmlextra
```

### 4. Installer Chaos Toolkit
```powershell
pip install chaostoolkit chaostoolkit-http psutil
```

## 🧪 Exécution des Tests

### Tests Selenium
```powershell
cd selenium
mvn clean test
```

Ou pour des tests spécifiques :
```powershell
mvn test -Dtest=LoginSuccess
mvn test -Dtest=LoginAdvanced
```

### Tests Postman
```powershell
cd postman
newman run collection.json --reporters cli,htmlextra --reporter-htmlextra-export ../reports/postman-report.html
```

### Tests JMeter
```powershell
cd jmeter
jmeter -n -t td_performance_test.jmx -l ../reports/jmeter-results.jtl -e -o ../reports/jmeter-report
```

### Tests Chaos Toolkit
```powershell
cd chaos
chaos run http_test.json --journal-path=../reports/chaos-http.json
chaos run cpu_test.json --journal-path=../reports/chaos-cpu.json
chaos run auth_test.json --journal-path=../reports/chaos-auth.json
```

## 📊 Rapports

Les rapports sont générés dans le dossier `reports/` :
- **Selenium** : `selenium/target/surefire-reports/`
- **Postman** : `reports/postman-report.html`
- **JMeter** : `reports/jmeter-report/index.html`
- **Chaos** : `reports/chaos-*.json`

## 🔧 Configuration

### Variables d'environnement Selenium
- `BASE_URL` : URL de base pour les tests (par défaut : https://practicetestautomation.com)

### Variables JMeter
- `THREADS` : Nombre d'utilisateurs virtuels (50)
- `RAMP_UP` : Temps de montée en charge en secondes (10)
- `DURATION` : Durée du test en secondes (60)

## 🔄 Pipeline CI/CD

Le pipeline GitLab CI/CD (`.gitlab-ci.yml`) exécute automatiquement :
1. Tests Selenium (Stage 1)
2. Tests Postman (Stage 2)
3. Tests JMeter (Stage 3)
4. Tests Chaos (Stage 4)
5. Publication des rapports consolidés (Stage 5)

### Déclencher le pipeline
```bash
git add .
git commit -m "Ajout des tests"
git push origin main
```

## 📝 Tests Implémentés

### Selenium (UI)
- ✅ Connexion avec identifiants valides
- ✅ Vérification des éléments post-connexion
- ✅ Cycle connexion-déconnexion
- ✅ Validation des erreurs (username/password invalides)
- ✅ Tests de sécurité (injection SQL)

### Postman (API)
- ✅ Tests d'authentification (login/register)
- ✅ CRUD utilisateurs (GET, POST, PUT, DELETE)
- ✅ Gestion des erreurs (404, 400)
- ✅ Tests de pagination
- ✅ Validation des réponses JSON

### JMeter (Performance)
- ✅ Tests de charge (50 utilisateurs simultanés)
- ✅ Tests de montée en charge progressive
- ✅ Assertions sur les temps de réponse
- ✅ Validation des codes HTTP

### Chaos (Fiabilité)
- ✅ Tests de disponibilité HTTP
- ✅ Tests de charge CPU
- ✅ Tests de sécurité d'authentification
- ✅ Validation des codes d'erreur

## 🛠️ Dépannage

### Erreur Selenium : WebDriver not found
```powershell
mvn clean install
```

### Erreur Newman : command not found
```powershell
npm install -g newman
```

### Erreur JMeter : Java heap space
Augmenter la mémoire dans `jmeter.bat` :
```
set HEAP=-Xms1g -Xmx4g
```

### Erreur Chaos : Module not found
```powershell
pip install --upgrade chaostoolkit chaostoolkit-http psutil
```

## 📚 Documentation

- [Selenium Documentation](https://www.selenium.dev/documentation/)
- [Newman Documentation](https://learning.postman.com/docs/running-collections/using-newman-cli/)
- [JMeter Documentation](https://jmeter.apache.org/usermanual/index.html)
- [Chaos Toolkit Documentation](https://chaostoolkit.org/reference/tutorial/)

## 👥 Contributeurs

Projet créé dans le cadre du cours CESI

## 📄 Licence

Ce projet est à usage éducatif.
