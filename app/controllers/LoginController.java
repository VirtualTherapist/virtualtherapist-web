package controllers;

import com.avaje.ebean.Ebean;
import filters.LoginFilter;
import models.User;
import models.UserRole;
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
import static play.libs.Json.toJson;

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
        DynamicForm registerForm = form().bindFromRequest();
        User user = new User();
        user.first_name       = registerForm.get("first_name");
        user.last_name        = registerForm.get("last_name");
        user.email            = registerForm.get("email");
        user.password         = Crypto.encryptAES(registerForm.get("password"));

        UserRole rol = Ebean.find(UserRole.class).where().eq("name", "Admin").findUnique();

        user.role    = rol;

        user.save();

        createSession(user.email);

        return redirect("/");
    }

    public static Result initializeDB()
    {
        UserRole rol = new UserRole();
        rol.level = 1;
        rol.name  = "Gebruiker";
        rol.save();

        rol.level = 10;
        rol.name  = "Admin";
        rol.save();

        return ok(toJson("true"));
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
