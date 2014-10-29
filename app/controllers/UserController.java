package controllers;

import com.avaje.ebean.Ebean;
import models.Chat;
import models.User;
import play.api.libs.Crypto;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.userdetail;
import views.html.users;

import java.util.List;

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
        List<Chat> chats = Ebean.find(Chat.class)
                .where()
                .eq("user_id", user.id)
                .findList();

        return ok(userdetail.render(chats, user, Crypto.decryptAES(session(Crypto.encryptAES("firstname"))), Crypto.decryptAES(session(Crypto.encryptAES("lastname"))), "", ""));
    }

    public static Result deleteUser(Integer id)
    {
        Ebean.find(User.class, id).delete();
        List<User> allUsers = Ebean.find(User.class).findList();
        return redirect("/gebruikers/all");
    }
}
