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

/**
 * @author Brian McCreary
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class UserTest {
    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void userCreationRetrievalAndModificationTests() {
//----------------Creation test (createUser)---------------------------------
        Response response = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("").
                when().
                post("/users/tester99/testpassword/testpassword");

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode);

        int createdUserId = -1;

        String returnString = response.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("success", returnObj.get("message"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

//---------------Retrieval test 1 (getAllUsers)--------------------------------
        Response response2 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("").
                when().
                get("/users");

        statusCode = response2.getStatusCode();
        assertEquals(200, statusCode);

        returnString = response2.getBody().asString();
        try {
            JSONArray returnArr = new JSONArray(returnString);
            JSONObject returnObj = returnArr.getJSONObject(returnArr.length() - 1);
            createdUserId = returnObj.getInt("id");
            assertEquals("tester99", returnObj.get("username"));
            assertEquals("testpassword", returnObj.get("password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
//---------------Retrieval test 2 (getUsersAsString)---------------------------------
        Response response3 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("").
                when().
                put("/getusers");

        statusCode = response3.getStatusCode();
        assertEquals(200, statusCode);

        returnString = response3.getBody().asString();
        String[] returnStrings = returnString.split(" ");
        String returnUsername = returnStrings[returnStrings.length-1];
        try {
            assertEquals("tester99", returnUsername);
        } catch (Exception e) {
            e.printStackTrace();
        }
//---------------Retrieval test 3 (getUserByName)---------------------------------
        Response response4 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("").
                when().
                get("/users/getByName/tester99");

        statusCode = response4.getStatusCode();
        assertEquals(200, statusCode);

        returnString = response4.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals("tester99", returnObj.get("username"));
            assertEquals("testpassword", returnObj.get("password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
//---------------Retrieval test 4 (getUserById)---------------------------------
        Response response5 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("").
                when().
                get("/users/" + createdUserId);

        statusCode = response5.getStatusCode();
        assertEquals(200, statusCode);

        returnString = response5.getBody().asString();
        try {
            JSONObject returnObj = new JSONObject(returnString);
            assertEquals(createdUserId, returnObj.getInt("id"));
            assertEquals("tester99", returnObj.get("username"));
            assertEquals("testpassword", returnObj.get("password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
//---------------Modification test 1 (changeUserName)---------------------------------
        Response response6 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("").
                when().
                put("/users/username/tester99/testpassword/tester100");

        statusCode = response6.getStatusCode();
        assertEquals(200, statusCode);

        returnString = response6.getBody().asString();
        try {
            assertEquals("success", returnString);
        } catch (Exception e) {
            e.printStackTrace();
        }
//---------------Retrieval test 5 (getUsername)---------------------------------
        Response response7 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("").
                when().
                get("/users/name/" + createdUserId);

        statusCode = response7.getStatusCode();
        assertEquals(200, statusCode);

        returnString = response7.getBody().asString();
        try {
            assertEquals("tester100", returnString);
        } catch (Exception e) {
            e.printStackTrace();
        }
//---------------Modification test 2 (changeUserPassword)---------------------------------
        Response response8 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("").
                when().
                put("/users/password/tester100/testpassword/passwordtest");

        statusCode = response8.getStatusCode();
        assertEquals(200, statusCode);

        returnString = response8.getBody().asString();
        try {
            assertEquals("success", returnString);
        } catch (Exception e) {
            e.printStackTrace();
        }
//---------------Retrieval test 6 (getUserPassword)---------------------------------
        Response response9 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("").
                when().
                get("/users/password/" + createdUserId);

        statusCode = response9.getStatusCode();
        assertEquals(200, statusCode);

        returnString = response9.getBody().asString();
        try {
            assertEquals("passwordtest", returnString);
        } catch (Exception e) {
            e.printStackTrace();
        }
//---------------Modification test 3---------------------------------
        Response response10 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset", "utf-8").
                body("").
                when().
                put("/users/makeadmin/tester100");

        statusCode = response10.getStatusCode();
        assertEquals(200, statusCode);

        returnString = response10.getBody().asString();
        try {
            assertEquals("success", returnString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void deletionTest() throws Exception {
//----------------Deletion test (deleteUser)--------------------------------
        Response response3 = RestAssured.given().
                header("Content-Type", "text/plain").
                header("charset","utf-8").
                body("").
                when().
                put("/users/tester100");

        int statusCode = response3.getStatusCode();
        assertEquals(200, statusCode);

        String returnString = response3.getBody().asString();
        try {
        JSONObject returnObj = new JSONObject(returnString);
        assertEquals("success", returnObj.get("message"));
        }
        catch (JSONException e) {
        e.printStackTrace();
        }
    }
}
