package controllers;


import com.avaje.ebean.Ebean;
import models.ChatLine;
import models.User;
import models.ChatLine;
import models.Answer;
import play.api.libs.Crypto;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class AnalysisController extends Controller {

    // Every question retrieves an Answer object as response. Even those questions
    // without suitable answer. These questions get response "Geen antwoord gevonden".
    // This variable contains that answer.
    private static Answer noAnswer = Ebean.find(Answer.class)
            .where()
            .eq("answer", "Geen antwoord gevonden")
            .findUnique();
    
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
        int amountOfUnansweredQuestions = 0;

        if (noAnswer != null) {
            int amountOfUnAnsweredQuestions = Ebean.find(ChatLine.class)
                .where()
                .eq("answer_id", noAnswer.id)
                .findRowCount();
        }

        //return ok();
        return ok(analysis.render("Analyse", null,
                Crypto.decryptAES(session(Crypto.encryptAES("firstname"))),
                Crypto.decryptAES(session(Crypto.encryptAES("lastname"))), "", "",
                amountOfQuestions, amountOfUnansweredQuestions));
    }

    public static Result exportChat()
    {
        String filename      = "chat_export.txt";

        String dataLine = "";

        for( ChatLine item : Ebean.find(ChatLine.class).findList())
        {
            dataLine += item.userQuestion.asked_question + "\n" + item.answer.answer + "\n";
        }

        try
        {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("app/exports" + filename), "UTF-8"));
            bw.write(dataLine);
            bw.newLine();
            bw.flush();
            bw.close();
        }
        catch( Exception e ){}

        File downloadMe = new File("app/exports" + filename);

        return ok(downloadMe);
    }

    //public static Result keywordTrendPage() {
        //return ok(keywordtrends.render("Keyword Trends",
                //Crypto.decryptAES(session(Crypto.encryptAES("firstname"))),
                //Crypto.decryptAES(session(Crypto.encryptAES("lastname"))), "", ""));
    //}
}
