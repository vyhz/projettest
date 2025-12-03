import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

/**
 * Tests de connexion avancés - Scénarios négatifs et cas limites
 */
public class LoginAdvanced {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = System.getenv("BASE_URL") != null 
        ? System.getenv("BASE_URL") 
        : "https://practicetestautomation.com/practice-test-login/";
    
    @BeforeClass
    public void setupClass() {
        WebDriverManager.chromedriver().setup();
    }
    
    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    
    @Test(priority = 1, description = "Test de connexion avec username invalide")
    public void testInvalidUsername() {
        System.out.println("🧪 Test: Username invalide");
        
        driver.get(BASE_URL);
        
        // Tentative avec username incorrect
        driver.findElement(By.id("username")).sendKeys("invalidUser");
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("submit")).click();
        
        // Vérifier le message d'erreur
        WebElement errorMessage = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.id("error"))
        );
        Assert.assertTrue(errorMessage.isDisplayed(), 
            "Message d'erreur non affiché");
        
        String errorText = errorMessage.getText();
        Assert.assertTrue(errorText.contains("username") || errorText.contains("invalid"), 
            "Message d'erreur incorrect: " + errorText);
        System.out.println("✓ Message d'erreur affiché: " + errorText);
        
        // Vérifier qu'on reste sur la page de login
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("practice-test-login"), 
            "Redirection inattendue");
        
        System.out.println("✅ Test username invalide réussi");
    }
    
    @Test(priority = 2, description = "Test de connexion avec password invalide")
    public void testInvalidPassword() {
        System.out.println("🧪 Test: Password invalide");
        
        driver.get(BASE_URL);
        
        // Tentative avec password incorrect
        driver.findElement(By.id("username")).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("wrongPassword");
        driver.findElement(By.id("submit")).click();
        
        // Vérifier le message d'erreur
        WebElement errorMessage = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.id("error"))
        );
        Assert.assertTrue(errorMessage.isDisplayed(), 
            "Message d'erreur non affiché");
        
        String errorText = errorMessage.getText();
        Assert.assertTrue(errorText.contains("password") || errorText.contains("invalid"), 
            "Message d'erreur incorrect: " + errorText);
        System.out.println("✓ Message d'erreur affiché: " + errorText);
        
        System.out.println("✅ Test password invalide réussi");
    }
    
    @Test(priority = 3, description = "Test avec champs vides")
    public void testEmptyFields() {
        System.out.println("🧪 Test: Champs vides");
        
        driver.get(BASE_URL);
        
        // Clic sur submit sans remplir les champs
        driver.findElement(By.id("submit")).click();
        
        // Vérifier la validation HTML5 ou message d'erreur
        WebElement usernameField = driver.findElement(By.id("username"));
        String validationMessage = usernameField.getAttribute("validationMessage");
        
        if (validationMessage != null && !validationMessage.isEmpty()) {
            System.out.println("✓ Validation HTML5: " + validationMessage);
            Assert.assertFalse(validationMessage.isEmpty(), 
                "Message de validation attendu");
        } else {
            // Certains sites affichent un message d'erreur personnalisé
            try {
                WebElement errorMessage = wait.until(
                    ExpectedConditions.presenceOfElementLocated(By.id("error"))
                );
                Assert.assertTrue(errorMessage.isDisplayed(), 
                    "Message d'erreur non affiché");
                System.out.println("✓ Message d'erreur: " + errorMessage.getText());
            } catch (Exception e) {
                System.out.println("✓ Validation côté client empêche la soumission");
            }
        }
        
        // Vérifier qu'on reste sur la page de login
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("practice-test-login"), 
            "Redirection inattendue avec champs vides");
        
        System.out.println("✅ Test champs vides réussi");
    }
    
    @Test(priority = 4, description = "Test avec username vide uniquement")
    public void testEmptyUsernameOnly() {
        System.out.println("🧪 Test: Username vide uniquement");
        
        driver.get(BASE_URL);
        
        // Password rempli mais pas username
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("submit")).click();
        
        // Vérifier qu'on ne peut pas se connecter
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("practice-test-login"), 
            "Connexion ne devrait pas être possible sans username");
        System.out.println("✓ Connexion bloquée sans username");
        
        System.out.println("✅ Test username vide réussi");
    }
    
    @Test(priority = 5, description = "Test de la sensibilité à la casse du password")
    public void testPasswordCaseSensitivity() {
        System.out.println("🧪 Test: Sensibilité à la casse du password");
        
        driver.get(BASE_URL);
        
        // Tentative avec password en minuscules
        driver.findElement(By.id("username")).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("password123"); // Mauvaise casse
        driver.findElement(By.id("submit")).click();
        
        // Vérifier qu'on ne peut pas se connecter
        try {
            WebElement errorMessage = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("error"))
            );
            Assert.assertTrue(errorMessage.isDisplayed(), 
                "Message d'erreur attendu pour mauvaise casse");
            System.out.println("✓ Password sensible à la casse confirmé");
        } catch (Exception e) {
            // Si pas de message d'erreur, vérifier qu'on n'est pas connecté
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("practice-test-login"), 
                "Ne devrait pas être connecté avec mauvaise casse");
            System.out.println("✓ Connexion refusée avec mauvaise casse");
        }
        
        System.out.println("✅ Test sensibilité à la casse réussi");
    }
    
    @Test(priority = 6, description = "Test d'injection SQL basique")
    public void testSQLInjectionAttempt() {
        System.out.println("🧪 Test: Tentative d'injection SQL");
        
        driver.get(BASE_URL);
        
        // Tentative d'injection SQL
        driver.findElement(By.id("username")).sendKeys("admin' OR '1'='1");
        driver.findElement(By.id("password")).sendKeys("admin' OR '1'='1");
        driver.findElement(By.id("submit")).click();
        
        // Vérifier que l'injection ne fonctionne pas
        try {
            WebElement errorMessage = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("error"))
            );
            System.out.println("✓ Injection SQL bloquée avec message d'erreur");
        } catch (Exception e) {
            // Vérifier qu'on n'est pas connecté
            String currentUrl = driver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("practice-test-login"), 
                "Injection SQL ne devrait pas permettre la connexion");
            System.out.println("✓ Injection SQL sans effet");
        }
        
        System.out.println("✅ Test sécurité injection SQL réussi");
    }
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("🔚 Navigateur fermé\n");
        }
    }
}
