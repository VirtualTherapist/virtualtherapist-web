package models;

import com.avaje.ebean.Ebean;
import helpers.DatabaseFunctionsHelper;
import helpers.DatabaseHelper;
import modelsTest.DatabaseFunctions;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.running;

/**
 * CategoryModelTest , tests the category model.
 */
public class CategoryModelTest extends DatabaseHelper {

    @Test
    public void createCategoryModelTest(){
        running(fakeApp, new Runnable(){
            public void run(){
                Category category = DatabaseFunctionsHelper.createCategory("T");

                assertThat(category).isNotNull();
            }
        });
    }

    @Test
    public void deleteCategoryModelTest() {
        running(fakeApp, new Runnable() {
            public void run() {
                Category category = DatabaseFunctionsHelper.createCategory("T");

                DatabaseFunctionsHelper.deleteCategory(category);

                assertThat(Ebean.find(Category.class, category.id)).isNull();
            }
        });
    }

    @Test
    public void createCategoryAndKeywordTest(){
        running(fakeApp, new Runnable(){
            public void run(){
                Category category   = DatabaseFunctionsHelper.createCategory("T");
                Keyword keyword     = DatabaseFunctionsHelper.createKeyword("Keyword");
                KeywordCategory kc  = DatabaseFunctionsHelper.createKeywordCategory(keyword, category);

                assertThat(kc).isNotNull();
            }
        });
    }

    @Test
    public void deleteCategoryKeywordTest(){
        running(fakeApp, new Runnable(){
            public void run(){
                Category category   = DatabaseFunctionsHelper.createCategory("T");
                Keyword keyword     = DatabaseFunctionsHelper.createKeyword("Keyword");
                KeywordCategory kc  = DatabaseFunctionsHelper.createKeywordCategory(keyword, category);

                DatabaseFunctionsHelper.deleteKeywordCategory(kc);

                assertThat(Ebean.find(Category.class, category.id)).isNotNull(); // category stays
                assertThat(Ebean.find(Keyword.class, keyword.id)).isNotNull(); // keyword stays
                assertThat(Ebean.find(KeywordCategory.class, kc.id)).isNull(); // link between keyword and category is removed
            }
        });
    }

    @Test
    public void deleteCategoryCheckForKeywordTest(){
        running(fakeApp, new Runnable(){
            public void run(){
                Category category   = DatabaseFunctionsHelper.createCategory("T");
                Keyword keyword     = DatabaseFunctionsHelper.createKeyword("Keyword");
                KeywordCategory kc  = DatabaseFunctionsHelper.createKeywordCategory(keyword, category);

                DatabaseFunctionsHelper.deleteCategory(category);

                assertThat(Ebean.find(Category.class, category.id)).isNull(); // category is deleted
                assertThat(Ebean.find(Keyword.class, keyword.id)).isNotNull(); // keyword stay
                assertThat(Ebean.find(KeywordCategory.class, kc.id)).isNull();
            }
        });
    }
}
