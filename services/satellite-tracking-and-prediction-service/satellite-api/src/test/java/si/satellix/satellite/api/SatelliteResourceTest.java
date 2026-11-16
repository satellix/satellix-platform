package si.satellix.satellite.api;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class SatelliteResourceTest {

    @Test
    void listSatellitesReturnsIssAndHubble(){
        given()
                .accept("application/json")
        .when()
                .get("/v1/satellites")
        .then()
                .statusCode(200)
                .contentType("application/json")
                .body("items.size()", is(2))
                .body("items.noradId", containsInAnyOrder(25544, 20580))
                .body("offset", is(0))
                .body("totalItems", is(2))
                .body("hasNext", is(false))
                .body("nextOffset", nullValue());
    }

    @Test
    void listSatellitesRejectsNegativeLimit() {
        given()
                .queryParam("limit", -1)
        .when()
                .get("/v1/satellites")
        .then()
                .statusCode(400)
                .contentType("application/problem+json")
                .body("type", is("about:blank"))
                .body("title", is("Bad Request"))
                .body("status", is(400))
                .body("errors.pointer[0]", containsString(".limit"));
    }

    @Test
    void listSatellitesRejectsNonNumericOffset() {
        given()
                .queryParam("offset", "prpo")
        .when()
                .get("/v1/satellites")
        .then()
                .statusCode(400)
                .contentType("application/problem+json")
                .body("type", is("about:blank"))
                .body("title", is("Bad Request"))
                .body("status", is(400));
    }

    @Test
    void getSatelliteByIdReturnsSatellite(){
        given()
                .accept("application/json")
        .when()
                .get("/v1/satellites/25544")
        .then()
                .statusCode(200)
                .contentType("application/json")
                .body("noradId", is(25544))
                .body("name", is("ISS (ZARYA)"));

    }

    @Test
    void getSatelliteByIdReturnsNotFound(){
        given()
                .accept("application/problem+json")
        .when()
                .get("/v1/satellites/999999")
        .then()
                .statusCode(404)
                .contentType("application/problem+json");
    }
}
