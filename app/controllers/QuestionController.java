package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.PagingList;
import filters.SessionFilter;
import models.*;
import play.Logger;
import play.api.libs.Crypto;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import utils.NLPUtil;
import views.html.questions;
import views.html.synonyms;

import java.util.*;

import static play.data.Form.form;
import static play.libs.Json.toJson;

/**
 * Created by Akatchi on 9-10-2014.
 */
@With(SessionFilter.class)
public class QuestionController extends Controller
{

    public static Result questionpage(int page)
    {
        int pageLength = 10;
        PagingList<Question> pagingList = Ebean.find(Question.class).findPagingList(pageLength);
        List<Question> allQuestions = pagingList.getPage(page).getList();
        //int i = pagingList.getPageSize();
        return ok(questions.render(pagingList, allQuestions, page, Crypto.decryptAES(session(Crypto.encryptAES("firstname"))), Crypto.decryptAES(session(Crypto.encryptAES("lastname"))), "", ""));
    }

    public static Result synonymPage(int questionId) {
        Question question = Ebean.find(Question.class, questionId);

        return ok(synonyms.render("Synonyms", Crypto.decryptAES(session(Crypto.encryptAES("firstname"))), Crypto.decryptAES(session(Crypto.encryptAES("lastname"))), "", "", question));
    }

    public static Result showQuestions()
    {


        Set<Question> allQuestions = Ebean.find(Question.class).findSet();

        return ok(toJson(allQuestions));
    }

    public static Result searchQuestion()
    {
        Map<String, String[]> parameters = request().body().asFormUrlEncoded();
        String search = (String) parameters.get("search")[0];
        boolean answerActivated =  Boolean.parseBoolean(parameters.get("answer_activated")[0]);
        boolean questionActivated = Boolean.parseBoolean(parameters.get("question_activated")[0]);
        Collection<String[]> answer = parameters.values();

        DynamicForm searchForm        = form().bindFromRequest();

        Set<Object> returnData       = new HashSet<Object>();
        Set<Answer> foundAnswers     = new HashSet<Answer>();
        Set<Question> foundQuestions = new HashSet<Question>();

//        if( searchForm.get("answer") != "" )
        for(String[] item : answer )
        {
           for( String temp : item )
           {
//               Logger.debug("data: " + temp);
               if( temp.equals("answer") )  { answerActivated = true; }
               if( temp.equals("question") ){ questionActivated = true; }
           }
        }

        if( answerActivated )
        {
//            Logger.debug("searching answer");
            foundAnswers = Ebean.find(Answer.class).where().contains("answer", search).findSet();
            foundAnswers.addAll(Ebean.find(Answer.class).where().like("answer", search).findSet());
            foundAnswers.addAll(Ebean.find(Answer.class).where().eq("answer", search).findSet());

            for( Answer ans : foundAnswers ){ returnData.add(ans); }
        }
//        if( searchForm.get("question") != "" )

        if( questionActivated )
        {
//            Logger.debug("searching question");
            foundAnswers = Ebean.find(Answer.class).where().contains("question", search).findSet();
            //foundAnswers.addAll(Ebean.find(Question.class).where().like("question", search).findSet());
            //foundAnswers.addAll(Ebean.find(Question.class).where().eq("question", search).findSet());

            for( Answer ans : foundAnswers ){ returnData.add(ans); }
        }

        return ok(toJson(returnData));
//        return ok(questions.render(new ArrayList<Question>(foundQuestions), new ArrayList<Answer>(foundAnswers), Crypto.decryptAES(session(Crypto.encryptAES("firstname"))), Crypto.decryptAES(session(Crypto.encryptAES("lastname")))));
    }

    public static Result saveQuestion()
    {
        DynamicForm aqForm    = form().bindFromRequest();
        Answer answer         = new Answer();
        Question question     = new Question();

        answer.answer = aqForm.get("answer");
        answer.save();

        question.question = aqForm.get("question");
        question.answer   = answer;
        question.save();

        saveKeywords(question);

        return questionpage(0);
    }

    private static void saveKeywords(Question question)
    {
        SortedMap<String, String>[] tokens = NLPUtil.getInstance().tagMessage(question.question);
        for(SortedMap<String, String> map : tokens)
        {
            for(Map.Entry<String, String> entry : map.entrySet())
            {
                Logger.debug("key: " + entry.getKey() + " Value: " + entry.getValue());

                Category cat = Ebean.find(Category.class).where().eq("name", entry.getValue()).findUnique();
                if( cat == null )
                {
                    cat = new Category();
                    cat.name = entry.getValue();
                    cat.save();
                }

                Keyword keyword = Ebean.find(Keyword.class).where().eq("keyword", entry.getKey()).findUnique();
                if( keyword == null )
                {
                    keyword = new Keyword();
                    keyword.keyword = entry.getKey();
                    keyword.save();
                }

                KeywordCategory keyCat = Ebean.find(KeywordCategory.class).where().eq("keyword", keyword).eq("category", cat).findUnique();
                if( keyCat == null )
                {
                    keyCat = new KeywordCategory();
                    keyCat.keyword  = keyword;
                    keyCat.category = cat;
                    keyCat.save();
                }

                QuestionKeyword link = new QuestionKeyword();
                link.question           = question;
                link.keywordCategory    = keyCat;
                link.save();
            }
        }
    }

    public static Result deleteAnswer(Integer id)
    {
        Ebean.find(Answer.class, id).delete();

        return questionpage(0);
    }

    public static Result deleteQuestion(Integer id)
    {
        Question q = Ebean.find(Question.class, id);

        List<QuestionKeyword> koppeling = Ebean.find(QuestionKeyword.class).where().eq("question", q).findList();
        for(QuestionKeyword item : koppeling){ item.delete(); }

        q.delete();

        return questionpage(0);
    }

    public static Result updateQuestion(Integer id)
    {
        DynamicForm questionForm    = form().bindFromRequest();

        Question q = Ebean.find(Question.class, id);
        q.question = questionForm.get("value");
        q.save();

        List<QuestionKeyword> koppeling = Ebean.find(QuestionKeyword.class).where().eq("question", q).findList();
        for(QuestionKeyword item : koppeling){ item.delete(); }

        saveKeywords(q);

        return questionpage(0);
    }

    public static Result updateAnswer(Integer id)
    {
        DynamicForm answerForm    = form().bindFromRequest();

        Answer a = Ebean.find(Answer.class, id);
        a.answer = answerForm.get("value");
        a.save();

        return questionpage(0);
    }

    public static Result saveSynonym() {
        DynamicForm form    = form().bindFromRequest();

        Keyword keyword = Ebean.find(Keyword.class, Integer.parseInt(form.get("keyword_id")));

        Keyword synonymKeyWord = new Keyword();
        synonymKeyWord.keyword = form.get("synonym");
        Ebean.save(synonymKeyWord);

        Synonym synonym = new Synonym();
        synonym.keyword = keyword;
        synonym.synonym = synonymKeyWord;
        synonym.save();

        keyword.synonyms.add(synonym);

        Ebean.save(keyword);

        return synonymPage(Integer.parseInt(form.get("question_id")));
    }
}
