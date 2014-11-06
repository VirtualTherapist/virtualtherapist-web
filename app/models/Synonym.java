package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.jws.Oneway;
import javax.persistence.*;

/**
 * Created by bas on 6-11-14.
 */
@Entity
public class Synonym extends Model {

    @Id
    public int id;

    @Constraints.Required
    @ManyToOne
    public Keyword keyword;

    @Constraints.Required
    @ManyToOne
    public Keyword synonym;

}
