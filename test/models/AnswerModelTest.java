package models;

import com.avaje.ebean.Ebean;
import helpers.DatabaseFunctionsHelper;
import helpers.DatabaseHelper;
import org.junit.Test;
import play.test.FakeApplication;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.running;

/**
 * AnswerModelTest, tests the answer model in different scenario.
 */
public class AnswerModelTest extends DatabaseHelper {

    @Test
    public void createAnswerModelTest(){
        running(fakeApp, new Runnable(){
            public void run(){
                Answer answer = DatabaseFunctionsHelper.createAnswer("Test Antwoord");

                assertThat(answer).isNotNull();
            }
        });
    }

    @Test
    public void deleteAnswerModelTest(){
        running(fakeApp, new Runnable(){
            public void run(){
                Answer answer = DatabaseFunctionsHelper.createAnswer("Test Antwoord");
                DatabaseFunctionsHelper.deleteAnswer(answer);

                assertThat(Ebean.find(Answer.class, answer.id)).isNull();
            }
        });
    }

    @Test
    public void deleteAnswerCheckQuestionTest(){
        running(fakeApp, new Runnable(){
            public void run(){
                Answer answer       = DatabaseFunctionsHelper.createAnswer("Test Antwoord");
                Question question   = DatabaseFunctionsHelper.createQuestion("Test Vraag", answer, null);
                DatabaseFunctionsHelper.deleteAnswer(answer); // Answer (geen fk) (error op question -> answer_id)

                assertThat(Ebean.find(Answer.class, answer.id)).isNull(); // answer gets removed
                assertThat(Ebean.find(Question.class, question.id)).isNull(); // question linked to answer gets removed
            }
        });
    }
}
