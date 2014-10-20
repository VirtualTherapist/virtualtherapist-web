package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Answer;
import models.User;
import play.libs.Crypto;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.HashUtil;
import utils.NLPUtil;

import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import static play.libs.Json.toJson;

/**
 * Created by Akatchi on 10-10-2014.
 */
public class APIController extends Controller
{
    public static Result validateLogin()
    {
        User user = null;
        String secret = request().getHeader("authentication");
        if(secret != null) {
            user = authenticate(secret);
        }

        if (user != null)
        {
            ObjectNode result = Json.newObject();
            result.put("first_name", user.first_name);
            result.put("last_name", user.last_name);
            result.put("status", 200);
            return ok(toJson(result));
        }
        else
        {
            return unauthorized();
        }


    }

    public static play.mvc.Result message() {
        String secret = request().getHeader("authentication");
        if(secret != null) {
            authenticate(secret);
        }else{
            return unauthorized();
        }

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
    }

    public static User authenticate(String secret) {
        User user = Ebean.find(User.class).where()
                .eq("password", secret).findUnique();
        return user;
    }
}