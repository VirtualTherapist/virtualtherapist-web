package models;

import org.jboss.logging.Field;
import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Akatchi on 9-10-2014.
 */
@Entity
public class UserRole extends Model
{
    @Id
    public int id;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public int level;

}
