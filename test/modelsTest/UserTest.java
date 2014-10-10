package modelsTest;

import models.User;
import models.UserRole;
import static org.fest.assertions.Assertions.*;
import org.junit.Test;
import play.api.libs.Crypto;


/**
 * Created by wahid on 10/10/14.
 */
public class UserTest extends BaseModelTest {
    @Test
    public void save() {
        super.createCleanDb();

        User user = new User();
        user.email      = "test@test.nl";
        user.first_name = "Test";
        user.last_name  = "Tset";
        user.password   = Crypto.encryptAES("test");

        UserRole role = new UserRole();
        role.level  = 10;
        role.name   = "admin";
        role.save();

        user.role = role;
        user.save();


        assertThat(user.id).isNotNull();
    }
}
