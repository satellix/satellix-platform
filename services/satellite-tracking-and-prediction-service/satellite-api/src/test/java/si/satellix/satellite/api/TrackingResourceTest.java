package si.satellix.satellite.api;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
public class TrackingResourceTest {

    @Test
    void getSatellitePositionsReturnsPositionAndTrajectory(){
        given()
                .accept("application/json")
                .pathParam("noradId", 25544)
        .when()
                .get("/v1/satellites/{noradId}/positions")
        .then()
                .statusCode(200)
                .contentType("application/json")
                .body("noradId", is(25544))
                .body("trajectory.size()", is(2));
    }

    @Test
    void getSatellitePositionsReturns404ForMissingSatellite(){
        given()
                .accept("application/problem+json")
                .pathParam("noradId", 99999)
        .when()
                .get("/v1/satellites/{noradId}/positions")
        .then()
                .statusCode(404)
                .contentType("application/problem+json");
    }

    @Test
    void getSatellitePositionsRejectsInvalidPathParameter(){
        given()
                .accept("application/problem+json")
                .pathParam("noradId", "prpo")
        .when()
                .get("/v1/satellites/{noradId}/positions")
        .then()
                .statusCode(400)
                .contentType("application/problem+json");
    }
}
