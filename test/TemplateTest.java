import com.avaje.ebean.Ebean;
import controllers.LoginController;
import models.Answer;
import models.Question;
import models.User;
import models.UserRole;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.android.library.Logger;
import play.db.ebean.Model;
import play.mvc.Http;
import play.mvc.Http.*;
import play.twirl.api.Content;
import play.twirl.api.Html;
import views.html.analysis;
import views.html.userdetail;

import java.util.*;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.test.Helpers.*;

/**
 * Created by wahid on 10/10/14.
 */
public class TemplateTest {

    private Http.Context context;
    private Http.Flash flash;
    private Http.Session session;

    private final Http.Request request = mock(Http.Request.class);

    @Before
    public void setUp() throws Exception {
        Map<String, String> flashData = Collections.emptyMap();
        Map<String, Object> argData = Collections.emptyMap();
        Long id = 2L;
        play.api.mvc.RequestHeader header = mock(play.api.mvc.RequestHeader.class);
        Http.Context context = new Http.Context(id, header, request, flashData, flashData, argData);
        Http.Context.current.set(context);
    }

    @Test
    public void loginTest() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                Content html = views.html.login.render("Login", "");
                assertThat(contentType(html)).isEqualTo("text/html");
                assertThat(contentAsString(html)).contains("Login");
            }
        });
    }

    @Test
    public void registerTest() {
        running(fakeApplication(), new Runnable(){
            public void run(){
                List<UserRole> testRoles = new LinkedList<UserRole>();
                Content html = views.html.register.render("Registreer", "", testRoles);
                assertThat(contentType(html)).isEqualTo("text/html");
                assertThat(contentAsString(html)).contains("Registreer");
            }
        });
    }

    @Test
    public void indexTest() {
        running(fakeApplication(), new Runnable(){
            public void run(){
                //run your test
                //null object is de lijst met userroles die gevraagd wordt op die pagina
                Content html = views.html.index.render("Title", "Test", "Voornaam", "Achternaam", "testError", "Test");
                assertThat(contentType(html)).isEqualTo("text/html");
                assertThat(contentAsString(html)).contains("Title");
            }
        });
    }

    @Test
    public void analysisTest(){
        running(fakeApplication(), new Runnable(){
            public void run(){
                User testUser = Mockito.mock(User.class);
                Content html = views.html.analysis.render("Title", testUser, "Voornaam", "Achternaam", "testError", "Test");
                assertThat(contentType(html)).isEqualTo("text/html");
                assertThat(contentAsString(html)).contains("Title");
            }
        });
    }

    @Test
    public void questionsTest(){
        running(fakeApplication(), new Runnable(){
            public void run(){
                List<Question> testQuestions = new LinkedList<Question>();
                List<Answer> testAnswers = new LinkedList<Answer>();
                //run your test
                Content html = views.html.questions.render(testQuestions, testAnswers, "Voornaam", "Achternaam", "testError", "Test");
                assertThat(contentType(html)).isEqualTo("text/html");
                assertThat(contentAsString(html)).contains("Voornaam");
            }
        });
    }

    @Test
    public void userDetailTest() {
       running(fakeApplication(), new Runnable(){
           public void run(){
               User testUser = Mockito.mock(User.class);

               Content html = views.html.userdetail.render(testUser, "Voornaam", "Achternaam", "testError", "Test");
               assertThat(contentType(html)).isEqualTo("text/html");
               assertThat(contentAsString(html)).contains("Voornaam");
           }
       });
    }

    @Test
    public void userTest() {
        running(fakeApplication(), new Runnable(){
            public void run(){
                List<User> testUsers = new LinkedList<User>();

                Content html = views.html.users.render(testUsers, "Voornaam", "Achternaam", "testError", "Test");
                assertThat(contentType(html)).isEqualTo("text/html");
                assertThat(contentAsString(html)).contains("Voornaam");
            }
        });
    }
}
