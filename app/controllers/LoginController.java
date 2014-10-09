package controllers;

import com.avaje.ebean.Ebean;
import filters.LoginFilter;
import models.User;
import play.Logger;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Crypto;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import views.html.index;
import views.html.login;
import views.html.register;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static play.data.Form.form;

/**
 * Created by Akatchi on 8-10-2014.
 */
public class LoginController extends Controller
{
    @With(LoginFilter.class)
    public static Result login()
    {
        session().clear();
        return ok(login.render("Login", ""));
    }

    @With(LoginFilter.class)
    public static Result register()
    {
        return ok(register.render("Registreer", ""));
    }

    public static Result logout()
    {
        session().clear();

        return redirect("/login");
    }

    @With(LoginFilter.class)
    public static Result validateLogin()
    {
        DynamicForm loginForm = form().bindFromRequest();
        User user             = new User();
        user.email            = loginForm.get("email");
        user.password         = loginForm.get("password");

        List<User> userList = Ebean.find(User.class).where()
                                                        .eq("email", user.email)
                                                        .eq("password", Crypto.encryptAES(user.password)).findList();
        if( userList.size() == 1 )
        {
            createSession(user.email);

            return redirect("/");
        }
        else
        {
            return ok(login.render("Login pagina", "Error: Foutieve email & wachtwoord combinatie"));
        }
    }

    @With(LoginFilter.class)
    public static Result createUser()
    {
        Form<User> registerForm = form(User.class).bindFromRequest();
        User user = registerForm.get();

        user.password = Crypto.encryptAES(user.password);

        Ebean.beginTransaction();
        user.save();
        Ebean.commitTransaction();
        Ebean.endTransaction();

        createSession(user.email);

        return redirect("/");
    }

    public static void createSession(String email)
    {
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date()); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, Integer.valueOf(Play.application().configuration().getString("session.expererationtime")));
        cal.getTime();

        session(Crypto.encryptAES("expiretime"), Crypto.encryptAES(String.valueOf(cal.getTimeInMillis())));
        session(Crypto.encryptAES("loggedin"), Crypto.encryptAES("true"));
        session(Crypto.encryptAES("email"), Crypto.encryptAES(email));
    }
}
