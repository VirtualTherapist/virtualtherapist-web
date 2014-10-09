package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Akatchi on 9-10-2014.
 */
@Entity
public class Keyword extends Model
{
    @Id
    public int id;

    @Constraints.Required
    public String keyword;

    public KeywordCategory categoryid;

}
