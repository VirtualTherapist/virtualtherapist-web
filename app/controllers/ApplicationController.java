package controllers;

import com.avaje.ebean.Ebean;
import models.User;
import play.*;
import play.api.libs.json.JsPath;
import play.mvc.*;

import views.html.*;

import java.util.List;

import static play.libs.Json.toJson;

public class ApplicationController extends Controller {

    public static Result homepage()
    {
        if( session("loggedin") != null && session("loggedin").equals("true") )
        {
            return ok(index.render(""));
        }
        else
        {
            return redirect("/login");
        }
    }

    public static Result getUser(Integer id)
    {
        User user = Ebean.find(User.class, id);
        return ok(toJson(user));
    }

    public static Result showUsers()
    {
        if( session("loggedin") != null && session("loggedin").equals("true") )
        {
            List<User> allUsers = Ebean.find(User.class).findList();

            return ok(users.render(allUsers));
        }
        else
        {
            return redirect("/login");
        }
    }

}
