package controllers;

import com.avaje.ebean.Ebean;
import filters.SessionFilter;
import models.Answer;
import models.Question;
import models.User;
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
        return ok(questions.render());
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

        return ok(toJson(answer));
    }

    public static Result deleteQuestion(Integer id)
    {
        Ebean.find(Answer.class, id).delete();

        return ok(toJson("true"));
    }
}
