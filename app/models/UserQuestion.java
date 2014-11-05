package models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Akatchi on 9-10-2014.
 */
@Entity
public class UserQuestion extends Model
{
    @Id
    public int id;

    @ManyToOne(optional=true)
    public User user;

    @ManyToOne(cascade=CascadeType.ALL)
    public List<UserQuestionKeyword> userQuestionKeywordList;

    @Constraints.Required
    public String asked_question;

    @Column(name="created_at")
    @CreatedTimestamp
    public Date createdAt;
}
