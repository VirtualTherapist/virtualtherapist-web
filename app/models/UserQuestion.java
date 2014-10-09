package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Akatchi on 9-10-2014.
 */
@Entity
public class UserQuestion extends Model
{
    @Id
    public int id;

    @Constraints.Required
    public User userid;

    @Constraints.Required
    public String question;

    public long timestamp;

    public UserQuestion(String question, User userid)
    {
        this.question = question;
        this.userid = userid;
        this.timestamp = System.currentTimeMillis();
    }
}
