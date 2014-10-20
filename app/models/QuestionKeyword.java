package models;


import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Created by Akatchi on 9-10-2014.
 */
@Entity
public class QuestionKeyword extends Model
{
    @Constraints.Required
    @OneToOne
    public Question question;

    @Constraints.Required
    @OneToOne
    public Keyword keyword;
}
