package models;

import com.avaje.ebean.Ebean;
import helpers.DatabaseFunctionsHelper;
import helpers.DatabaseHelper;
import static org.fest.assertions.Assertions.assertThat;
import org.junit.Test;

import static play.test.Helpers.running;

/**
 * QuestionModelTest, tests the Question model
 */
public class QuestionModelTest extends DatabaseHelper {

    @Test
    public void createQuestionModelTest() {
        running(fakeApp, new Runnable(){
            public void run(){
                Answer answer       = DatabaseFunctionsHelper.createAnswer("Test antwoord");
                Question question   = DatabaseFunctionsHelper.createQuestion("Test vraag", answer);

                assertThat(answer).isNotNull();
                assertThat(question).isNotNull();
            }
        });
    }

//    @Test
//    public void deleteQuestionModelTest() {
//        running(fakeApp, new Runnable() {
//            public void run(){
//                Answer answer       = DatabaseFunctionsHelper.createAnswer("Test antwoord");
//                Question question   = DatabaseFunctionsHelper.createQuestion("Test antwoord", answer);
//
//                DatabaseFunctionsHelper.deleteQuestion(question);
//
//                assertThat(Ebean.find(Question.class, question.id)).isNull();
//            }
//        });
//    }
}