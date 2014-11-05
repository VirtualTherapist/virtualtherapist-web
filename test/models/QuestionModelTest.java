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
                Question question   = DatabaseFunctionsHelper.createQuestion("Test vraag", null, null);

                assertThat(question).isNotNull();
            }
        });
    }

    @Test
    public void deleteQuestionModelTest() {
        running(fakeApp, new Runnable() {
            public void run(){
                Question question   = DatabaseFunctionsHelper.createQuestion("Test vraag", null, null);

                DatabaseFunctionsHelper.deleteQuestion(question);

                assertThat(Ebean.find(Question.class, question.id)).isNull();
            }
        });
    }

    @Test
    public void deleteQuestionCheckAnswerTest(){
        running(fakeApp, new Runnable() {
            public void run() {
                Answer answer       = DatabaseFunctionsHelper.createAnswer("Test Antwoord");
                Question question   = DatabaseFunctionsHelper.createQuestion("Test vraag", answer, null);

                DatabaseFunctionsHelper.deleteQuestion(question); // delete just the question

                assertThat(Ebean.find(Question.class, question.id)).isNull(); // question deleted
                assertThat(Ebean.find(Answer.class, answer.id)).isNull(); // answer deleted too
            }
        });
    }

    @Test
    public void deleteUserCheckQuestionTest(){
        running(fakeApp, new Runnable(){
            public void run(){
                Answer answer       = DatabaseFunctionsHelper.createAnswer("Test Antwoord");
                UserRole userRole   = DatabaseFunctionsHelper.addAdminRole();
                User user           = DatabaseFunctionsHelper.addUser(userRole);
                Question question   = DatabaseFunctionsHelper.createQuestion("Test vraag", answer, user);

                DatabaseFunctionsHelper.deleteUser(user);

                assertThat(Ebean.find(User.class, user.id)).isNull();
                assertThat(Ebean.find(Question.class, question.id)).isNotNull(); // or i kill you
                assertThat(Ebean.find(Answer.class, answer.id)).isNotNull();
            }
        });
    }

}