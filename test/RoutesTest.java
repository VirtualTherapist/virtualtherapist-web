import org.junit.Test;
import org.junit.Ignore;
import play.mvc.Result;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;

 /**
 * Created by wahid on 10/10/14.
 */
public class RoutesTest 
{
    // List wich of URLs which are accessable using the HTTP GET method.
    private List<String> getUrls = Arrays.asList(
            "/vragen",
            "/vragen/all",
            "/vragen/delete/1",
            "/antwoord/delete/1",
            "/login",
            "/register",
            "/logout",
            "/antwoord/delete/1"

            // These URLs are defined in con/routes but lack implementation.
            //"/analyse"               
            //"/gebruikers/all"
            //"/gebruikers/1"
    );

    /**
     * Test if HTTP GET request on url returns result. 
     *
     * @param url       URL without the protocol and server, e.g.: /route or /vragen/all. 
     */
    public void testGetRoute(String url) {
        final String url_ = url;

        running(fakeApplication(), new Runnable() {
            public void run() {
                Result result = route(fakeRequest(GET, url_));
                assertThat(result).isNotNull();
            }
        });
    }

    /**
     * Test if HTTP POST request on url returns result. 
     *
     * @param url   URL without the protocol and server, e.g.: /route or /vragen/all. 
     * @param data  A Map with key values representing form data.    
     */
    public void testPostRoute(String url, Map data) {
        final String url_ = url;
        final Map data_ = data;

        running(fakeApplication(), new Runnable() {
            public void run() {
                Result result = route(fakeRequest(POST, url_).withFormUrlEncodedBody(data_));
                assertThat(result).isNotNull();
            }
        });
    }

    /** 
     * Iterate over list with urls and perform a fake HTTP GET request on each
     * URL, checking if response is not null.
     */
    @Test
    public void getUrlsTest() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                for (String url: getUrls) {
                    testGetRoute(url);
                }
            }
        });
    }

    @Test
    public void postQuestionTest() {
        testPostRoute("/vragen", new HashMap<>());
    }

    @Test
    public void postSaveQuestionTest() {
        Map<String, String> data = new HashMap<>();
        data.put("question", "Is this a question?");
        data.put("answer", "This this is anwer.");

        testPostRoute("/vragen/opslaan", data);
    }

    @Test
    public void postUpdateAnwserTest() {
        Map<String, String> data= new HashMap<>();
        data.put("value", "42");

        testPostRoute("/antwoord/update", data);
    }

    @Test 
    @Ignore
    public void postLoginTest() {
        Map<String, String> data = new HashMap<>();
        data.put("email", "user@server.com");
        data.put("password", "42");

        testPostRoute("/login", data);
    }
    
    @Test 
    public void postRegisterTest() {
        Map<String, String> data = new HashMap<>();
        data.put("first_name", "John");
        data.put("last_name", "Doe");
        data.put("email", "user@server.com");
        data.put("password", "42");

        testPostRoute("/register", data);
    }
}
