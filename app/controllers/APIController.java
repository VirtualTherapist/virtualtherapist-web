package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import filters.APIAuthHeaderFilter;
import models.Answer;
import models.User;
import play.libs.Crypto;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import utils.HashUtil;
import utils.NLPUtil;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import static play.libs.Json.toJson;
import static play.mvc.Controller.request;
import static play.mvc.Results.badRequest;
import static play.mvc.Results.ok;
import static play.mvc.Results.unauthorized;

/**
 * Created by Akatchi on 10-10-2014.
 */
@Api(value = "/api", description = "API operations")
public class APIController extends SwaggerBaseApiController
{
    @ApiOperation(nickname = "validateLogin", value="", notes = "Validates app login", response = JsonNode.class, httpMethod = "GET")
    //@ApiResponses(new Array(new ApiResponse(code = 401, message = "Unauthorized"), new ApiResponse(code = 200, message = "Login validated")))
    @With(APIAuthHeaderFilter.class)
    public static Result validateLogin()
    {
        String secret = request().getHeader("authentication");
        User user = Ebean.find(User.class).where().eq("password", secret).findUnique();

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
    }


}