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
public class Chat extends Model {

    @Id
    public int id;

    @Constraints.Required
    @OneToOne
    public User user;

    @Constraints.Required
    public double lat;

    @Constraints.Required
    public double lng;

    @Constraints.Required
    public String mood;

    @Column(name="created_at")
    @CreatedTimestamp
    public Date createdAt;

    public int rating = 0;
}
