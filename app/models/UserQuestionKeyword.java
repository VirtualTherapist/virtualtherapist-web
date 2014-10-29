package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Created by Akatchi on 20-10-2014.
 */
@Entity
public class UserQuestionKeyword extends Model
{
    @Id
    public Integer id;

    @Constraints.Required
    @OneToOne
    public UserQuestion userquestion;

    @Constraints.Required
    @OneToOne
    public KeywordCategory keywordCategory;
}
