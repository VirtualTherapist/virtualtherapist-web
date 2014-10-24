package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by bas on 23-10-14.
 */
@Entity
public class Chat extends Model {

    @Id
    public int id;

    @Constraints.Required
    @ManyToOne
    public User user;

    @Constraints.Required
    public double lat;

    @Constraints.Required
    public double lng;

    @Constraints.Required
    public String mood;

}
