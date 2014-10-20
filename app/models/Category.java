package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

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
}
