package controllers;

import com.avaje.ebean.Ebean;
import filters.SessionFilter;
import models.Answer;
import models.Question;
import models.User;
import play.Logger;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import views.html.questions;

import java.util.List;

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

        return ok(questions.render(allAnswers));
    }

    public static Result showQuestions()
    {
        List<Answer> allAnswers = Ebean.find(Answer.class).findList();

        return ok(toJson(allAnswers));
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

    public static Result deleteQuestion(Integer id)
    {
        Ebean.find(Answer.class, id).delete();

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
