package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by Akatchi on 20-10-2014.
 */
@Entity
public class Category extends Model
{
    @Id
    public int id;

    @Constraints.Required
    public String name;

    @OneToMany(cascade = CascadeType.ALL)
    public List<KeywordCategory> keywordCategoryList;
}
