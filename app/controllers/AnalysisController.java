package controllers;


import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.api.libs.Crypto;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import scala.util.parsing.json.JSONObject;
import scala.util.parsing.json.JSONObject$;
import utils.KeywordUsage;
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

        List<Chat> chats = Ebean.find(Chat.class).where().eq("user", user).findList();

        // Iterate over all chats and count amount of chat lines and amount
        // of unanswered questions.
        for (Chat chat : chats) {
            amountOfQuestions += chat.chatlines.size();

            for (ChatLine chatline : chat.chatlines) {
                if (chatline.answer.equals(noAnswer))  {
                    amountOfUnansweredQuestions++;
                }
            }
        }

        return ok(analysis.render("Analyse", null,
                Crypto.decryptAES(session(Crypto.encryptAES("firstname"))),
                Crypto.decryptAES(session(Crypto.encryptAES("lastname"))), "", "",
                amountOfQuestions, amountOfUnansweredQuestions,
                getKeywordUsageFromUser(userId),
                getTrendingAnswersFromUser(userId)
                ));
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
        int amountOfUnAnsweredQuestions = 0;

        if (noAnswer != null) {
            amountOfUnAnsweredQuestions = query.eq("answer", noAnswer).findRowCount();
        }

        return ok(analysis.render("Analyse", null,
                Crypto.decryptAES(session(Crypto.encryptAES("firstname"))),
                Crypto.decryptAES(session(Crypto.encryptAES("lastname"))), "", "",
                amountOfQuestions, amountOfUnAnsweredQuestions,
                getKeywordUsage(),
                getTrendingAnswers()
                ));
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
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("app/exports/" + filename), "UTF-8"));
            bw.write(dataLine);
            bw.newLine();
            bw.flush();
            bw.close();
        }
        catch( Exception e ){}

        File downloadMe = new File("app/exports/" + filename);

        return ok(downloadMe);
    }

    private static Map<Answer, Integer> getTrendingAnswers()
    {
        Map<Answer, Integer> toReturn = new HashMap<Answer, Integer>();

        for( ChatLine item : Ebean.find(ChatLine.class).findList() )
        {
            Answer answer = item.answer;
            if( toReturn.containsKey(answer) ){ toReturn.put(answer, toReturn.get(answer) + 1); }
            else                              { toReturn.put(answer, 1); }
        }

        return sortByValue(toReturn);
    }

    private static Map<Answer, Integer> getTrendingAnswersFromUser(int userId)
    {
        Map<Answer, Integer> toReturn = new HashMap<Answer, Integer>();

        for( ChatLine item : Ebean.find(ChatLine.class).findList() )
        {
            if (item.chat.user.id == userId)
            {
                Answer answer = item.answer;
                if( toReturn.containsKey(answer) ){ toReturn.put(answer, toReturn.get(answer) + 1); }
                else                              { toReturn.put(answer, 1); }
            }
        }

        return sortByValue(toReturn);
    }

    private static Map<Keyword, Integer> getKeywordUsage()
    {
        Map<Keyword, Integer> toReturn = new HashMap<Keyword, Integer>();

        for( UserQuestionKeyword item : Ebean.find(UserQuestionKeyword.class).findList() )
        {
            Keyword keyword = item.keywordCategory.keyword;
            if(! keyword.keyword.equals("?")) {
                if( toReturn.containsKey(keyword) ){ toReturn.put(keyword, toReturn.get(keyword) + 1); }
                else                               { toReturn.put(keyword, 1); }
            }

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

    public static Result keywordUsage() {

        Map<String, String[]> params = request().body().asFormUrlEncoded();
        Keyword keyword = Ebean.find(Keyword.class).where().eq("keyword", params.get("keyword")[0]).findUnique();

        System.out.println(keyword.keyword);

        ArrayList<KeywordUsage> keywordUsages = new ArrayList<>();

        for( UserQuestionKeyword item : Ebean.find(UserQuestionKeyword.class).findList() )
        {
            Keyword usedKeyword = item.keywordCategory.keyword;
            if(keyword.keyword.equals(usedKeyword.keyword)) {
                Date createdAt = item.userquestion.createdAt;
                int foundDateIndex = -1;
                if((foundDateIndex = listContaintsDate(keywordUsages, createdAt)) == -1) {
                    //keywordMap.put(Long.toString(createdAt.getTime()), 1);
                    KeywordUsage usage = new KeywordUsage();
                    usage.time = createdAt.getTime();
                    usage.usage = 1;
                    keywordUsages.add(usage);
                }else{
                    //keywordMap.put(foundDate, keywordMap.get(foundDate) +1);
                    keywordUsages.get(foundDateIndex).usage += 1;
                }
            }

        }

        /*
        Date date1 = new Date();
        Date date2 = new Date();
        Date date3 = new Date();

        Integer int1 = new Integer(1);
        Integer int2 = new Integer(2);
        Integer int3 = new Integer(4);

        TreeMap<Date, Integer> map = new TreeMap();
        map.put(date1, int1);
        map.put(date2, int2);
        map.put(date3, int3);

                    ['Dag', 'Snap', 'Game', 'Motivatie'],
            ['1',  1,      4,       10],
            ['2',  5,      4,       10],
            ['3',  5,       11,     12],
            ['4',  14,      4,      11],
            ['5',  12,      4,      10],
            ['6',  11,       12,    9],
            ['7',  12,      4,      8]

        */

        return ok(toJson(keywordUsages));
    }

    private static int listContaintsDate(ArrayList<KeywordUsage> keywordUsages, Date date) {
        Calendar cal = Calendar.getInstance();
        for(KeywordUsage usage : keywordUsages) {
            long time = usage.time;
            cal.setTime(new Date(time));
            int keyDay = cal.get(Calendar.DAY_OF_MONTH);
            int keyMonth = cal.get(Calendar.MONTH);
            int keyYear = cal.get(Calendar.YEAR);

            cal.setTime(date);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);

            if(keyDay == day && keyMonth == month && keyYear == year) {
                return keywordUsages.indexOf(usage);
            }
        }
        return -1;
    }


}
