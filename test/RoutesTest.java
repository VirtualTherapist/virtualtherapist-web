import org.junit.Test;
import org.junit.Ignore;
import play.mvc.Result;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.assertThat;

 /**
 * Created by wahid on 10/10/14.
 */
public class RoutesTest {

    
    // List with all URLS 
    private List<String> getUrls = Arrays.asList(
            "/vragen",
            "/vragen/all",
            "/vragen/delete/1",
            "/antwoord/delete/1",
            "/login",
            "/register",
            "/logout"
            //"/analyse"                // Not Implemented
    );

    /** 
     * Iterate over list with urls and perform a fake HTTP GET request on each
     * URL, checking if response is not null.
     */
    @Test
    public void getUrlTest() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                for (String url: getUrls) {
                    Result result = route(fakeRequest(GET, url));
                    assertThat(result).isNotNull();
                }
            }
        });
    }

    @Test
    @Ignore("Not implemented.")
    public void getUserTest() {
       running(fakeApplication(), new Runnable() {
           public void run() {
               Result result = route(fakeRequest(GET, "/gebruikers/all"));
               assertThat(result).isNotNull();
           }
        });
    }

    @Test
    @Ignore("Not implemented.")
    public void postUserTest() {
       running(fakeApplication(), new Runnable() {
           public void run() {
              Result result = route(fakeRequest(GET, "/gebruikers/1"));
              assertThat(result).isNotNull();
           }
        });
    }

    @Test
    public void postQuestionTest() {
       running(fakeApplication(), new Runnable() {
           public void run() {
              Result result = route(fakeRequest(POST, "/vragen"));
              assertThat(result).isNotNull();
           }
        });
    }

    @Test
    public void postSaveQuestionTest() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                Map<String, String> map = new HashMap<>();
                map.put("question", "Is this a question?");
                map.put("answer", "This this is anwer.");


                Result result = route(fakeRequest(POST, "/vragen/opslaan").withFormUrlEncodedBody(map));
                assertThat(result).isNotNull();
            }
        });
    }
}
