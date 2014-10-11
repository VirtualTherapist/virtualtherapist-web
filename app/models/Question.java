package models;

import play.data.validation.*;
import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created by Akatchi on 9-10-2014.
 */
@Entity
public class Question extends Model
{
    @Id
    public int id;

    @Constraints.Required
    public String question;

    @OneToOne(cascade=CascadeType.ALL)
    public Answer answer;

}
