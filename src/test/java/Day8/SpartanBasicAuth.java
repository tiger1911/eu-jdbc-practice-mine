package Day8;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

public class SpartanBasicAuth {

    @Test
    public void test1(){

        given()
                .accept(ContentType.JSON)
                .and()
                .auth().basic("admin","admin")
        .when()
                .get("http://54.198.216.176:8000/api/spartans")
        .then().log().all()
                .statusCode(200);
    }

}
