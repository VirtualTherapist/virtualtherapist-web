package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Akatchi on 9-10-2014.
 */
@Entity
public class KeywordCategory extends Model {
    @Id
    public Integer id;

    @Constraints.Required
    @OneToOne
    public Keyword keyword;

    @Constraints.Required
    @ManyToOne
    public Category category;

    @OneToMany(cascade = CascadeType.ALL)
    public List<QuestionKeyword> questionKeywordList;
}
