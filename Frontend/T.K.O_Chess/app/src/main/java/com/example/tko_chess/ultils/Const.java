package com.example.tko_chess.ultils;

public class Const {

    public static final String URL_SERVER_AN5 = "http://coms-309-005.class.las.iastate.edu:8080";

    public static final String URL_SERVER_USERS = "http://coms-309-005.class.las.iastate.edu:8080/users";

    public static final String URL_SERVER_LOGIN = "http://coms-309-005.class.las.iastate.edu:8080/users/login";

    public static final String URL_SERVER_CHANGEUSERNAME = "http://coms-309-005.class.las.iastate.edu:8080/users/name/";

    public static final String URL_SERVER_CHANGEPASSWORD = "http://coms-309-005.class.las.iastate.edu:8080/users/password/";

    //Concatenate desired User's username
    public static final String URL_SERVER_GETUSER = "http://coms-309-005.class.las.iastate.edu:8080/users/getByName/";

    //This URL can be used to GET the friends list, and also to REMOVE a friend from the list.
    //For GET, concatenate currUser's Username. For REMOVE, concatenate currUser's username and the friend being removed
    public static final String URL_SERVER_FRIENDSLIST = "http://coms-309-005.class.las.iastate.edu:8080/friends/";

    //Concatenate User's username
    public static final String URL_SERVER_PENDINGFRIENDSREQUEST = "http://coms-309-005.class.las.iastate.edu:8080/friendsRequests/outgoing/";

    //Concatenate User's username
    public static final String URL_SERVER_INCOMINGFRIENDSREQUEST = "http://coms-309-005.class.las.iastate.edu:8080/friendsRequests/incoming/";
}
