package models;

import play.data.validation.*;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Akatchi on 9-10-2014.
 */
@Entity
public class Answer extends Model
{
    @Id
    public int id;

    @Constraints.Required
    public String answer;

}
