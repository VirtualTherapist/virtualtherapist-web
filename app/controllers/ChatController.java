package controllers;

import play.api.libs.Crypto;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.chatlist;
import views.html.userchat;

//import views.html

/**
 * Created by bas on 9-10-14.
 */
public class ChatController extends Controller {

    public static Result chatList()
    {
        return ok(userchat.render("Analyse", null, Crypto.decryptAES(session(Crypto.encryptAES("firstname"))),
                Crypto.decryptAES(session(Crypto.encryptAES("lastname"))), "", ""));
    }

    public static Result showChat(Integer id)
    {
        return ok(chatlist.render("Analyse", null, Crypto.decryptAES(session(Crypto.encryptAES("firstname"))),
                Crypto.decryptAES(session(Crypto.encryptAES("lastname"))), "", ""));
    }
}

