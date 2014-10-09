package controllers;

import com.avaje.ebean.Ebean;
import filters.SessionFilter;
import models.Answer;
import models.Question;
import models.User;
import play.*;
import play.api.libs.Crypto;
import play.api.libs.json.JsPath;
import play.data.DynamicForm;
import play.mvc.*;

import views.html.*;

import java.util.List;

import static play.data.Form.form;
import static play.libs.Json.toJson;

@With(SessionFilter.class)
public class ApplicationController extends Controller
{

    public static Result homepage()
    {
        return ok(index.render(""));
    }

    public static Result showUsers()
    {
        List<User> allUsers = Ebean.find(User.class).findList();

        return ok(users.render(allUsers));
    }


    public static Result getUser(Integer id)
    {
        User user = Ebean.find(User.class, id);
        return ok(toJson(user));
    }

}
