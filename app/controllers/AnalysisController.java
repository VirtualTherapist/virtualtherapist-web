package controllers;


import com.avaje.ebean.Ebean;
import models.User;
import models.ChatLine;
import models.Answer;
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
                Crypto.decryptAES(session(Crypto.encryptAES("lastname"))), "", "",
                0, 0));
        //return ok();

    }

    public static Result analysisPage() {
        int amountOfQuestions = Ebean.find(ChatLine.class).findRowCount();
        Answer answer = Ebean.find(Answer.class)
            .where()
            .eq("answer", "Geen antwoord gevonden")
            .findUnique();

        int amountOfUnansweredQuestions = 0;
        if (answer != null) {
            int amountOfUnAnsweredQuestions = Ebean.find(ChatLine.class)
                .where()
                .eq("answer_id", answer.id)
                .findRowCount();
        }

        //return ok();
        return ok(analysis.render("Analyse", null,
                Crypto.decryptAES(session(Crypto.encryptAES("firstname"))),
                Crypto.decryptAES(session(Crypto.encryptAES("lastname"))), "", "",
                amountOfQuestions, amountOfUnansweredQuestions));
    }

    //public static Result keywordTrendPage() {
        //return ok(keywordtrends.render("Keyword Trends",
                //Crypto.decryptAES(session(Crypto.encryptAES("firstname"))),
                //Crypto.decryptAES(session(Crypto.encryptAES("lastname"))), "", ""));
    //}

}
