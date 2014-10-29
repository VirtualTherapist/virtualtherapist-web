package models;

import com.avaje.ebean.Ebean;

import static junit.framework.TestCase.assertEquals;
import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;
import helpers.DatabaseFunctionsHelper;
import helpers.DatabaseHelper;
import org.junit.Test;
import java.util.List;
/**
 * UserModelTest, tests the user model.
 */
public class UserModelTest extends DatabaseHelper
{
    @Test
    public void testSaveUser()
    {
        running(fakeApp, new Runnable() {
            public void run() {
                UserRole adminRole = DatabaseFunctionsHelper.addAdminRole();

                int usersToMake = 5;
                for( int i = 0; i < usersToMake; i++ ) {
                    DatabaseFunctionsHelper.addUser(adminRole);
                }

                List<User> createdUsers = Ebean.find(User.class).findList();

                assertEquals(createdUsers.size(), usersToMake);
            }
        });
    }

    @Test
    public void testDeleteUser()
    {
        running(fakeApp, new Runnable() {
           public void run() {
               UserRole userRole = DatabaseFunctionsHelper.addUserRole();

               User regularUser = DatabaseFunctionsHelper.addUserWithInput("test@test.nl", "Tester", "Test", "test", userRole);

               DatabaseFunctionsHelper.deleteUser(regularUser);

               // Try to fetch the user
               assertThat(Ebean.find(User.class, regularUser.id)).isNull();
           }
        });
    }
}
