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
import utils.HashUtil;
import utils.NLPUtil;

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

    @Test public void testHash() {
        String hash = HashUtil.createHash("helderbas@gmail.com", "Bas1991");
        assertThat(hash).isEqualTo("cae352dbfc4552aca527f49211d6dedd09924e50cd64e73a800fb65806f2f477");
    }

    @Test
    public void testNLPMessageTagging() {
        /*
        Token:	Dit			    Tag:	Pron
        Token:	is			    Tag:	V
        Token:	een			    Tag:	Art
        Token:	testbericht		Tag:	N
        Token:	.			    Tag:	Punc
        Token:	Hiermee			Tag:	Adv
        Token:	wordt			Tag:	V
        Token:	de			    Tag:	Art
        Token:	tag			    Tag:	N
        Token:	functie			Tag:	N
        Token:	getest			Tag:	V
        Token:	.			    Tag:	Punc
         */
        String message = "Dit is een testbericht. Hiermee wordt de tag functie getest.";
        TreeMap<String, String> treeMap1 = new TreeMap();
        treeMap1.put("Dit", "Pron");
        treeMap1.put("is", "V");
        treeMap1.put("een", "Art");
        treeMap1.put("testbericht", "N");
        treeMap1.put(".", "Punc");

        TreeMap<String, String> treeMap2 = new TreeMap();
        treeMap2.put("Hiermee", "Adv");
        treeMap2.put("wordt", "V");
        treeMap2.put("de", "Art");
        treeMap2.put("tag", "N");
        treeMap2.put("functie", "N");
        treeMap2.put("getest", "V");
        treeMap2.put(".", "Punc");

        SortedMap<String, String>[] expectedMaps = new SortedMap[]{treeMap1, treeMap2};

        SortedMap<String, String>[] maps = NLPUtil.getInstance().tagMessage(message);
        assertThat(maps).isNotEqualTo(null);
        assertThat(maps).isEqualTo(expectedMaps);
    }
}
