package controllersTest;

import com.avaje.ebean.Ebean;
import controllers.UserController;
import controllers.routes;
import models.User;
import models.UserRole;
import modelsTest.DatabaseFunctions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Computer;
import play.mvc.Http;
import play.mvc.Result;
import play.test.FakeApplication;
import play.test.FakeRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static play.test.Helpers.*;

/**
 * Created by Akatchi on 21-10-2014.
 */
public class UserTest
{
    private final Http.Request request = mock(Http.Request.class);
    private FakeApplication fakeApp;

    @Before
    public void setupDatabase()
    {
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

    /*
     * Test om te kijken of het mogelijk is om users te deleten
     */
    @Test
    public void testDeleteUser()
    {
        running(fakeApp, new Runnable()
        {
            public void run()
            {
                UserRole adminRole   = DatabaseFunctions.addAdminRole();
                User newUser         = DatabaseFunctions.addUser(adminRole);
                DatabaseFunctions.addUser(adminRole);

                User createdUser = Ebean.find(User.class, newUser.id);
                assertThat(createdUser).isNotNull();

                callAction(routes.ref.UserController.deleteUser(newUser.id));
                createdUser = Ebean.find(User.class, newUser.id);
                assertThat(createdUser).isNull();
            }
        });
    }
}
