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

    //Used for sending a friend request to another user
    public static final String URL_SERVER_SENDFRIENDREQUEST = "http://coms-309-005.class.las.iastate.edu:8080/friendRequest/";

    //Used for denying a friend request sent to you, or canceling a friend request you sent to somebody.
    public static final String URL_SERVER_DELETEFRIENDREQUEST = "http://coms-309-005.class.las.iastate.edu:8080/deleteFriendRequest/";

    //Used for accepting a friend request sent to you.
    public static final String URL_SERVER_ACCEPTFRIENDREQUEST = "http://coms-309-005.class.las.iastate.edu:8080/acceptFriendRequest/";

    public static final String URL_SERVER_TKOLOBBYPASSWORD = "http://coms-309-005.class.las.iastate.edu:8080/joinLobby/";

    public static final String URL_SERVER_CHESSLOBBYPASSWORD = "http://coms-309-005.class.las.iastate.edu:8080/joinLobby/";

    public static final String URL_SERVER_TKOCHESSLOBBYPASSWORD = "http://coms-309-005.class.las.iastate.edu:8080/joinLobby/";

    public static final String URL_SERVER_CHESSLOBBY = "http://coms-309-005.class.las.iastate.edu:8080/hostLobby";

    public static final String URL_SERVER_TKOCHESSLOBBY = "http://coms-309-005.class.las.iastate.edu:8080/hostLobby";

    public static final String URL_SERVER_TKOLOBBY = "http://coms-309-005.class.las.iastate.edu:8080/hostLobby";

    //WebSocket Address
    public static final String URL_SERVER_WEBSOCKET = "ws://coms-309-005.class.las.iastate.edu:8080/websocket/";

    //Boxing Test URL
    public static final String URL_SERVER_BOXINGTEST = "http://coms-309-005.class.las.iastate.edu:8080/boxingGame/";
}
