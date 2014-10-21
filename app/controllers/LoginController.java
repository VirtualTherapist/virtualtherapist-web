package controllers;

import com.avaje.ebean.Ebean;
import filters.LoginFilter;
import filters.SessionFilter;
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
import utils.HashUtil;
import views.html.index;
import views.html.login;
import views.html.register;
import views.html.users;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static play.data.Form.form;
import static play.libs.Json.toJson;

public class LoginController extends Controller
{
    @With(LoginFilter.class)
    public static Result login()
    {
        session().clear();
        return ok(login.render("Login", ""));
    }

    @With(SessionFilter.class)
    public static Result register()
    {
        List<UserRole> userRoles = Ebean.find(UserRole.class).findList();
        return ok(register.render("Registreer", "", userRoles));
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

        System.out.println(loginForm.get("password"));

        String hash = HashUtil.createHash(loginForm.get("email"), loginForm.get("password"));

        User user             = new User();
        user.email            = loginForm.get("email");
        user.password         = hash;

        System.out.println("user.password: " + user.password);

        UserRole adminRole = Ebean.find(UserRole.class).where().eq("level", "10").findUnique();

        List<User> userList = new ArrayList<User>();
        if(hash != null) {
            userList = Ebean.find(User.class).where()
                    .eq("email", user.email)
                    .eq("password", user.password).findList();
        }

        if( userList.size() == 1 )
        {
            user = userList.get(0);
            createSession(user.email, user.first_name, user.last_name);

            return redirect("/");
        }
        else if(hash == null)
        {
            return ok(login.render("Login pagina", "Error: Er is iets mis gegaan, probeer het opnieuw."));
        }
        else
        {
            // Hier komt hij ook terecht als je niet bevoegd bent om in te loggen
            // misschien hier nog een betere error afhandeling maken dat hij een andere error daarvoor geeft
            return ok(login.render("Login pagina", "Error: Foutieve email & wachtwoord combinatie"));
        }
    }

    @With(SessionFilter.class)
    public static Result createUser()
    {
        DynamicForm registerForm = form().bindFromRequest();

        String hash = HashUtil.createHash(registerForm.get("email"), registerForm.get("password"));

        User user = new User();
        user.first_name       = registerForm.get("first_name");
        user.last_name        = registerForm.get("last_name");
        user.email            = registerForm.get("email");
        user.password         = hash;

        Logger.debug(registerForm.get("userrole"));

        UserRole rol = Ebean.find(UserRole.class).where().eq("name", registerForm.get("userrole")).findUnique();

        user.role    = rol;

        user.save();

//        return redirect("/gebruikers/all");

        return ok(users.render(Ebean.find(User.class).findList(), play.api.libs.Crypto.decryptAES(session(play.api.libs.Crypto.encryptAES("firstname"))), play.api.libs.Crypto.decryptAES(session(play.api.libs.Crypto.encryptAES("lastname"))), "Gebruiker: " + user.email + " - " + user.role.name + " aangemaakt!", "success"));
    }

    public static Result initializeDB()
    {
        List<Object> objectsAdded = new ArrayList<Object>();

        UserRole rol = new UserRole();
        rol.level = 1;
        rol.id = 1;
        rol.name  = "Gebruiker";
        rol.save();
        objectsAdded.add(rol);

        rol = new UserRole();
        rol.level = 10;
        rol.id= 2;
        rol.name  = "Super Admin";
        rol.save();
        objectsAdded.add(rol);

        User admin = new User();
        admin.email = "admin@therapist.com";
        admin.first_name = "Virtual";
        admin.last_name = "Therapist";
        admin.password = HashUtil.createHash(admin.email, "password");
        admin.role = rol;
        admin.save();
        objectsAdded.add(admin);

        return ok(toJson(objectsAdded));
    }

    public static void createSession(String email, String firstname, String lastname)
    {
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(new Date()); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, Integer.valueOf(Play.application().configuration().getString("session.expererationtime")));
        cal.getTime();

        Logger.debug("email: " + email + " firstname: " + firstname + " lastname: " + lastname);

        session(Crypto.encryptAES("expiretime"), Crypto.encryptAES(String.valueOf(cal.getTimeInMillis())));
        session(Crypto.encryptAES("loggedin"), Crypto.encryptAES("true"));
        session(Crypto.encryptAES("email"), Crypto.encryptAES(email));
        session(Crypto.encryptAES("firstname"), Crypto.encryptAES(firstname));
        session(Crypto.encryptAES("lastname"), Crypto.encryptAES(lastname));
    }
}
