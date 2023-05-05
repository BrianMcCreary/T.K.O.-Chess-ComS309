package TotalKnockoutChess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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

/**
 * @author Brian McCreary
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class LobbyTest {
    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void lobbyCreationRetrievalAndDeletionTest() {
//----------------Creation Test-----------------------------------
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("").
                when().
                post("/lobby/tester1");

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        String returnString = response.getBody().asString();

        try {
            assertEquals("success", returnString);
        } catch (Exception e) {
            e.printStackTrace();
        }
//--------------Retrieval Test 1 (getLobbies)---------------------------------
        Response response2 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("").
                when().
                get("/lobby/all");

        statusCode = response2.getStatusCode();
        assertEquals(200, statusCode);

        returnString = response2.getBody().asString();
        try {
            JSONArray returnArray = new JSONArray(returnString);
            JSONObject jsonObject = returnArray.getJSONObject(returnArray.length()-1);
            assertEquals("tester1", jsonObject.get("owner"));
        } catch (Exception e) {
            e.printStackTrace();
        }
//--------------Deletion Test---------------------------------
        Response response4 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("").
                when().
                delete("/lobby/delete/tester1");

        statusCode = response4.getStatusCode();
        assertEquals(200, statusCode);

        returnString = response4.getBody().asString();
        try {
            assertEquals("success", returnString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
