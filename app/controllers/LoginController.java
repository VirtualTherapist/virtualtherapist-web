package controllers;

import com.avaje.ebean.Ebean;
import models.User;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.login;
import views.html.register;

import java.util.List;

import static play.data.Form.form;

public class LoginController extends Controller
{
    public static Result login()
    {
        if( session("loggedin") != null && session("loggedin").equals("true") )
        {
            return redirect("/");
        }
        else
        {
            return ok(login.render("Login pagina", ""));
        }
    }

    public static Result register()
    {
        if( session("loggedin") != null && session("loggedin").equals("true") )
        {
            return redirect("/");
        }
        else
        {
            return ok(register.render("Registreer", ""));
        }
    }

    public static Result logout()
    {
        session().clear();

        return redirect("/login");
    }

    public static Result validateLogin()
    {
        DynamicForm loginForm = form().bindFromRequest();
        User user             = new User();
        user.email            = loginForm.get("email");
        user.password         = loginForm.get("password");

        List<User> userList = Ebean.find(User.class).where()
                                                        .eq("email", user.email)
                                                        .eq("password", user.password).findList();
        if( userList.size() == 1 )
        {
            session("loggedin", "true");
            session("email", user.email);

            return redirect("/");
        }
        else
        {
            return ok(login.render("Login pagina", "Error: Foutieve email & wachtwoord combinatie"));
        }
    }

    public static Result createUser()
    {
        Form<User> registerForm = form(User.class).bindFromRequest();
        User user = registerForm.get();

        Ebean.beginTransaction();
        user.save();
        Ebean.commitTransaction();
        Ebean.endTransaction();

        session("loggedin", "true");
        session("email", user.email);

        return redirect("/");
    }
}
