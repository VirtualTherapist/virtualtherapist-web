import controllers.LoginController;
import models.Answer;
import models.Question;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.android.library.Logger;
import play.mvc.Http;
import play.mvc.Http.*;
import play.twirl.api.Content;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static play.test.Helpers.*;

/**
 * Created by wahid on 10/10/14.
 */
public class TemplateTest {

    private Http.Context context;
    private Http.Flash flash;
    private Http.Session session;

    @Test
    public void loginTest() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                //setup HTTP Context
                context = Mockito.mock(Http.Context.class);
                //mocking flash session, request, etc... as required
                flash  = Mockito.mock(Http.Flash.class);
                when(context.flash()).thenReturn(flash);
                Http.Context.current.set(context);

                //run your test
                Content html = views.html.login.render("Login", "login");
                assertThat(contentType(html)).isEqualTo("text/html");
                assertThat(contentAsString(html)).contains("Login");
            }
        });
    }

    @Test
    public void registerTest() {
        running(fakeApplication(), new Runnable(){
            public void run(){
                //setup HTTP Context
                context = Mockito.mock(Http.Context.class);
                //mocking flash session, request, etc... as required
                flash  = Mockito.mock(Http.Flash.class);
                when(context.flash()).thenReturn(flash);
                Http.Context.current.set(context);

                //run your test
                Content html = views.html.register.render("Poo", "poo");
                assertThat(contentType(html)).isEqualTo("text/html");
                assertThat(contentAsString(html)).contains("Poo");
                assertThat(contentAsString(html)).contains("poo");
            }
        });
    }

    @Test
    public void indexTest() {
        running(fakeApplication(), new Runnable(){
            public void run(){
                //setup HTTP Context
                context = Mockito.mock(Http.Context.class);
                //mocking flash session, request, etc... as required
                session = Mockito.mock(Session.class);
                when(context.session()).thenReturn(session);
                Http.Context.current.set(context);


                //run your test
                Content html = views.html.index.render("poo");
//                assertThat(contentType(html)).isEqualTo("text/html");
//                assertThat(contentAsString(html)).contains("");
            }
        });
    }

//    @Test
    public void analysisTest(){
        running(fakeApplication(), new Runnable(){
            public void run(){
                //setup HTTP Context
                context = Mockito.mock(Http.Context.class);
                //mocking flash session, request, etc... as required
                flash  = Mockito.mock(Http.Flash.class);
                when(context.flash()).thenReturn(flash);
                Http.Context.current.set(context);
                //run your test
                Content html = views.html.index.render("Poo");
                assertThat(contentType(html)).isEqualTo("text/html");
                assertThat(contentAsString(html)).contains("Poo");
            }
        });
    }

//    @Test
    public void questionsTest(){
        running(fakeApplication(), new Runnable(){
            public void run(){
                //setup HTTP Context
                context = Mockito.mock(Http.Context.class);
                //mocking flash session, request, etc... as required
                flash  = Mockito.mock(Http.Flash.class);
                when(context.flash()).thenReturn(flash);
                Http.Context.current.set(context);

                List<Question> testQuestions = new ArrayList<Question>();
                List<Answer> testAnswers = new ArrayList<Answer>();

                //run your test
                Content html = views.html.questions.render(testAnswers,testQuestions);
                assertThat(contentType(html)).isEqualTo("text/html");
                assertThat(contentAsString(html)).isNotEmpty();
            }
        });
    }
}
