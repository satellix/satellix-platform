package si.satellix.satellite.api;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class VisualPassResourceTest {

    @Test
    void listVisualPassesReturnsPassesForIss(){
        given()
                .accept("application/json")
                .pathParam("noradId", 25544)
                // Ljubljana
                .queryParams(
                        "latitude", new BigDecimal("46.056946"),
                        "longitude", new BigDecimal("14.505751")
                )
        .when()
                .get("/v1/satellites/{noradId}/visual-passes")
        .then()
                .statusCode(200)
                .contentType("application/json")
                .body("passes.size()", is(2))
                .body("passes.startTime", everyItem(notNullValue()))
                .body("passes.endTime", everyItem(notNullValue()))
                .body("passes.maxElevationDegrees", contains(45.0f, 25.0f));
    }

    @Test
    void getVisualPassesReturns404ForMissingSatellite(){
        given()
                .accept("application/problem+json")
                .pathParam("noradId", 99999)
                .queryParams(
                        "latitude", new BigDecimal("46.056946"),
                        "longitude", new BigDecimal("14.505751")
                )
        .when()
                .get("/v1/satellites/{noradId}/visual-passes")
        .then()
                .statusCode(404)
                .contentType("application/problem+json");
    }

    @Test
    void getVisualPassesRejectsInvalidQueryParameter(){
        given()
                .accept("application/problem+json")
                .pathParam("noradId", 25544)
                .queryParams(
                        "latitude", new BigDecimal("146.056946"),
                        "longitude", new BigDecimal("14.505751")
                )
        .when()
                .get("/v1/satellites/{noradId}/visual-passes")
        .then()
                .statusCode(400)
                .contentType("application/problem+json");
    }
}
