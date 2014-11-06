package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * User model,
 */
@Entity
public class User extends Model
{
    @Id
    public int id;

    @Constraints.Required
    public String first_name;

    @Constraints.Required
    public String last_name;

    @Constraints.Required
    @Constraints.Email
    public String email;

    @Constraints.Required
    public String password;

    @ManyToOne
    public UserRole role;

    @OneToMany(cascade=CascadeType.REMOVE)
    public List<UserQuestion> userQuestionList;
}
