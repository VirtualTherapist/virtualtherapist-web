package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL)
    public List<KeywordCategory> keywordCategoryList;

    @OneToMany(mappedBy = "synonym", cascade = CascadeType.ALL)
    public List<Synonym> synonyms;
}
