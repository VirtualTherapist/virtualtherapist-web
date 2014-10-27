package models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

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

    @Column(name="created_at")
    @CreatedTimestamp
    public Date createdAt;
}
