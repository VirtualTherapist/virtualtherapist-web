package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wordnik.swagger.annotations.*;
import filters.APIAuthHeaderFilter;
import models.Answer;
import models.Chat;
import models.ChatContext;
import models.Question;
import models.User;
import play.Logger;
import play.libs.Crypto;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import play.mvc.With;
import utils.HashUtil;
import utils.NLPUtil;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

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

    public static Result setChatContext() {
        String secret = request().getHeader("authentication");
        User user = Ebean.find(User.class, secret);

        Map<String, String[]> postVariables = request().body().asFormUrlEncoded();
        String mood = postVariables.get("mood")[0];
        String lat = postVariables.get("lat")[0];
        String lng = postVariables.get("lng")[0];

        Chat chat = new Chat();
        chat.user = user;
        chat.lat = Double.parseDouble(lat);
        chat.lng = Double.parseDouble(lng);
        chat.mood = mood;
        chat.save();

        /*
        ChatContext chatContext = new ChatContext();
        chatContext.mood = mood;
        chatContext.lat = Double.parseDouble(lat);
        chatContext.lng = Double.parseDouble(lng);
        */

        return ok();
    }

    @ApiOperation(nickname = "WebSocket", value="", notes = "Returns chat websocket", response = WebSocket.class, httpMethod = "GET")
    public static WebSocket<String> WebSocket()
    {
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
                        // Log events to the console
                        if( event.contains("[question]") )
                        {
                            String[] question = event.split("\\[question\\]");
                            Logger.debug("Vraag: " + question[1]);

                            //Code om de beste question uit te kiezen
                            //Hier moet straks het hele keyword zoeken gebeuren maar dit is evne voor he gemaakt gedaan
                            List<Question> theQuestion = Ebean.find(Question.class).where().eq("question", question[1]).findList();

                            Answer bestAnswer = new Answer();

                            //Code om als er geen questoin gevonden is in ieder geval een standaard antwoord terug te sturen
                            if(theQuestion.size() == 0){ bestAnswer.answer = "Geen antwoord gevonden"; }
                            else { bestAnswer.answer = theQuestion.get(0).answer.answer; }

                            Logger.debug("Antwoord: " + bestAnswer.answer);

                            //Het antwoord terugsturen naar de client
                            out.write(bestAnswer.answer);
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

                // Send a single 'Hello!' message
//                out.write("Connected");
            }
        };
    }


}