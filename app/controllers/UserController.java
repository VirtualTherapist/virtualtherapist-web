package controllers;

import com.avaje.ebean.Ebean;
import models.User;
import play.api.libs.Crypto;
import play.mvc.Controller;
import play.mvc.Result;
import play.data.DynamicForm;
import utils.HashUtil;
import java.util.List;
import views.html.*;

import static play.data.Form.form;
import static play.libs.Json.toJson;

/**
 * Created by bas on 9-10-14.
 */
public class UserController extends Controller
{

    public static Result showUsers()
    {
        List<User> allUsers = Ebean.find(User.class).findList();

        return ok(users.render(allUsers, Crypto.decryptAES(session(Crypto.encryptAES("firstname"))), Crypto.decryptAES(session(Crypto.encryptAES("lastname"))), "", ""));
    }

    public static Result userPage(Integer id)
    {
        User user = Ebean.find(User.class, id);
        return ok(userdetail.render(user, Crypto.decryptAES(session(Crypto.encryptAES("firstname"))), Crypto.decryptAES(session(Crypto.encryptAES("lastname"))), "", ""));
    }

    public static Result deleteUser(Integer id)
    {
        Ebean.find(User.class, id).delete();
        List<User> allUsers = Ebean.find(User.class).findList();
        return redirect(controllers.routes.UserController.showUsers());
    }

    public static Result showProfile()
    {
        User user= Ebean.find(User.class).where().eq("email", Crypto.decryptAES(session(Crypto.encryptAES("email")))).findUnique();
        return ok(profile.render(user, Crypto.decryptAES(session(Crypto.encryptAES("firstname"))), Crypto.decryptAES(session(Crypto.encryptAES("lastname"))), "", ""));
    }

    public static Result saveProfile()
    {
        DynamicForm userForm = form().bindFromRequest();

        String email = Crypto.decryptAES(session(Crypto.encryptAES("email")));

        User user= Ebean.find(User.class).where().eq("email", email).findUnique();
        user.first_name = userForm.get("first_name");
        user.last_name = userForm.get("last_name");

        String password = userForm.get("password");
        if(!password.isEmpty()){
            String hash = HashUtil.createHash(email, userForm.get("password"));
            user.password = hash;
        }

        user.save();

        return redirect(controllers.routes.UserController.showProfile());
    }


}
