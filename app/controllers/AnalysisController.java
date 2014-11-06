package controllers;


import com.avaje.ebean.Ebean;
import models.*;
import play.api.libs.Crypto;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

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
        List<Chat> chats = Ebean.find(Chat.class)
            .where()
            .eq("user", user)
            .findList();

        // Iterate over all chats and count amount of chat lines and amount
        // of unanswered questions.
        for (Chat chat : chats) {
            amountOfQuestions += chat.chatlines.size();

            for (ChatLine chatline : chat.chatlines) {
                if (chatline.answer == noAnswer) {
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
        int amountOfQuestions = Ebean.find(ChatLine.class).findRowCount();
        int amountOfUnansweredQuestions = 0;

        if (noAnswer != null) {
            int amountOfUnAnsweredQuestions = Ebean.find(ChatLine.class)
                .where()
                .eq("answer_id", noAnswer.id)
                .findRowCount();
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

        for( QuestionKeyword item : Ebean.find(QuestionKeyword.class).findList() )
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
            if( item.userquestion.user.id == userId )
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
