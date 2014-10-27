package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wordnik.swagger.annotations.*;
import filters.APIAuthHeaderFilter;
import models.*;
import play.Logger;
import play.api.libs.json.Json;
import play.libs.F;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.WebSocket;
import play.mvc.With;
import scala.util.parsing.json.JSONArray;
import scala.util.parsing.json.JSONObject;
import utils.NLPUtil;

import java.io.ObjectInputStream;
import java.util.*;

import static play.libs.Json.toJson;
import static play.mvc.Controller.request;
import static play.mvc.Results.*;

/**
 * Created by Akatchi on 10-10-2014.
 */
@Api(value = "/api", description = "API operations", basePath = "http://localhost:9000/api")
public class APIController extends SwaggerBaseApiController
{
    @ApiOperation(nickname = "ValidateLogin", value="ValidateLogin", notes = "Validates app login", response = User.class, httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Login valid"),
            @ApiResponse(code = 401, message = "Unauthorized")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authentication", value = "sha256(Email + password)", required = true, dataType = "string", paramType = "header")})
    @With(APIAuthHeaderFilter.class)
    public static Result validateLogin()
    {
        String secret = request().getHeader("authentication");
        User user = Ebean.find(User.class).where().eq("password", secret).findUnique();

        if (user != null)
        {
            //ObjectNode result = Json.newObject();
            //result.put("first_name", user.first_name);
            //result.put("last_name", user.last_name);
            //result.put("status", 200);
            return ok(toJson(user));
        }
        else
        {
            return unauthorized();
        }


    }
    /*
    @With(APIAuthHeaderFilter.class)
    public static play.mvc.Result message() {

        JsonNode jsonNode = request().body().asJson();
        if(jsonNode == null) {
            return badRequest("Expecting JSON");
        }else{
            String message = jsonNode.findPath("message").textValue();
            if(message == null) {
                return badRequest("Missing parameter [message]");
            }else{
                System.out.println(message);
                SortedMap<String, String>[] tokensAndTags = NLPUtil.getInstance().tagMessage(message);
                return ok();
            }
        }
    }*/

    @ApiOperation(nickname = "CreateChatWithContext", value="CreateChatWithContext", notes = "Creates a chat and adds te context", response = Integer.class, httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Chat and context created"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 400, message = "Missing mood variable")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mood", value = "User mood", required = true, dataType = "string", paramType = "post"),
            @ApiImplicitParam(name = "lat", value = "User location lat", required = false, dataType = "double", paramType = "post"),
            @ApiImplicitParam(name = "lng", value = "User location lng", required = false, dataType = "double", paramType = "post")})
    @With(APIAuthHeaderFilter.class)
    public static Result setChatContext() {
        String secret = request().getHeader("authentication");
        User user = Ebean.find(User.class).where().eq("password", secret).findUnique();

        Map<String, String[]> postVariables = request().body().asFormUrlEncoded();
        if(postVariables == null || !postVariables.containsKey("mood"))
            return badRequest("Missing mood variable");

        String mood = postVariables.get("mood")[0];
        String lat = null;
        String lng = null;
        if(postVariables.containsKey("lat") && postVariables.containsKey("lng")) {
            lat = postVariables.get("lat")[0];
            lng = postVariables.get("lng")[0];
        }

        Chat chat = new Chat();
        chat.user = user;
        if(lat != null && lng != null) {
            chat.lat = Double.parseDouble(lat);
            chat.lng = Double.parseDouble(lng);
        }else{
            chat.lat = 0;
            chat.lng = 0;
        }
        chat.mood = mood;
        chat.save();

        if(chat != null)
            return created(toJson(chat.id));

        return internalServerError();
    }

    private static Map<String, String> parseJson(String s){

        Map<String,String> map = new HashMap<String,String>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(s,
                    new TypeReference<HashMap<String,String>>(){});
            Logger.debug("poep" +map);
        } catch (Exception e){
            e.printStackTrace();
        }

        return map;
    }

    @ApiOperation(nickname = "WebSocket", value="", notes = "Returns chat websocket", response = WebSocket.class, httpMethod = "GET")
    public static WebSocket<String> WebSocket()
    {
        NLPUtil tokenizer = NLPUtil.getInstance();

        return new WebSocket<String>()
        {
            // Called when the Websocket Handshake is done.
            public void onReady(WebSocket.In<String> in, final WebSocket.Out<String> out)
            {
                // For each event received on the socket,
                in.onMessage(new F.Callback<String>()
                {
                    public void invoke(String event)
                    {
                        // parse the json string from websocket into a map
                        Map<String, String> data = parseJson(event);
                        Logger.debug("incomming: "+event);
                        if(data.containsKey("token")){ // check for the token
                            if(APIAuthHeaderFilter.authenticate(data.get("token"))){ // see if the token is valid.
                                if( data.containsKey("question") ) // see if there's a question to be checked
                                {
//                                    String user_email = data.get("email"); // fetch the user mail
//                                    User user = Ebean.find(User.class).where().eq("email", user_email).findUnique();
//                                    Logger.debug("user: "+user.email);
//                                    Chat userChat = Ebean.find(Chat.class).where().eq("user", user).orderBy("createdAt, createdAt desc").findList().get(0); // get the latest room
//
//                                    String question = data.get("question"); // fetch the question
////                                    final SortedMap<String, String>[] tokens = tokenizer.tagMessage(question);
//                                    Logger.debug("user: "+user.email);
//                                    storeChat(user, question, tokens); // store everything that's being said
//
//
////                                    Logger.debug("Tokens: "+tokens[0]);
////                                    Logger.debug("Vraag: " + );
//
//                                    //Code om de beste question uit te kiezen
//                                    //Hier moet straks het hele keyword zoeken gebeuren maar dit is evne voor he gemaakt gedaan
//                                    List<Question> theQuestion = Ebean.find(Question.class).where().eq("question", question).findList();
//
//                                    Answer bestAnswer = new Answer();
//
//                                    //Code om als er geen questoin gevonden is in ieder geval een standaard antwoord terug te sturen
//                                    if(theQuestion.size() == 0){ bestAnswer.answer = "Geen antwoord gevonden"; }
//                                    else { bestAnswer.answer = theQuestion.get(0).answer.answer; }
//
//                                    Logger.debug("Antwoord: " + bestAnswer.answer);
//
//                                    //Het antwoord terugsturen naar de client
//                                    out.write(bestAnswer.answer);
                                }
                            }
                        }
                    }
                });

                // When the socket is closed.
                in.onClose(new F.Callback0()
                {
                    public void invoke()
                    {
                        Logger.debug("Disconnected");
                    }
                });
            }
        };
    }

    /**
     *
     * @param user
     * @param question
     * @param keywords
     */
    private static void storeChat(User user, String question, SortedMap<String, String>[] keywords){
        // Store the userquestion
        UserQuestion q = new UserQuestion();
        q.asked_question = question;
        q.user = user;
        q.save();

        // store keywords
        for(SortedMap<String, String> map : keywords){
            for(Map.Entry<String, String> entry : map.entrySet()){
                Logger.debug("key: " + entry.getKey() + " Value: " + entry.getValue());
            }
        }

    }


}