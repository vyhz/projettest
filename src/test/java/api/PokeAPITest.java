package api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * Tests API REST sur PokeAPI avec REST Assured
 * Complète les tests Postman/Newman
 */
public class PokeAPITest {
    
    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://pokeapi.co/api/v2";
    }
    
    @Test(priority = 1, description = "GET Pokemon Pikachu")
    public void testGetPikachu() {
        given()
            .when()
                .get("/pokemon/pikachu")
            .then()
                .statusCode(200)
                .body("name", equalTo("pikachu"))
                .body("id", equalTo(25))
                .body("types", not(empty()));
        
        System.out.println("✓ GET /pokemon/pikachu - OK");
    }
    
    @Test(priority = 2, description = "GET Pokemon par ID")
    public void testGetPokemonById() {
        given()
            .when()
                .get("/pokemon/1")
            .then()
                .statusCode(200)
                .body("name", equalTo("bulbasaur"))
                .body("id", equalTo(1));
        
        System.out.println("✓ GET /pokemon/1 (Bulbasaur) - OK");
    }
    
    @Test(priority = 3, description = "GET Liste des Pokemon")
    public void testGetPokemonList() {
        given()
            .queryParam("limit", 10)
            .queryParam("offset", 0)
            .when()
                .get("/pokemon")
            .then()
                .statusCode(200)
                .body("count", greaterThan(0))
                .body("results", hasSize(10))
                .body("results[0].name", notNullValue());
        
        System.out.println("✓ GET /pokemon?limit=10 - OK");
    }
    
    @Test(priority = 4, description = "GET Type Pokemon")
    public void testGetPokemonType() {
        given()
            .when()
                .get("/type/electric")
            .then()
                .statusCode(200)
                .body("name", equalTo("electric"))
                .body("pokemon", not(empty()));
        
        System.out.println("✓ GET /type/electric - OK");
    }
    
    @Test(priority = 5, description = "GET Pokemon Ability")
    public void testGetAbility() {
        given()
            .when()
                .get("/ability/static")
            .then()
                .statusCode(200)
                .body("name", equalTo("static"))
                .body("pokemon", not(empty()));
        
        System.out.println("✓ GET /ability/static - OK");
    }
    
    @Test(priority = 6, description = "GET Generation")
    public void testGetGeneration() {
        given()
            .when()
                .get("/generation/1")
            .then()
                .statusCode(200)
                .body("name", equalTo("generation-i"))
                .body("pokemon_species", not(empty()));
        
        System.out.println("✓ GET /generation/1 - OK");
    }
    
    @Test(priority = 7, description = "GET Pokemon inexistant - 404")
    public void testGetNonExistentPokemon() {
        given()
            .when()
                .get("/pokemon/fakemonster999999")
            .then()
                .statusCode(404);
        
        System.out.println("✓ GET /pokemon/fakemonster999999 - 404 OK");
    }
    
    @Test(priority = 8, description = "Vérifier les stats de Pikachu")
    public void testPikachuStats() {
        Response response = given()
            .when()
                .get("/pokemon/pikachu")
            .then()
                .statusCode(200)
                .extract().response();
        
        // Vérifier que Pikachu a des stats
        int baseExperience = response.jsonPath().getInt("base_experience");
        Assert.assertTrue(baseExperience > 0, "Pikachu devrait avoir de l'expérience de base");
        
        // Vérifier le poids et la taille
        int weight = response.jsonPath().getInt("weight");
        int height = response.jsonPath().getInt("height");
        Assert.assertTrue(weight > 0, "Le poids devrait être positif");
        Assert.assertTrue(height > 0, "La taille devrait être positive");
        
        System.out.println("✓ Stats Pikachu - XP: " + baseExperience + ", Poids: " + weight + ", Taille: " + height);
    }
}
