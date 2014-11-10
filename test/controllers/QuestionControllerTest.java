package controllers;

import com.avaje.ebean.Ebean;
import helpers.DatabaseFunctionsHelper;
import helpers.DatabaseHelper;
import org.junit.Test;
import models.Question;
import play.test.FakeRequest;
import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

public class QuestionControllerTest extends DatabaseHelper
{
//    @Test
//    public void saveQuestionTest()
//    {
//        running(fakeApp, new Runnable()
//        {
//            public void run()
//            {
////                Question question = DatabaseFunctionsHelper.createQuestion("Test vraag?", null, null);
//                Map<String, String> data = new HashMap<>();
//                data.put("answer", "Dit is een antwoord");
//                data.put("question", "Is dit een vraag?");
//
//                FakeRequest request = new FakeRequest().withFormUrlEncodedBody(data);
//                callAction(routes.ref.QuestionController.saveQuestion(), request);
////                callAction(routes.ref.QuestionController.deleteQuestion(question.id));
//                assertThat(Ebean.find(Question.class).where().contains("question", "Is dit een vraag?")).isNotNull();
//            }
//        });
//    }

//    @Test
//    public void deleteQuestioTest(){
////        Question question = DatabaseFunctionsHelper.createQuestion("Test vraag?", null, null);
//        running(fakeApp, new Runnable(){
//            public void run(){
//                Map<String, String> data = new HashMap<>();
//                data.put("answer", "Dit is een antwoord");
//                data.put("question", "Is dit een vraag?");
//
//                FakeRequest request = new FakeRequest().withFormUrlEncodedBody(data);
//                fakeRequest(routes.ref.QuestionController.saveQuestion(), "answer");
//
////                Ebean.commitTransaction();
////
////                Question question = Ebean.find(Question.class).where().contains("question", "Is dit een vraag?").findUnique();
////                assertThat(question).isNotNull();
//
////                Question question = Ebean.find(Question.class).where().eq("question", "Is dit een vraag?").findUnique();
////                assertThat(Ebean.find(Question.class).where().eq("question", "Is dit een vraag?").findUnique()).isNotNull();
////
////                callAction(routes.ref.QuestionController.deleteQuestion(question.id));
////
////                assertThat(Ebean.find(Question.class).where().contains("question", "Is dit een vraag?")).isNull();
//
//            }
//        });
//    }
//    public void testUpdateQuestion()
//    {
//        running(fakeApp, new Runnable()
//        {
//            public void run()
//            {
//                Question question = DatabaseFunctionsHelper.createQuestion("Is dit een vraag?", null, null);
//
//                Map<String, String> data = new HashMap<>();
//                data.put("pk", Integer.toString(question.id));
//                data.put("value", "Has this question changed?");
//
//                FakeRequest request = new FakeRequest().withFormUrlEncodedBody(data);
//                callAction(routes.ref.QuestionController.updateQuestion(question.id), request);
//
//                question = Ebean.find(Question.class, question.id);
//                assertThat(question.question).isEqualTo("Has this question changed?");
//            }
//        });
//    }
}
