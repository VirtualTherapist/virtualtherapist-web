package helpers;

import com.avaje.ebean.Ebean;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import play.mvc.Http;
import play.test.FakeApplication;
import play.test.Helpers;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static play.test.Helpers.fakeApplication;

/**
 * DatabseHelper, sets up the database before each test.
 * Also tears it down after each test.
 */
public class DatabaseHelper {
    protected final Http.Request request = mock(Http.Request.class);
    protected FakeApplication fakeApp;

    @Before
    public void setupDatabase(){
        Map<String, String> map = new HashMap<>();
        map.put("db.default.driver", "org.h2.Driver");
        map.put("db.default.url", "jdbc:h2:mem:play");

        fakeApp = fakeApplication(map);
    }

    @Before
    public void setUp() throws Exception
    {
        Map<String, String> flashData   = Collections.emptyMap();
        Map<String, Object> argData     = Collections.emptyMap();
        Long id = 2L;
        play.api.mvc.RequestHeader header = mock(play.api.mvc.RequestHeader.class);
        Http.Context context = new Http.Context(id, header, request, flashData, flashData, argData);
        Http.Context.current.set(context);
    }

    @After
    public void stopApp() {
        Helpers.stop(fakeApp);
    }
}
