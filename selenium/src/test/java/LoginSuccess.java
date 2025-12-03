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
 * Tests de connexion réussie - Scénarios positifs
 */
public class LoginSuccess {
    
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = System.getenv("BASE_URL") != null 
        ? System.getenv("BASE_URL") 
        : "https://practicetestautomation.com/practice-test-login/";
    
    @BeforeClass
    public void setupClass() {
        // Configuration automatique du driver Chrome
        WebDriverManager.chromedriver().setup();
    }
    
    @BeforeMethod
    public void setUp() {
        // Configuration des options Chrome
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Mode sans interface graphique
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    
    @Test(priority = 1, description = "Test de connexion avec identifiants valides")
    public void testValidLogin() {
        System.out.println("🧪 Test: Connexion avec identifiants valides");
        
        // Navigation vers la page de login
        driver.get(BASE_URL);
        System.out.println("✓ Navigation vers: " + BASE_URL);
        
        // Saisie des identifiants valides
        WebElement usernameField = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.id("username"))
        );
        usernameField.sendKeys("student");
        System.out.println("✓ Username saisi");
        
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("Password123");
        System.out.println("✓ Password saisi");
        
        // Clic sur le bouton de connexion
        WebElement submitButton = driver.findElement(By.id("submit"));
        submitButton.click();
        System.out.println("✓ Clic sur le bouton Submit");
        
        // Vérification de la redirection vers la page de succès
        wait.until(ExpectedConditions.urlContains("logged-in-successfully"));
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("logged-in-successfully"), 
            "URL ne contient pas 'logged-in-successfully'");
        System.out.println("✓ Redirection vers page de succès confirmée");
        
        // Vérification du message de succès
        WebElement successMessage = wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h1[contains(@class,'post-title')]")
            )
        );
        Assert.assertTrue(successMessage.isDisplayed(), 
            "Message de succès non affiché");
        System.out.println("✓ Message de succès affiché: " + successMessage.getText());
        
        // Vérification du bouton de déconnexion
        WebElement logoutButton = driver.findElement(
            By.xpath("//a[contains(@class,'wp-block-button__link') and contains(text(),'Log out')]")
        );
        Assert.assertTrue(logoutButton.isDisplayed(), 
            "Bouton de déconnexion non trouvé");
        System.out.println("✅ Test de connexion valide réussi");
    }
    
    @Test(priority = 2, description = "Vérification des éléments de la page après connexion")
    public void testPostLoginPageElements() {
        System.out.println("🧪 Test: Vérification des éléments post-connexion");
        
        // Connexion
        driver.get(BASE_URL);
        driver.findElement(By.id("username")).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("submit")).click();
        
        // Attendre la page de succès
        wait.until(ExpectedConditions.urlContains("logged-in-successfully"));
        
        // Vérifier le titre de la page
        String pageTitle = driver.getTitle();
        Assert.assertFalse(pageTitle.isEmpty(), "Titre de la page est vide");
        System.out.println("✓ Titre de la page: " + pageTitle);
        
        // Vérifier la présence du texte de confirmation
        WebElement congratsText = wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(),'Congratulations') or contains(text(),'successfully logged in')]")
            )
        );
        Assert.assertTrue(congratsText.isDisplayed(), 
            "Texte de confirmation non trouvé");
        System.out.println("✓ Texte de confirmation présent");
        
        System.out.println("✅ Vérification des éléments post-connexion réussie");
    }
    
    @Test(priority = 3, description = "Test du cycle complet: connexion -> déconnexion")
    public void testLoginLogoutCycle() {
        System.out.println("🧪 Test: Cycle connexion-déconnexion complet");
        
        // Connexion
        driver.get(BASE_URL);
        driver.findElement(By.id("username")).sendKeys("student");
        driver.findElement(By.id("password")).sendKeys("Password123");
        driver.findElement(By.id("submit")).click();
        
        // Attendre la connexion
        wait.until(ExpectedConditions.urlContains("logged-in-successfully"));
        System.out.println("✓ Connexion réussie");
        
        // Déconnexion
        WebElement logoutButton = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//a[contains(@class,'wp-block-button__link') and contains(text(),'Log out')]")
            )
        );
        logoutButton.click();
        System.out.println("✓ Clic sur déconnexion");
        
        // Vérifier le retour à la page de login
        wait.until(ExpectedConditions.urlContains("practice-test-login"));
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("practice-test-login"), 
            "Pas de retour à la page de login");
        System.out.println("✓ Retour à la page de login confirmé");
        
        // Vérifier que les champs de login sont à nouveau présents
        WebElement usernameField = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.id("username"))
        );
        Assert.assertTrue(usernameField.isDisplayed(), 
            "Champ username non affiché");
        
        System.out.println("✅ Cycle connexion-déconnexion réussi");
    }
    
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("🔚 Navigateur fermé\n");
        }
    }
}
