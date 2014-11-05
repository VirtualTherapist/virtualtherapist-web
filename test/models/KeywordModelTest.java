package models;

import com.avaje.ebean.Ebean;
import helpers.DatabaseFunctionsHelper;
import helpers.DatabaseHelper;
import modelsTest.DatabaseFunctions;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.running;

/**
 * KeywordModelTest, tests the keyword model and relations
 */
public class KeywordModelTest extends DatabaseHelper {

    @Test
    public void createKeywordModelTest(){
        running(fakeApp, new Runnable() {
            public void run() {
                Keyword keyword = DatabaseFunctionsHelper.createKeyword("Keyword");

                assertThat(Ebean.find(Keyword.class, keyword.id)).isNotNull();
            }
        });
    }

    @Test
    public void deleteKeywordModelTest(){
        running(fakeApp, new Runnable(){
            public void run(){
                Keyword keyword = DatabaseFunctionsHelper.createKeyword("Keyword");
                DatabaseFunctionsHelper.deleteKeyword(keyword);

                assertThat(Ebean.find(Keyword.class, keyword.id)).isNull();
            }
        });
    }

    @Test
    public void createKeywordCategoryTest() {
        running(fakeApp, new Runnable(){
            public void run(){
                Keyword keyword = DatabaseFunctionsHelper.createKeyword("Keyword");
                Category category = DatabaseFunctionsHelper.createCategory("T");
                KeywordCategory kc = DatabaseFunctionsHelper.createKeywordCategory(keyword, category);

                assertThat(Ebean.find(Keyword.class, keyword.id)).isNotNull();
                assertThat(Ebean.find(Category.class, category.id)).isNotNull();
                assertThat(Ebean.find(KeywordCategory.class, kc.id)).isNotNull();
            }
        });
    }

    @Test
    public void deleteKeywordCheckForCategoryTest() {
        running(fakeApp, new Runnable(){
            public void run(){
                Keyword keyword = DatabaseFunctionsHelper.createKeyword("Keyword");
                Category category = DatabaseFunctionsHelper.createCategory("T");
                KeywordCategory kc = DatabaseFunctionsHelper.createKeywordCategory(keyword, category);

                DatabaseFunctionsHelper.deleteKeyword(keyword);

                assertThat(Ebean.find(Keyword.class, keyword.id)).isNull(); // keyword gets deleted
                assertThat(Ebean.find(Category.class, category.id)).isNotNull(); // category stays
                assertThat(Ebean.find(KeywordCategory.class, kc.id)).isNull(); // Keywordcategory gets deleted
            }
        });
    }

    @Test
    public void deleteKeywordCategoryCheckKeywordTest(){
        running(fakeApp, new Runnable(){
            public void run(){
                Keyword keyword = DatabaseFunctionsHelper.createKeyword("Keyword");
                Category category = DatabaseFunctionsHelper.createCategory("T");
                KeywordCategory kc = DatabaseFunctionsHelper.createKeywordCategory(keyword, category);

                DatabaseFunctionsHelper.deleteKeywordCategory(kc);

                assertThat(Ebean.find(Keyword.class, keyword.id)).isNotNull(); // keyword gets deleted
                assertThat(Ebean.find(Category.class, category.id)).isNotNull(); // category stays
                assertThat(Ebean.find(KeywordCategory.class, kc.id)).isNull(); // Keywordcategory gets deleted
            }
        });
    }

    @Test
    public void createKeywordCategoryQuestionTest() {
        running(fakeApp, new Runnable(){
            public void run(){
                Keyword keyword = DatabaseFunctionsHelper.createKeyword("Keyword");
                Category category = DatabaseFunctionsHelper.createCategory("Test Category");
                KeywordCategory kc = DatabaseFunctionsHelper.createKeywordCategory(keyword, category);

                Question question = DatabaseFunctionsHelper.createQuestion("Test Vraag", null, null);

                QuestionKeyword qk = DatabaseFunctionsHelper.createQuestionKeyword(question, kc);

                assertThat(Ebean.find(QuestionKeyword.class, qk.id)).isNotNull();
                assertThat(Ebean.find(KeywordCategory.class, kc.id)).isNotNull();
            }
        });
    }

    @Test
    public void deleteKeywordCheckKeywordCategoryAndQuestionKeywordTest(){
        running(fakeApp, new Runnable(){
            public void run(){
                Keyword keyword = DatabaseFunctionsHelper.createKeyword("Keyword");
                Category category = DatabaseFunctionsHelper.createCategory("Test Category");
                KeywordCategory kc = DatabaseFunctionsHelper.createKeywordCategory(keyword, category);

                Question question = DatabaseFunctionsHelper.createQuestion("Test Vraag", null, null);

                QuestionKeyword qk = DatabaseFunctionsHelper.createQuestionKeyword(question, kc);

                // remove the kewyord
                DatabaseFunctionsHelper.deleteKeyword(keyword);

                assertThat(Ebean.find(Keyword.class, keyword.id)).isNull();
                assertThat(Ebean.find(KeywordCategory.class, kc.id)).isNull();
                assertThat(Ebean.find(QuestionKeyword.class, qk.id)).isNull();

                assertThat(Ebean.find(Category.class, category.id)).isNotNull();
                assertThat(Ebean.find(Question.class, question.id)).isNotNull();
            }
        });
    }
}
