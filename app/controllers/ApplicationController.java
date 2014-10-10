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
        return ok(index.render("", Crypto.decryptAES(session(Crypto.encryptAES("firstname"))), Crypto.decryptAES(session(Crypto.encryptAES("lastname")))));
    }


}
