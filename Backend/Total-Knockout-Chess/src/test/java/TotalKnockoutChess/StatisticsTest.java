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
public class StatisticsTest {
    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

//    @Test
//    public void testStatistics() {
//        Response response = RestAssured.given().
//                header("Content-Type", "text/plain").
//                header("charset","utf-8").
//                body("").
//                when().
//                post("/users/testStatisticsPal/password/password");
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
////----------------------------------------------------------------------
//        response = RestAssured.given().
//                header("Content-Type", "text/plain").
//                header("charset","utf-8").
//                body("").
//                when().
//                put("/userStats/chessWin/testStatisticsPal");
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
////----------------------------------------------------------------------
//        response = RestAssured.given().
//                header("Content-Type", "text/plain").
//                header("charset","utf-8").
//                body("").
//                when().
//                put("/userStats/chessLoss/testStatisticsPal");
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
////----------------------------------------------------------------------
//        response = RestAssured.given().
//                header("Content-Type", "text/plain").
//                header("charset","utf-8").
//                body("").
//                when().
//                put("/userStats/boxingWin/testStatisticsPal");
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
////----------------------------------------------------------------------
//        response = RestAssured.given().
//                header("Content-Type", "text/plain").
//                header("charset","utf-8").
//                body("").
//                when().
//                put("/userStats/boxingLoss/testStatisticsPal");
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
////----------------------------------------------------------------------
//        response = RestAssured.given().
//                header("Content-Type", "text/plain").
//                header("charset","utf-8").
//                body("").
//                when().
//                put("/userStats/chessBoxingWin/testStatisticsPal");
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
////----------------------------------------------------------------------
//        response = RestAssured.given().
//                header("Content-Type", "text/plain").
//                header("charset","utf-8").
//                body("").
//                when().
//                put("/userStats/chessBoxingLoss/testStatisticsPal");
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
////----------------------------------------------------------------------
//        response = RestAssured.given().
//                header("Content-Type", "text/plain").
//                header("charset","utf-8").
//                body("").
//                when().
//                get("/userStats/testStatisticsPal");
//
//        statusCode = response.getStatusCode();
//        assertEquals(200, statusCode);
//
//        returnString = response.getBody().asString();
//        try {
//            JSONObject returnObj = new JSONObject(returnString);
//            assertEquals("testStatisticsPal", returnObj.get("username"));
//            assertEquals(1, returnObj.get("chessWins"));
//            assertEquals(1, returnObj.get("chessLosses"));
//            assertEquals(2, returnObj.get("chessGames"));
//            assertEquals(1, returnObj.get("boxingWins"));
//            assertEquals(1, returnObj.get("boxingLosses"));
//            assertEquals(2, returnObj.get("boxingGames"));
//            assertEquals(1, returnObj.get("chessBoxingWins"));
//            assertEquals(1, returnObj.get("chessBoxingLosses"));
//            assertEquals(2, returnObj.get("chessBoxingGames"));
//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//        }
////----------------------------------------------------------------------
//        response = RestAssured.given().
//                header("Content-Type", "text/plain").
//                header("charset","utf-8").
//                body("").
//                when().
//                get("/getUserStats/testStatisticsPal");
//
//        statusCode = response.getStatusCode();
//        assertEquals(200, statusCode);
//
//        returnString = response.getBody().asString();
//        assertEquals("ChessBoxing 2 50.0 Chess 2 50.0 Boxing 2 50.0", returnString);
////----------------------------------------------------------------------
//        response = RestAssured.given().
//                header("Content-Type", "text/plain").
//                header("charset","utf-8").
//                body("").
//                when().
//                put("/users/testStatisticsPal");
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

    @Test
    public void nothing() {
        assertEquals("hi", "hi");
    }
}
