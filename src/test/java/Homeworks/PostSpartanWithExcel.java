package Homeworks;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ConfigurationReader;
import utilities.ExcelUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class PostSpartanWithExcel {

    @BeforeClass
    public void beforeclass(){
        baseURI= ConfigurationReader.get("spartan_api_url");
    }

    @Test
    public void test1(){

        ExcelUtil mockData = new ExcelUtil("src/test/resources/MOCK_DATA_2.xlsx","data");

        List<Map<String, Object>> dataList = mockData.getDataList1();

        System.out.println("dataList = " + dataList);

        for (Map<String, Object> dataListMap : dataList) {

                given().log().all()
                    .accept(ContentType.JSON)
                    .and()
                    .contentType(ContentType.JSON)
                    .and()
                    .body(dataListMap)
                .when()
                    .post("/api/spartans")
                .then().log().all()
                    .statusCode(201)
                    .and()
                    .contentType("application/json")
                    .and()
                    .body("success", is("A Spartan is Born!"),
                            "data.name",equalTo(dataListMap.get("name")),
                            "data.gender",equalTo(dataListMap.get("gender")),
                            "data.phone",equalTo(dataListMap.get("phone")));


        }

    }

    @Test
    public void test2(){

        Response response = when().get("https://my.api.mockaroo.com/spartan?key=d0fcd1a0");

        //response.prettyPrint();

        JsonPath jsonPath = response.jsonPath();

        List<Map<String,Object>> allNewSpartans = response.body().as(List.class);

        System.out.println("allNewSpartans = " + allNewSpartans);

        for (Map<String, Object> allNewSpartan : allNewSpartans) {

            given().log().all()
                    .accept(ContentType.JSON)
                    .and()
                    .contentType(ContentType.JSON)
                    .and()
                    .body(allNewSpartan)
                    .when()
                    .post("/api/spartans")
                    .then().log().all()
                    .statusCode(201)
                    .and()
                    .contentType("application/json")
                    .and()
                    .body("success", is("A Spartan is Born!"),
                            "data.name",equalTo(allNewSpartan.get("name")),
                            "data.gender",equalTo(allNewSpartan.get("gender")),
                            "data.phone",equalTo(allNewSpartan.get("phone")));
        }


    }



}
