package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by Akatchi on 9-10-2014.
 */
@Entity
public class KeywordCategory extends Model
{
    @Id
    public Integer id;

    @Constraints.Required
    @OneToOne
    public Keyword keyword;

    @Constraints.Required
    @OneToOne
    public Category category;
}
