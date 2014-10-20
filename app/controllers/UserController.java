package controllers;

import com.avaje.ebean.Ebean;
import models.User;
import play.api.libs.Crypto;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import views.html.*;

/**
 * Created by bas on 9-10-14.
 */
public class UserController extends Controller {

    public static Result showUsers()
    {
        List<User> allUsers = Ebean.find(User.class).findList();

        return ok(users.render(allUsers, Crypto.decryptAES(session(Crypto.encryptAES("firstname"))), Crypto.decryptAES(session(Crypto.encryptAES("lastname"))), "", ""));
        //return ok();
    }

    public static Result userPage(Integer id) {
        User user = Ebean.find(User.class, id);
        return ok(userdetail.render(user, Crypto.decryptAES(session(Crypto.encryptAES("firstname"))), Crypto.decryptAES(session(Crypto.encryptAES("lastname"))), "", ""));
        //return ok();
    }
}
