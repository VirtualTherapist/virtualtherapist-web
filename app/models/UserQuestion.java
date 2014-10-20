package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by Akatchi on 9-10-2014.
 */
@Entity
public class UserQuestion extends Model
{
    @Id
    public int id;

    @Constraints.Required
    @ManyToOne
    public User user;

    @Constraints.Required
    public String asked_question;

    public long created_at;

    public UserQuestion(String asked_question, User user)
    {
        this.asked_question = asked_question;
        this.user = user;
        this.created_at = System.currentTimeMillis();
    }
}
