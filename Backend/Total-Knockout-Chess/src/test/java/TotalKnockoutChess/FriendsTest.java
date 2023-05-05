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
 * @author Connor Hand
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class FriendsTest {
    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

//    @Test
//    public void sendAndDeleteRequest() {
//        Response response = RestAssured.given().
//                header("Content-Type", "text/plain").
//                header("charset","utf-8").
//                body("").
//                when().
//                post("/friendRequest/tester8/tester9");
//
//        int statusCode = response.getStatusCode();
//        assertEquals(200, statusCode);
//
//        String returnString = response.getBody().asString();
//        try {
//            JSONObject returnObj = new JSONObject(returnString);
//            assertEquals("success", returnObj.get("message"));
//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
////----------------------------------------------------------------
//        response = RestAssured.given().
//                header("Content-Type", "text/plain").
//                header("charset","utf-8").
//                body("").
//                when().
//                get("/friendRequests/outgoing/tester8");
//
//        statusCode = response.getStatusCode();
//        assertEquals(200, statusCode);
//
//        returnString = response.getBody().asString();
//        try {
//            JSONArray returnArr = new JSONArray(returnString);
//            assertEquals("tester9", returnArr.get(returnArr.length()-1));
//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
////----------------------------------------------------------------
//        response = RestAssured.given().
//                header("Content-Type", "text/plain").
//                header("charset","utf-8").
//                body("").
//                when().
//                get("/friendRequests/incoming/tester9");
//
//        statusCode = response.getStatusCode();
//        assertEquals(200, statusCode);
//
//        returnString = response.getBody().asString();
//        try {
//            JSONArray returnArr = new JSONArray(returnString);
//            assertEquals("tester8", returnArr.get(returnArr.length()-1));
//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
////----------------------------------------------------------------
//        response = RestAssured.given().
//                header("Content-Type", "text/plain").
//                header("charset","utf-8").
//                body("").
//                when().
//                put("/deleteFriendRequest/tester8/tester9");
//
//        statusCode = response.getStatusCode();
//        assertEquals(200, statusCode);
//
//        returnString = response.getBody().asString();
//        try {
//            JSONObject returnObj = new JSONObject(returnString);
//            assertEquals("success", returnObj.get("message"));
//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void sendAndAcceptRequestAndDeleteFriend() {
//        Response response = RestAssured.given().
//                header("Content-Type", "text/plain").
//                header("charset","utf-8").
//                body("").
//                when().
//                post("/friendRequest/tester8/tester9");
//
//        int statusCode = response.getStatusCode();
//        assertEquals(200, statusCode);
//
//        response = RestAssured.given().
//                header("Content-Type", "text/plain").
//                header("charset","utf-8").
//                body("").
//                when().
//                post("/acceptFriendRequest/tester8/tester9");
//
//        statusCode = response.getStatusCode();
//        assertEquals(200, statusCode);
//
//        String returnString = response.getBody().asString();
//        try {
//            JSONObject returnObj = new JSONObject(returnString);
//            assertEquals("success", returnObj.get("message"));
//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
////----------------------------------------------------------------
//        response = RestAssured.given().
//                header("Content-Type", "text/plain").
//                header("charset","utf-8").
//                body("").
//                when().
//                get("/friends/tester8");
//
//        statusCode = response.getStatusCode();
//        assertEquals(200, statusCode);
//
//        returnString = response.getBody().asString();
//        try {
//            JSONArray returnArr = new JSONArray(returnString);
//            assertEquals("tester9", returnArr.get(returnArr.length()-1));
//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
////----------------------------------------------------------------
//        response = RestAssured.given().
//                header("Content-Type", "text/plain").
//                header("charset","utf-8").
//                body("").
//                when().
//                put("/friends/tester8/tester9");
//
//        statusCode = response.getStatusCode();
//        assertEquals(200, statusCode);
//
//        returnString = response.getBody().asString();
//        try {
//            JSONObject returnObj = new JSONObject(returnString);
//            assertEquals("success", returnObj.get("message"));
//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}
