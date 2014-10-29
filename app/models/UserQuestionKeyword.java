package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * Created by Akatchi on 20-10-2014.
 */
@Entity
public class UserQuestionKeyword extends Model
{
    @Constraints.Required
    @OneToOne
    public UserQuestion userquestion;

    @Constraints.Required
    @OneToOne
    public Keyword keyword;
}
