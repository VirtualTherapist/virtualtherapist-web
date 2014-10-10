package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Answer;
import models.User;
import play.libs.Crypto;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.Set;

import static play.libs.Json.toJson;

/**
 * Created by Akatchi on 10-10-2014.
 */
public class APIController extends Controller
{
    public static Result validateLogin(String username, String password)
    {
        ObjectNode result = Json.newObject();

        User user = Ebean.find(User.class).where()
                .eq("email", username)
                .eq("password", Crypto.encryptAES(password)).findUnique();

        if (user != null)
        {
            result.put("first_name", user.first_name);
            result.put("last_name", user.last_name);
            result.put("status", 200);
        }
        else
        {
            result.put("status", 404);
        }

        return ok(toJson(result));
    }
}
