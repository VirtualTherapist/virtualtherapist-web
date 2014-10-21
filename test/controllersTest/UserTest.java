
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
import test.BaseTest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static play.test.Helpers.*;

/**
 * Created by Akatchi on 21-10-2014.
 */
public class UserTest extends BaseTest 
{
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
