package models;

import com.avaje.ebean.annotation.CreatedTimestamp;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;


/**
 * Created by bas on 23-10-14.
 */
@Entity
public class ChatLine extends Model{

    @Id
    public int id;

    @ManyToOne
    @Constraints.Required
    public Chat chat;

    @OneToOne
    @Constraints.Required
    public UserQuestion userQuestion;

    @OneToOne
    @Constraints.Required
    public Answer answer;

    @Column(name="created_at")
    @CreatedTimestamp
    public Date createdAt;

}
