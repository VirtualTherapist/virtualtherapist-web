package controllers;


import com.avaje.ebean.Ebean;
import models.User;
import play.api.libs.Crypto;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

/**
 * Created by bas on 9-10-14.
 */
public class AnalysisController extends Controller {

    public static Result analysisPageForUser(Integer userId) {
        User user = null;
        if(userId != null) {
            user = Ebean.find(User.class, userId);
        }
        return ok(analysis.render("Analyse", user,
                Crypto.decryptAES(session(Crypto.encryptAES("firstname"))),
                Crypto.decryptAES(session(Crypto.encryptAES("lastname"))), "", ""));
        //return ok();
    }

    public static Result analysisPage() {
        //return ok();
        return ok(analysis.render("Analyse", null,
                Crypto.decryptAES(session(Crypto.encryptAES("firstname"))),
                Crypto.decryptAES(session(Crypto.encryptAES("lastname"))), "", ""));
    }

    //public static Result keywordTrendPage() {
        //return ok(keywordtrends.render("Keyword Trends",
                //Crypto.decryptAES(session(Crypto.encryptAES("firstname"))),
                //Crypto.decryptAES(session(Crypto.encryptAES("lastname"))), "", ""));
    //}

}
