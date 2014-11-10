package controllersTest;

import com.avaje.ebean.Ebean;
import controllers.routes;
import helpers.DatabaseFunctionsHelper;
import helpers.DatabaseHelper;
import models.User;
import models.UserRole;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static play.test.Helpers.*;

/**
 * Created by Akatchi on 21-10-2014.
 */
public class UserTest extends DatabaseHelper
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
                UserRole adminRole   = DatabaseFunctionsHelper.addAdminRole();
                User newUser         = DatabaseFunctionsHelper.addUser(adminRole);
                DatabaseFunctionsHelper.addUser(adminRole);

                User createdUser = Ebean.find(User.class, newUser.id);
                assertThat(createdUser).isNotNull();

                callAction(routes.ref.UserController.deleteUser(newUser.id));
                createdUser = Ebean.find(User.class, newUser.id);
                assertThat(createdUser).isNull();
            }
        });
    }
}
