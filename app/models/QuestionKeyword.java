package models;


import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created by Akatchi on 9-10-2014.
 */
@Entity
public class QuestionKeyword extends Model
{
    @Id
    public Integer id;

    @Constraints.Required
    @OneToOne
    public Question question;

    @OneToOne
    public KeywordCategory keywordCategory;

}
