package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created by Akatchi on 8-10-2014.
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

}
