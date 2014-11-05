import com.avaje.ebean.PagingList;
import models.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import play.mvc.Http;
import play.twirl.api.Content;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
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
                PagingList<Question> testQuestions = Mockito.mock(PagingList.class);
                List<Question> wtfQuestions = Mockito.mock(List.class);
                //run your test
                int count = 10;
                Content html = views.html.questions.render(testQuestions, wtfQuestions, count, "Voornaam", "Achternaam", "testError", "Test");
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
               List<Chat> testChats;
//               for()
//               Content html = views.html.userdetail.render(testChat, testUser, "Voornaam", "Achternaam", "testError", "Test");
//               assertThat(contentType(html)).isEqualTo("text/html");
//               assertThat(contentAsString(html)).contains("Voornaam");
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
