package modelsTest;

import models.User;
import models.UserRole;

/**
 * Created by Akatchi on 21-10-2014.
 */
public class DatabaseFunctions
{
    public static UserRole addAdminRole()
    {
        UserRole rol = new UserRole();
            rol.level = 10;
            rol.id= 2;
            rol.name  = "Super Admin";
        rol.save();

        return rol;
    }

    public static User addUser(UserRole rol)
    {
        User user = new User();
            user.email = "user@therapist.com";
            user.first_name = "User";
            user.last_name = "Name";
            user.password = play.libs.Crypto.encryptAES("password");
            user.role = rol;
        user.save();

        return user;
    }

    public static void deleteUser(User user)
    {
        user.delete();
    }
}
