package TotalKnockoutChess;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class BoxingTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void gameCreationAndDeletionTest() {
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("").
                when().
                post("/boxingGame/tester1/tester2");

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("success", returnObj.get("message"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        Response response2 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("").
                when().
                get("/getBoxingGames");

        statusCode = response2.getStatusCode();
        assertEquals(200, statusCode);

        returnString = response2.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length()-1);
            assertEquals("tester1", returnObj.get("player1"));
            assertEquals("tester2", returnObj.get("player2"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        Response response3 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("").
                when().
                put("/boxingGame/tester1/tester2");

        statusCode = response3.getStatusCode();
        assertEquals(200, statusCode);

        returnString = response3.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("success", returnObj.get("message"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
