package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany
    @Constraints.Required
    public UserQuestion userQuestion;

    @ManyToMany(cascade=CascadeType.ALL)
    @Constraints.Required
    public List<Answer> answers;

}
