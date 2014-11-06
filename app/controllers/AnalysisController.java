package controllers;


import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import models.*;
import play.api.libs.Crypto;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.*;

import org.joda.time.DateTime;

import static play.libs.Json.toJson;

public class AnalysisController extends Controller {

    // Every question retrieves an Answer object as response. Even those questions
    // without suitable answer. These questions get response "Geen antwoord gevonden".
    // This variable contains that answer.
    private static Answer noAnswer = Ebean.find(Answer.class)
            .where()
            .eq("answer", "Geen antwoord gevonden")
            .findUnique();

    /**
     * Returns a view with analysis for of chats of a user.
     */
    public static Result analysisPageForUser(Integer userId) {
        User user = Ebean.find(User.class, userId);
        if (user == null) {
            return notFound("User not found.");
        }
        
        int amountOfQuestions = 0;
        int amountOfUnansweredQuestions = 0;

        ExpressionList query = Ebean.find(Chat.class).where().eq("user", user);

        String fromTS =request().getQueryString("from");
        String toTS=request().getQueryString("to");

        if (fromTS != null) {
            DateTime from = new DateTime(Long.parseLong(fromTS));
            query = query.gt("createdAt", from);
        }

        if (toTS!= null) {
            DateTime to = new DateTime(Long.parseLong(toTS));
            query = query.lt("createdAt", to);
        }

        List<Chat> chats = Ebean.find(Chat.class).findList();

        // Iterate over all chats and count amount of chat lines and amount
        // of unanswered questions.
        for (Chat chat : chats) {
            amountOfQuestions += chat.chatlines.size();

            for (ChatLine chatline : chat.chatlines) {
                if (chatline.answer == noAnswer)  {
                    amountOfUnansweredQuestions++;
                }
            }
        }

        return ok(analysis.render("Analyse", null,
                Crypto.decryptAES(session(Crypto.encryptAES("firstname"))),
                Crypto.decryptAES(session(Crypto.encryptAES("lastname"))), "", "",
                amountOfQuestions, amountOfUnansweredQuestions,
                getKeywordUsageFromUser(userId)));
    }

    /**
     * Returns a view with analysis for for chats of all users.
     */
    public static Result analysisPage() {
        ExpressionList query = Ebean.find(ChatLine.class).where();

        String fromTS =request().getQueryString("from");
        String toTS=request().getQueryString("to");

        if (fromTS != null) {
            DateTime from = new DateTime(Long.parseLong(fromTS));
            query = query.gt("createdAt", from);
        }

        if (toTS!= null) {
            DateTime to = new DateTime(Long.parseLong(toTS));
            query = query.lt("createdAt", to);
        }

        int amountOfQuestions = query.findRowCount();
        int amountOfUnansweredQuestions = 0;

        if (noAnswer != null) {
            int amountOfUnAnsweredQuestions = query.eq("answer_id", noAnswer.id).findRowCount();
        }

        return ok(analysis.render("Analyse", null,
                Crypto.decryptAES(session(Crypto.encryptAES("firstname"))),
                Crypto.decryptAES(session(Crypto.encryptAES("lastname"))), "", "",
                amountOfQuestions, amountOfUnansweredQuestions,
                getKeywordUsage()));
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

    private static Map<Keyword, Integer> getKeywordUsage()
    {
        Map<Keyword, Integer> toReturn = new HashMap<Keyword, Integer>();

        for( UserQuestionKeyword item : Ebean.find(UserQuestionKeyword.class).findList() )
        {
            Keyword keyword = item.keywordCategory.keyword;
            if( toReturn.containsKey(keyword) ){ toReturn.put(keyword, toReturn.get(keyword) + 1); }
            else                               { toReturn.put(keyword, 1); }
        }

        return sortByValue(toReturn);
    }

    private static Map<Keyword, Integer> getKeywordUsageFromUser(int userId)
    {
        Map<Keyword, Integer> toReturn = new HashMap<Keyword, Integer>();

        for( UserQuestionKeyword item : Ebean.find(UserQuestionKeyword.class).findList() )
        {
            if (item.userquestion.user.id == userId)
            {
                Keyword keyword = item.keywordCategory.keyword;
                if (toReturn.containsKey(keyword)) { toReturn.put(keyword, toReturn.get(keyword) + 1); }
                else { toReturn.put(keyword, 1); }
            }
        }

        return sortByValue(toReturn);
    }

    //Method om een map op values te sorteren ( aangezien dit niet ondersteund wordt )
    private static Map sortByValue(Map unsortMap)
    {
        List list = new LinkedList(unsortMap.entrySet());

        Collections.sort(list, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        Map sortedMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
}
