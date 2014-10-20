import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import controllers.LoginController;
import controllers.routes;
import models.Question;
import modelsTest.BaseModelTest;
import modelsTest.UserTest;
import org.junit.*;

import org.junit.runner.Computer;
import org.mockito.Answers;
import org.mockito.Mockito;
import play.Logger;
import play.Play;
import play.libs.Crypto;
import play.libs.ws.WS;
import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import play.twirl.api.Content;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ApplicationTest {

/*    @Test
    public void simpleCheck() {
        int a = 1 + 1;
        assertThat(a).isEqualTo(2);
    }

    @Test
    public void renderTemplate() {
        Content html = views.html.index.render("Your new application is ready.");
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("Your new application is ready.");
    }*/
//    @Test
//    public void testCallIndex() {
//        final Http.Context mockContext = mock(Http.Context.class);
//        running(fakeApplication(), new Runnable() {
//            public void run() {
//                Result result = callAction(
//                        routes.ref.LoginController.login(),
//                        new FakeRequest(GET, "/")
//                );
//                assertThat(status(result)).isEqualTo(OK);
//            }
//        });
//    }

//    @Test
//    public void renderTemplate() {
//        running(fakeApplication(), new Runnable() {
//            public void run() {
//
////                //setup HTTP Context
//////                context = Mockito.mock(Http.Context.class);
////
////                Result result = LoginController.login();
////                assertThat(status(result)).isEqualTo(OK);
////                assertThat(contentType(result)).isEqualTo("text/html");
////                assertThat(charset(result)).isEqualTo("utf-8");
////                assertThat(contentAsString(result)).contains("Login");
//            }
//        });

//    }
//    @Test
//    public void runInBrowser() {
//        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
//            public void invoke(TestBrowser browser) {
//
//                browser.goTo("http://localhost:3333");
//
//                assertThat(browser.url()).isEqualTo("http://localhost:3333/login");
//            }
//        });
//    }
}
