package Homeworks;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;

import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class Homework_1 {

    @BeforeClass
    public void setUp(){
        baseURI = ConfigurationReader.get("hr_api_url");
    }

    @Test
    public void Q1(){

        Response response = given().accept(ContentType.JSON)
                .and().pathParam("country_id", "US")
                .when().get("/countries/{country_id}");

        String countryId = response.path("country_id");
        String countryName = response.path("country_name");
        int regionId = response.path("region_id");

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json");
        assertEquals(countryId,"US");
        assertEquals(countryName,"United States of America");
        assertEquals(regionId,2);

        System.out.println("countryId = " + countryId);
        System.out.println("countryName = " + countryName);
        System.out.println("regionId = " + regionId);


    }

    @Test
    public void Q2(){

        Response response = given().accept(ContentType.JSON)
                .and().queryParam("q", "{\"department_id\":80}")
                .when().get("/employees");

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json");

        JsonPath jsonPath = response.jsonPath();

        List<String> jobIDs = jsonPath.getList("items.job_id");
        System.out.println("jobIDss = " + jobIDs);

        for (String jobID : jobIDs) {
            assertTrue(jobID.contains("SA"));
        }


        List<Object> departmentIDs = jsonPath.getList("items.department_id");
        System.out.println("departmentIDs = " + departmentIDs);

        for (Object departmentID : departmentIDs) {
            assertEquals(departmentID,80);
        }

        int count = jsonPath.getInt("count");
        System.out.println("count = " + count);
        assertEquals(count,25);



    }

    @Test
    public void Q3(){
        Response response = given().accept(ContentType.JSON)
                .and().queryParam("q", "{\"region_id\":3}")
                .when().get("/countries");

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json");

        JsonPath jsonPath = response.jsonPath();


        List<Object> regionIDs = jsonPath.getList("items.region_id");
        for (Object regionID : regionIDs) {
            System.out.println("regionID = " + regionID);
            assertEquals(regionID,3);
        }

        int count = response.path("count");
        System.out.println("count = " + count);
        assertEquals(count,6);

        String hasMore = jsonPath.getString("hasMore");
        System.out.println("hasMore = " + hasMore);
        assertEquals(hasMore,"false");


        List<String> countryNames = jsonPath.getList("items.country_name");
        String listExpected = "Australia,China,India,Japan,Malaysia,Singapore";
        for (String countryName : countryNames) {
            assertTrue(listExpected.contains(countryName));

        }


    }
}
