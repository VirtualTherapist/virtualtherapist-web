package modelsTest;

import com.avaje.ebean.Ebean;
import models.User;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static play.test.Helpers.*;

import models.UserRole;
import org.junit.Before;
import org.junit.Test;
import play.test.WithApplication;
import play.test.FakeApplication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by wahid on 10/10/14.
 */
public class UserTest
{
    private FakeApplication fakeApp;

    @Before
    public void setupDatabase()
    {
        Map<String, String> map = new HashMap<>();
            map.put("db.default.driver", "org.h2.Driver");
            map.put("db.default.url", "jdbc:h2:mem:play");

        fakeApp = fakeApplication(map);
    }

    /*
     * Test om te testen of er users gemaakt kunnen worden
     */
    @Test
    public void testSaveUser()
    {
        running(fakeApp, new Runnable()
        {
            public void run()
            {
                UserRole adminRole = DatabaseFunctions.addAdminRole();

                int usersToMake = 5;
                for( int i = 0; i < usersToMake; i++ )
                {
                    DatabaseFunctions.addUser(adminRole);
                }

                List<User> createdUsers = Ebean.find(User.class).findList();

                assertEquals(createdUsers.size(), usersToMake);
            }
        });

    }
}
