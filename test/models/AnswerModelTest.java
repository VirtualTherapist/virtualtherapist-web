package models;

import com.avaje.ebean.Ebean;
import helpers.DatabaseFunctionsHelper;
import helpers.DatabaseHelper;
import org.junit.Test;
import play.test.FakeApplication;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.running;

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

}
