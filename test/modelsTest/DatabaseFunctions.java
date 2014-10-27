package modelsTest;

import models.User;
import models.UserRole;
import models.Question;

/**
 * Created by Akatchi on 21-10-2014.
 */
public class DatabaseFunctions
{
    public static UserRole addAdminRole()
    {
        UserRole role = new UserRole();
            role.level = 10;
            role.id= 2;
            role.name  = "Super Admin";
        role.save();

        return role;
    }

    public static User addUser(UserRole role)
    {
        User user = new User();
            user.email = "user@therapist.com";
            user.first_name = "User";
            user.last_name = "Name";
            user.password = play.libs.Crypto.encryptAES("password");
            user.role = role;
        user.save();

        return user;
    }

    public static void deleteUser(User user)
    {
        user.delete();
    }

    public static Question addQuestion()
    {
        Question question = new Question();
        question.question = "Is this a question?";

        question.save();
        return question;
    }
}
