package models;

import com.avaje.ebean.Ebean;
import helpers.DatabaseFunctionsHelper;
import helpers.DatabaseHelper;
import org.junit.Test;
import static play.test.Helpers.running;
import static org.fest.assertions.Assertions.assertThat;

/**
 * UserQuestionModelTest, tests the user question model with writing and deleting from the database.
 */
public class UserQuestionModelTest extends DatabaseHelper {

    @Test
    public void createUserQuestionTest(){
        running(fakeApp, new Runnable(){
            public void run(){
                UserRole role = DatabaseFunctionsHelper.addUserRole();
                User user = DatabaseFunctionsHelper.addUser(role);
                UserQuestion userQuestion = DatabaseFunctionsHelper.createUserQuestion(user, "Test vraag");

                assertThat(Ebean.find(UserQuestion.class, userQuestion.id)).isNotNull();
            }
        });
    }

    @Test
    public void deleteUserQuestionTest(){
        running(fakeApp, new Runnable(){
            public void run(){
                UserRole role = DatabaseFunctionsHelper.addUserRole();
                User user = DatabaseFunctionsHelper.addUser(role);
                UserQuestion userQuestion = DatabaseFunctionsHelper.createUserQuestion(user, "Test vraag");

                DatabaseFunctionsHelper.deleteUserQuestion(userQuestion);

                assertThat(Ebean.find(UserQuestion.class, userQuestion.id)).isNull();

            }
        });
    }

    @Test
    public void deleteUserCheckUserQuestionTest(){
        running(fakeApp, new Runnable(){
            public void run(){
                UserRole role = DatabaseFunctionsHelper.addUserRole();
                User user = DatabaseFunctionsHelper.addUser(role);
                UserQuestion userQuestion = DatabaseFunctionsHelper.createUserQuestion(user, "Test vraag");

                DatabaseFunctionsHelper.deleteUser(user);

                assertThat(Ebean.find(User.class, user.id)).isNull(); // user was just deleted
                assertThat(Ebean.find(UserQuestion.class, userQuestion.id)).isNull(); // check if the user question is removed
            }
        });
    }

    @Test
    public void deleteUserQuestionCheckUserQuestionKeywordTest(){
        running(fakeApp, new Runnable(){
            public void run(){
                UserRole role = DatabaseFunctionsHelper.addUserRole();
                User user = DatabaseFunctionsHelper.addUser(role);
                UserQuestion userQuestion = DatabaseFunctionsHelper.createUserQuestion(user, "Test vraag");

                Category c = DatabaseFunctionsHelper.createCategory("Test");
                Keyword k = DatabaseFunctionsHelper.createKeyword("Hoi");
                KeywordCategory kc = DatabaseFunctionsHelper.createKeywordCategory(k,c);
                UserQuestionKeyword userQuestionKeywordCategory = DatabaseFunctionsHelper.createUserQuestionKeywordCategory(userQuestion, kc);

                DatabaseFunctionsHelper.deleteUser(user);

                assertThat(Ebean.find(User.class, user.id)).isNull(); // user was just deleted
                assertThat(Ebean.find(UserQuestion.class, userQuestion.id)).isNull(); // check if the user question is removed
//                assertThat(Ebean.find(UserQuestionKeyword.class, userQuestionKeywordCategory.id)).isNull();
            }
        });
    }
}
