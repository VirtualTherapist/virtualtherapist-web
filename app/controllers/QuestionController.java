package controllers;

import com.avaje.ebean.Ebean;
import filters.SessionFilter;
import models.Answer;
import models.Question;
import play.Logger;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import views.html.questions;

import java.util.*;

import static play.data.Form.form;
import static play.libs.Json.toJson;

/**
 * Created by Akatchi on 9-10-2014.
 */
@With(SessionFilter.class)
public class QuestionController extends Controller
{

    public static Result questionpage()
    {
        List<Answer> allAnswers = Ebean.find(Answer.class).findList();

        return ok(questions.render(allAnswers, null));
    }

    public static Result showQuestions()
    {
        Set<Answer> allAnswers = Ebean.find(Answer.class).findSet();

        return ok(toJson(allAnswers));
    }

    public static Result searchQuestion()
    {
        Map<String, String[]> parameters = request().body().asFormUrlEncoded();
        String search = (String) parameters.get("search")[0];
        //Collection<String[]> answer = parameters.values();
        //DynamicForm searchForm        = form().bindFromRequest();
        //HashSet om dubbele waardes te voorkomen
        /*Set<Object> returnData       = new HashSet<Object>();
        Set<Answer> foundAnswers     = new HashSet<Answer>();
        Set<Question> foundQuestions = new HashSet<Question>();

        if( searchForm.get("answer") != "" )
        {
            foundAnswers = Ebean.find(Answer.class).where().contains("answer", searchForm.get("answer")).findSet();
            foundAnswers.addAll(Ebean.find(Answer.class).where().like("answer", searchForm.get("answer")).findSet());
            foundAnswers.addAll(Ebean.find(Answer.class).where().eq("answer", searchForm.get("answer")).findSet());

            for( Answer ans : foundAnswers ){ returnData.add(ans); }
        }
        if( searchForm.get("question") != "" )
        {
            foundQuestions = Ebean.find(Question.class).where().contains("question", searchForm.get("question")).findSet();
            foundQuestions.addAll(Ebean.find(Question.class).where().like("question", searchForm.get("question")).findSet());
            foundQuestions.addAll(Ebean.find(Question.class).where().eq("question", searchForm.get("question")).findSet());

            for( Question question : foundQuestions ){ returnData.add(question); }
        }*/
        return ok(search);
        //return ok(toJson(returnData));
//        return ok(questions.render(new ArrayList<Answer>(foundAnswers), new ArrayList<Question>(foundQuestions)));
    }

    public static Result saveQuestion()
    {
        DynamicForm aqForm    = form().bindFromRequest();
        Answer answer         = new Answer();
        Question question     = new Question();

        answer.answer = aqForm.get("answer");
        question.question = aqForm.get("question");
        question.save();

        answer.question = question;
        answer.save();

        return questionpage();
    }

    public static Result deleteAnswer(Integer id)
    {
        Ebean.find(Answer.class, id).delete();

        return questionpage();
    }

    public static Result deleteQuestion(Integer id)
    {
        Ebean.find(Question.class, id).delete();

        return questionpage();
    }

    public static Result updateQuestion()
    {
        DynamicForm questionForm    = form().bindFromRequest();

        Question q = Ebean.find(Question.class, questionForm.get("pk"));
        q.question = questionForm.get("value");
        q.save();

        return questionpage();
    }

    public static Result updateAnswer()
    {
        DynamicForm answerForm    = form().bindFromRequest();

        Logger.debug("Updating answer");

        Answer a = Ebean.find(Answer.class, answerForm.get("pk"));
        a.answer = answerForm.get("value");
        a.save();

        return questionpage();
    }
}
