package Homeworks;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class Homework_2 {

    @BeforeClass
    public void setUp(){
        baseURI = ConfigurationReader.get("spartan_api_url");

    }

    @Test
    public void Q1(){

        Response response = given().accept(ContentType.JSON)
                .and().pathParam("id", 20)
                .when().get("/api/spartans/{id}");

        JsonPath jsonPath = response.jsonPath();

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json;charset=UTF-8");

        assertTrue(response.getHeaders().hasHeaderWithName("Date"));

        String transferEncoding = response.getHeaders().getValue("Transfer-Encoding");
        System.out.println("transferEncoding = " + transferEncoding);
        assertEquals(transferEncoding,"chunked");

        int jsonId = jsonPath.getInt("id");
        String jsonName = jsonPath.getString("name");
        String gender = jsonPath.getString("gender");
        long phone = jsonPath.getLong("phone");

        System.out.println("JsonId = " + jsonId);
        System.out.println("JsonName = " + jsonName);
        System.out.println("gender = " + gender);
        System.out.println("phone = " + phone);

        assertEquals(jsonId,20);
        assertEquals(jsonName,"Lothario");
        assertEquals(gender,"Male");
        assertEquals(phone,7551551687L);



    }

    @Test
    public void Q2(){

        Map<String,Object> queryMap = new HashMap<>();
        queryMap.put("gender","Female");
        queryMap.put("nameContains","r");

        Response response = given().accept(ContentType.JSON)
                .and().queryParams(queryMap)
                .when().get("/api/spartans/search");

        JsonPath jsonPath = response.jsonPath();

        assertEquals(response.statusCode(),200);
        assertEquals(response.contentType(),"application/json;charset=UTF-8");

        List<String> genders = jsonPath.getList("content.gender");
        System.out.println("genders = " + genders);

        for (String gender : genders) {
            assertEquals(gender,"Female");
        }

        List<String> names = jsonPath.getList("content.name");
        System.out.println("names = " + names);

        for (String name : names) {
            assertTrue(name.toLowerCase().contains("r"));
        }

        int size = jsonPath.getInt("size");
        int totalPages = jsonPath.getInt("totalPages");
        String sorted = jsonPath.getString("pageable.sort.sorted");
        
        assertEquals(size,20);
        assertEquals(totalPages,1);
        assertEquals(sorted,"false");

    }



}
