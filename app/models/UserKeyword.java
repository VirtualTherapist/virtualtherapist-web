package models;


import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Akatchi on 9-10-2014.
 */
@Entity
public class UserKeyword extends Model
{
    @Id
    public int id;

    @Constraints.Required
    public User userid;

    @Constraints.Required
    public Keyword keywordid;
}
