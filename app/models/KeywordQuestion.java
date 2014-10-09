package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Id;

/**
 * Created by Akatchi on 9-10-2014.
 */
public class KeywordQuestion extends Model
{
    @Id
    public int id;

    @Constraints.Required
    public Question questionid;

    @Constraints.Required
    public Keyword keywordid;
}
