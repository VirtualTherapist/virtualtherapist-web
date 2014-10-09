package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import play.api.mvc.Result;
import play.mvc.Controller;
import scala.util.parsing.json.JSONObject$;
import utils.NLPUtil;

import java.util.SortedMap;

/**
 * Created by bas on 9-10-14.
 */
public class ChatController extends Controller {

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
